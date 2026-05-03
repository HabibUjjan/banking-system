package com.bank.bankingsystem.service.impl;// FraudDetectionServiceImpl.java


import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.FraudAlert;
import com.bank.bankingsystem.entity.Transaction;
import com.bank.bankingsystem.repository.FraudAlertRepository;
import com.bank.bankingsystem.repository.TransactionRepository;
import com.bank.bankingsystem.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;

    @Value("${app.banking.fraud.large-transaction-threshold}")
    private BigDecimal largeTransactionThreshold;

    @Value("${app.banking.fraud.max-daily-transactions}")
    private int maxDailyTransactions;

    @Override
    public void checkForFraud(Account account, BigDecimal amount,
                              Transaction.TransactionType type) {

        // Check for large transactions
        if (amount.compareTo(largeTransactionThreshold) > 0) {
            createFraudAlert(null, FraudAlert.AlertType.LARGE_TRANSACTION,
                    "Large transaction detected: $" + amount,
                    FraudAlert.Severity.HIGH);
            log.warn("Large transaction alert: Account {} attempted {} of ${}",
                    account.getAccountNumber(), type, amount);
        }

        // Check daily transaction count
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        long dailyTransactions = transactionRepository
                .findByAccountAndDateRange(account, startOfDay, LocalDateTime.now())
                .size();

        if (dailyTransactions >= maxDailyTransactions) {
            createFraudAlert(null, FraudAlert.AlertType.SUSPICIOUS_PATTERN,
                    "Exceeded maximum daily transactions",
                    FraudAlert.Severity.MEDIUM);
            log.warn("Maximum daily transactions exceeded for account {}",
                    account.getAccountNumber());
        }
    }

    private void createFraudAlert(Transaction transaction,
                                  FraudAlert.AlertType type,
                                  String description,
                                  FraudAlert.Severity severity) {
        FraudAlert alert = FraudAlert.builder()
                .transaction(transaction)
                .alertType(type)
                .description(description)
                .severity(severity)
                .isResolved(false)
                .build();
        fraudAlertRepository.save(alert);
    }
}