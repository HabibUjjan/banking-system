package com.bank.bankingsystem.dto.request;

// AccountRequest.java


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountRequest {
    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(SAVINGS|CURRENT)$", message = "Account type must be SAVINGS or CURRENT")
    private String accountType;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter code")
    private String currency = "USD";

    private BigDecimal initialDeposit = BigDecimal.ZERO;
}
