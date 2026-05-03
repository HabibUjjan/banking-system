package com.bank.bankingsystem.dto.response;

// AccountResponse.java


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    private String accountHolder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
