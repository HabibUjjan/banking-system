package com.bank.bankingsystem.dto.response;

// DashboardResponse.java


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {
    private BigDecimal totalBalance;
    private int totalAccounts;
    private int totalBeneficiaries;
    private int recentTransactionCount;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private List<AccountResponse> accounts;
    private List<TransactionResponse> recentTransactions;
}