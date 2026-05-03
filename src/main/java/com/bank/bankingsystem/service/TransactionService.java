// TransactionService.java
package com.bank.bankingsystem.service;

import com.bank.bankingsystem.dto.TransactionRequest;
import com.bank.bankingsystem.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    // Core transaction operations
    TransactionResponse deposit(String accountNumber, TransactionRequest request);
    TransactionResponse withdraw(String accountNumber, TransactionRequest request);
    TransactionResponse transfer(String fromAccountNumber, TransactionRequest request);

    // Transaction retrieval operations
    TransactionResponse getTransactionById(String transactionId);
    TransactionResponse getTransaction(String transactionId);
    Page<TransactionResponse> getAccountTransactions(String accountNumber, Pageable pageable);
    List<TransactionResponse> getRecentTransactions(String accountNumber, int limit);
    List<TransactionResponse> getTransactionsByDateRange(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    // Admin operations
    List<TransactionResponse> getAllTransactions();
}