package com.bank.bankingsystem.controller;

// TransactionController.java
import com.bank.bankingsystem.dto.ApiResponse;
import com.bank.bankingsystem.dto.TransactionRequest;
import com.bank.bankingsystem.dto.response.TransactionResponse;
import com.bank.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse transaction = transactionService.deposit(accountNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Deposit completed successfully", transaction));
    }

    @PostMapping("/withdraw/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse transaction = transactionService.withdraw(accountNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Withdrawal completed successfully", transaction));
    }

    @PostMapping("/transfer/{fromAccountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @PathVariable String fromAccountNumber,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse transaction = transactionService.transfer(fromAccountNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully", transaction));
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(
            @PathVariable String transactionId) {
        TransactionResponse transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", transaction));
    }

    @GetMapping("/account/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAccountTransactions(
            @PathVariable String accountNumber,
            Pageable pageable) {
        Page<TransactionResponse> transactions =
                transactionService.getAccountTransactions(accountNumber, pageable);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/account/{accountNumber}/recent")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getRecentTransactions(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "5") int limit) {
        List<TransactionResponse> transactions =
                transactionService.getRecentTransactions(accountNumber, limit);
        return ResponseEntity.ok(ApiResponse.success("Recent transactions retrieved successfully", transactions));
    }
}
