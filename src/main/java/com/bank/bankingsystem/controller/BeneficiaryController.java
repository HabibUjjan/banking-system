package com.bank.bankingsystem.controller;

// BeneficiaryController.java
import com.bank.bankingsystem.dto.ApiResponse;
import com.bank.bankingsystem.dto.request.BeneficiaryRequest;
import com.bank.bankingsystem.dto.response.BeneficiaryResponse;
import com.bank.bankingsystem.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> addBeneficiary(
            Authentication authentication,
            @Valid @RequestBody BeneficiaryRequest request) {
        BeneficiaryResponse beneficiary =
                beneficiaryService.addBeneficiary(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Beneficiary added successfully", beneficiary));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getBeneficiaries(
            Authentication authentication) {
        List<BeneficiaryResponse> beneficiaries =
                beneficiaryService.getUserBeneficiaries(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Beneficiaries retrieved successfully", beneficiaries));
    }

    @PutMapping("/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateBeneficiary(
            @PathVariable Long beneficiaryId,
            @Valid @RequestBody BeneficiaryRequest request) {
        BeneficiaryResponse beneficiary =
                beneficiaryService.updateBeneficiary(beneficiaryId, request);
        return ResponseEntity.ok(
                ApiResponse.success("Beneficiary updated successfully", beneficiary));
    }

    @DeleteMapping("/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficiary(
            @PathVariable Long beneficiaryId) {
        beneficiaryService.deleteBeneficiary(beneficiaryId);
        return ResponseEntity.ok(
                ApiResponse.success("Beneficiary deleted successfully", null));
    }
}
