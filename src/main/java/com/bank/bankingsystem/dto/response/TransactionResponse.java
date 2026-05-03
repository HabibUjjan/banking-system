package com.bank.bankingsystem.dto.response;

// TransactionResponse.java


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
public class TransactionResponse {
    private String transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String fromAccountHolder;
    private String toAccountHolder;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private String referenceNumber;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;
}
