// TransactionServiceImpl.java
package com.bank.bankingsystem.service.impl;

import com.bank.bankingsystem.dto.TransactionRequest;
import com.bank.bankingsystem.dto.response.TransactionResponse;
import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.Transaction;
import com.bank.bankingsystem.entity.User;
import com.bank.bankingsystem.expection.InsufficientBalanceException;
import com.bank.bankingsystem.expection.ResourceNotFoundException;
import com.bank.bankingsystem.repository.AccountRepository;
import com.bank.bankingsystem.repository.TransactionRepository;
import com.bank.bankingsystem.service.FraudDetectionService;
import com.bank.bankingsystem.service.NotificationService;
import com.bank.bankingsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public TransactionResponse deposit(String accountNumber, TransactionRequest request) {
        log.info("Processing deposit of {} to account {}", request.getAmount(), accountNumber);

        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // Fraud check
        fraudDetectionService.checkForFraud(account, request.getAmount(),
                Transaction.TransactionType.DEPOSIT);

        // Update balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .toAccount(account)
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.COMPLETED)
                .referenceNumber(UUID.randomUUID().toString())
                .build();

        transaction = transactionRepository.save(transaction);

        // Send notification
        notificationService.sendTransactionNotification(account.getUser(), transaction);

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(String accountNumber, TransactionRequest request) {
        log.info("Processing withdrawal of {} from account {}", request.getAmount(), accountNumber);

        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // Check sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Check minimum balance requirement
        if (account.getBalance().subtract(request.getAmount())
                .compareTo(account.getMinimumBalance()) < 0) {
            throw new InsufficientBalanceException(
                    "Withdrawal would violate minimum balance requirement");
        }

        // Fraud check
        fraudDetectionService.checkForFraud(account, request.getAmount(),
                Transaction.TransactionType.WITHDRAWAL);

        // Update balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .fromAccount(account)
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.COMPLETED)
                .referenceNumber(UUID.randomUUID().toString())
                .build();

        transaction = transactionRepository.save(transaction);

        // Send notification
        notificationService.sendTransactionNotification(account.getUser(), transaction);

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(String fromAccountNumber, TransactionRequest request) {
        log.info("Processing transfer of {} from {} to {}",
                request.getAmount(), fromAccountNumber, request.getToAccountNumber());

        Account fromAccount = accountRepository.findByAccountNumberForUpdate(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account toAccount = accountRepository.findByAccountNumberForUpdate(
                        request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Fraud check
        fraudDetectionService.checkForFraud(fromAccount, request.getAmount(),
                Transaction.TransactionType.TRANSFER);

        // Perform transfer atomically
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .transactionType(Transaction.TransactionType.TRANSFER)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.COMPLETED)
                .referenceNumber(UUID.randomUUID().toString())
                .build();

        transaction = transactionRepository.save(transaction);

        // Send notifications to both parties
        notificationService.sendTransactionNotification(fromAccount.getUser(), transaction);
        notificationService.sendTransactionNotification(toAccount.getUser(), transaction);

        return mapToTransactionResponse(transaction);
    }

    // ============ MISSING METHODS THAT NEED TO BE IMPLEMENTED ============

    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        log.info("Fetching transaction by ID: {}", transactionId);

        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with ID: " + transactionId));

        return mapToTransactionResponse(transaction);
    }

    @Override
    public TransactionResponse getTransaction(String transactionId) {
        return getTransactionById(transactionId);
    }

    @Override
    public Page<TransactionResponse> getAccountTransactions(String accountNumber, Pageable pageable) {
        log.info("Fetching transactions for account: {}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found: " + accountNumber));

        Page<Transaction> transactions = transactionRepository
                .findByFromAccountOrToAccountOrderByCreatedAtDesc(account, account, pageable);

        return transactions.map(this::mapToTransactionResponse);
    }

    @Override
    public List<TransactionResponse> getRecentTransactions(String accountNumber, int limit) {
        log.info("Fetching recent {} transactions for account: {}", limit, accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found: " + accountNumber));

        List<Transaction> transactions = transactionRepository.findAccountTransactions(account);

        return transactions.stream()
                .limit(limit)
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByDateRange(
            String accountNumber,
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate) {

        log.info("Fetching transactions for account: {} between {} and {}",
                accountNumber, startDate, endDate);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found: " + accountNumber));

        List<Transaction> transactions = transactionRepository
                .findByAccountAndDateRange(account, startDate, endDate);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        log.info("Fetching all transactions");

        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    // ============ HELPER METHODS ============

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse.TransactionResponseBuilder builder = TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .type(transaction.getTransactionType().toString())
                .status(transaction.getStatus().toString())
                .description(transaction.getDescription())
                .referenceNumber(transaction.getReferenceNumber())
                .timestamp(transaction.getCreatedAt());

        // Set from account details if exists
        if (transaction.getFromAccount() != null) {
            builder.fromAccountNumber(transaction.getFromAccount().getAccountNumber())
                    .fromAccountHolder(getAccountHolderName(transaction.getFromAccount()));
        }

        // Set to account details if exists
        if (transaction.getToAccount() != null) {
            builder.toAccountNumber(transaction.getToAccount().getAccountNumber())
                    .toAccountHolder(getAccountHolderName(transaction.getToAccount()));
        }

        return builder.build();
    }

    private String getAccountHolderName(Account account) {
        if (account != null && account.getUser() != null) {
            User user = account.getUser();
            return user.getFirstName() + " " + user.getLastName();
        }
        return "Unknown";
    }
}