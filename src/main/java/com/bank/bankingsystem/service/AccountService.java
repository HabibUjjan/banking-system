package com.bank.bankingsystem.service;

// AccountService.java

import com.bank.bankingsystem.dto.request.AccountRequest;
import com.bank.bankingsystem.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(String username, AccountRequest request);
    AccountResponse getAccount(String accountNumber);
    List<AccountResponse> getUserAccounts(String username);
    AccountResponse updateAccountStatus(String accountNumber, String status);
    void closeAccount(String accountNumber);
}
