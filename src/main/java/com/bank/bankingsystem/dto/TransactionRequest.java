package com.bank.bankingsystem.dto;

// TransactionRequest.java


import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "50000.00", message = "Amount cannot exceed $50,000")
    private BigDecimal amount;

    @Size(max = 255, message = "Description too long")
    private String description;

    // For transfers
    private String toAccountNumber;
}
