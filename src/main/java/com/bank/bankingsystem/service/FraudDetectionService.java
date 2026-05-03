package com.bank.bankingsystem.service;

// FraudDetectionService.java



import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.Transaction;

import java.math.BigDecimal;

public interface FraudDetectionService {
    void checkForFraud(Account account, BigDecimal amount, Transaction.TransactionType type);
}