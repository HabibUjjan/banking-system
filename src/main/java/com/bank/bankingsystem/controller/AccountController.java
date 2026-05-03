package com.bank.bankingsystem.controller;

// AccountController.java

import com.bank.bankingsystem.dto.ApiResponse;
import com.bank.bankingsystem.dto.request.AccountRequest;
import com.bank.bankingsystem.dto.response.AccountResponse;
import com.bank.bankingsystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            Authentication authentication,
            @Valid @RequestBody AccountRequest request) {
        AccountResponse account = accountService.createAccount(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully", account));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getUserAccounts(
            Authentication authentication) {
        List<AccountResponse> accounts = accountService.getUserAccounts(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @PathVariable String accountNumber) {
        AccountResponse account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", account));
    }

    @PutMapping("/{accountNumber}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestParam String status) {
        AccountResponse account = accountService.updateAccountStatus(accountNumber, status);
        return ResponseEntity.ok(ApiResponse.success("Account status updated successfully", account));
    }

    @DeleteMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> closeAccount(
            @PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully", null));
    }
}