package com.bank.bankingsystem.dto.request;

// ScheduledTransferRequest.java


import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScheduledTransferRequest {
    @NotBlank(message = "Source account is required")
    private String fromAccountNumber;

    @NotBlank(message = "Destination account is required")
    private String toAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Frequency is required")
    @Pattern(regexp = "^(ONCE|DAILY|WEEKLY|MONTHLY)$",
            message = "Frequency must be ONCE, DAILY, WEEKLY, or MONTHLY")
    private String frequency;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate nextExecutionDate;

    private LocalDate endDate;

    @Size(max = 255, message = "Description too long")
    private String description;
}