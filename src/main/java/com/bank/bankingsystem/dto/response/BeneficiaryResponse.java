package com.bank.bankingsystem.dto.response;

// BeneficiaryResponse.java


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
public class BeneficiaryResponse {
    private Long id;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String bankName;
    private String relationship;
    private BigDecimal maxTransferLimit;
    private boolean isActive;
    private LocalDateTime createdAt;
}
