package com.bank.bankingsystem.dto.request;

// BeneficiaryRequest.java


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BeneficiaryRequest {
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{10,16}$", message = "Invalid account number")
    private String beneficiaryAccountNumber;

    @NotBlank(message = "Beneficiary name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String beneficiaryName;

    @Size(max = 100, message = "Bank name too long")
    private String bankName;

    @Size(max = 50, message = "Relationship too long")
    private String relationship;

    private BigDecimal maxTransferLimit;
}
