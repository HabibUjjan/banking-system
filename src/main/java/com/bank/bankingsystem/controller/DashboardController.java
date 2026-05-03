package com.bank.bankingsystem.controller;

// DashboardController.java

import com.bank.bankingsystem.dto.ApiResponse;
import com.bank.bankingsystem.dto.response.AccountResponse;
import com.bank.bankingsystem.dto.response.DashboardResponse;
import com.bank.bankingsystem.dto.response.TransactionResponse;
import com.bank.bankingsystem.service.AccountService;
import com.bank.bankingsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            Authentication authentication) {

        String username = authentication.getName();
        List<AccountResponse> accounts = accountService.getUserAccounts(username);

        BigDecimal totalBalance = accounts.stream()
                .map(AccountResponse::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<TransactionResponse> recentTransactions = null;
        if (!accounts.isEmpty()) {
            recentTransactions = transactionService.getRecentTransactions(
                    accounts.get(0).getAccountNumber(), 5);
        }

        DashboardResponse dashboard = DashboardResponse.builder()
                .totalBalance(totalBalance)
                .totalAccounts(accounts.size())
                .accounts(accounts)
                .recentTransactions(recentTransactions)
                .build();

        return ResponseEntity.ok(
                ApiResponse.success("Dashboard data retrieved successfully", dashboard));
    }
}
