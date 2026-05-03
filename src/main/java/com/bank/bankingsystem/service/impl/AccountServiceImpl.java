package com.bank.bankingsystem.service.impl;

// AccountServiceImpl.java
import com.bank.bankingsystem.dto.request.AccountRequest;
import com.bank.bankingsystem.dto.response.AccountResponse;
import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.User;
import com.bank.bankingsystem.expection.DuplicateResourceException;
import com.bank.bankingsystem.expection.ResourceNotFoundException;
import com.bank.bankingsystem.repository.AccountRepository;
import com.bank.bankingsystem.repository.UserRepository;
import com.bank.bankingsystem.service.AccountService;
import com.bank.bankingsystem.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Value("${app.banking.account.min-balance}")
    private BigDecimal minimumBalance;

    @Override
    @Transactional
    public AccountResponse createAccount(String username, AccountRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check account limit per user
        long activeAccounts = accountRepository.findByUserAndStatus(
                user, Account.AccountStatus.ACTIVE).size();
        if (activeAccounts >= 5) {
            throw new DuplicateResourceException(
                    "Maximum number of active accounts (5) reached");
        }

        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .user(user)
                .accountType(Account.AccountType.valueOf(request.getAccountType()))
                .balance(request.getInitialDeposit() != null ?
                        request.getInitialDeposit() : BigDecimal.ZERO)
                .currency(request.getCurrency())
                .status(Account.AccountStatus.ACTIVE)
                .minimumBalance(minimumBalance)
                .build();

        // Set interest rate for savings accounts
        if (account.getAccountType() == Account.AccountType.SAVINGS) {
            account.setInterestRate(new BigDecimal("2.50"));
        }

        account = accountRepository.save(account);

        log.info("New account created: {} for user: {}",
                account.getAccountNumber(), username);

        return mapToAccountResponse(account);
    }

    @Override
    public AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "accountNumber", accountNumber));
        return mapToAccountResponse(account);
    }

    @Override
    public List<AccountResponse> getUserAccounts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Account> accounts = accountRepository.findByUser(user);

        return accounts.stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponse updateAccountStatus(String accountNumber, String status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "accountNumber", accountNumber));

        account.setStatus(Account.AccountStatus.valueOf(status));
        account = accountRepository.save(account);

        log.info("Account {} status updated to {}", accountNumber, status);

        return mapToAccountResponse(account);
    }

    @Override
    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "accountNumber", accountNumber));

        // Check if account has zero balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                    "Account must have zero balance before closing");
        }

        account.setStatus(Account.AccountStatus.CLOSED);
        accountRepository.save(account);

        log.info("Account {} closed", accountNumber);
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus().name())
                .interestRate(account.getInterestRate())
                .minimumBalance(account.getMinimumBalance())
                .accountHolder(account.getUser().getFirstName() + " " +
                        account.getUser().getLastName())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
