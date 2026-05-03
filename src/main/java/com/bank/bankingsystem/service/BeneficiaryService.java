package com.bank.bankingsystem.service;

// BeneficiaryService.java

import com.bank.bankingsystem.dto.request.BeneficiaryRequest;
import com.bank.bankingsystem.dto.response.BeneficiaryResponse;

import java.util.List;

public interface BeneficiaryService {
    BeneficiaryResponse addBeneficiary(String username, BeneficiaryRequest request);
    BeneficiaryResponse updateBeneficiary(Long beneficiaryId, BeneficiaryRequest request);
    void deleteBeneficiary(Long beneficiaryId);
    BeneficiaryResponse getBeneficiaryById(Long beneficiaryId);
    List<BeneficiaryResponse> getUserBeneficiaries(String username);
    List<BeneficiaryResponse> getActiveBeneficiaries(String username);
    BeneficiaryResponse toggleBeneficiaryStatus(Long beneficiaryId, boolean active);
}
