package com.bank.bankingsystem.service.impl;

// BeneficiaryServiceImpl.java


import com.bank.bankingsystem.dto.request.BeneficiaryRequest;
import com.bank.bankingsystem.dto.response.BeneficiaryResponse;
import com.bank.bankingsystem.entity.Beneficiary;
import com.bank.bankingsystem.entity.User;
import com.bank.bankingsystem.expection.DuplicateResourceException;
import com.bank.bankingsystem.expection.ResourceNotFoundException;
import com.bank.bankingsystem.repository.BeneficiaryRepository;
import com.bank.bankingsystem.repository.UserRepository;
import com.bank.bankingsystem.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BeneficiaryResponse addBeneficiary(String username, BeneficiaryRequest request) {
        log.info("Adding beneficiary for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Check if beneficiary already exists for this user
        if (beneficiaryRepository.existsByUserAndBeneficiaryAccountNumber(
                user, request.getBeneficiaryAccountNumber())) {
            throw new DuplicateResourceException(
                    "Beneficiary with account number " + request.getBeneficiaryAccountNumber() +
                            " already exists");
        }

        // Check maximum beneficiaries limit (e.g., 20)
        long activeBeneficiaries = beneficiaryRepository.countByUserAndIsActiveTrue(user);
        if (activeBeneficiaries >= 20) {
            throw new IllegalStateException("Maximum number of beneficiaries (20) reached");
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .user(user)
                .beneficiaryAccountNumber(request.getBeneficiaryAccountNumber())
                .beneficiaryName(request.getBeneficiaryName())
                .bankName(request.getBankName())
                .relationship(request.getRelationship())
                .maxTransferLimit(request.getMaxTransferLimit() != null ?
                        request.getMaxTransferLimit() : new BigDecimal("10000.00"))
                .isActive(true)
                .build();

        beneficiary = beneficiaryRepository.save(beneficiary);

        log.info("Beneficiary added successfully: {} for user: {}",
                beneficiary.getId(), username);

        return mapToBeneficiaryResponse(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryResponse updateBeneficiary(Long beneficiaryId, BeneficiaryRequest request) {
        log.info("Updating beneficiary: {}", beneficiaryId);

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary not found with ID: " + beneficiaryId));

        // Update fields
        if (request.getBeneficiaryName() != null) {
            beneficiary.setBeneficiaryName(request.getBeneficiaryName());
        }
        if (request.getBankName() != null) {
            beneficiary.setBankName(request.getBankName());
        }
        if (request.getRelationship() != null) {
            beneficiary.setRelationship(request.getRelationship());
        }
        if (request.getMaxTransferLimit() != null) {
            beneficiary.setMaxTransferLimit(request.getMaxTransferLimit());
        }
        if (request.getBeneficiaryAccountNumber() != null) {
            // Check if new account number already exists for this user
            if (!request.getBeneficiaryAccountNumber().equals(
                    beneficiary.getBeneficiaryAccountNumber()) &&
                    beneficiaryRepository.existsByUserAndBeneficiaryAccountNumber(
                            beneficiary.getUser(), request.getBeneficiaryAccountNumber())) {
                throw new DuplicateResourceException(
                        "Beneficiary with account number " +
                                request.getBeneficiaryAccountNumber() + " already exists");
            }
            beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        }

        beneficiary = beneficiaryRepository.save(beneficiary);

        log.info("Beneficiary updated successfully: {}", beneficiaryId);

        return mapToBeneficiaryResponse(beneficiary);
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long beneficiaryId) {
        log.info("Deleting beneficiary: {}", beneficiaryId);

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary not found with ID: " + beneficiaryId));

        beneficiaryRepository.delete(beneficiary);

        log.info("Beneficiary deleted successfully: {}", beneficiaryId);
    }

    @Override
    public BeneficiaryResponse getBeneficiaryById(Long beneficiaryId) {
        log.info("Fetching beneficiary by ID: {}", beneficiaryId);

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary not found with ID: " + beneficiaryId));

        return mapToBeneficiaryResponse(beneficiary);
    }

    @Override
    public List<BeneficiaryResponse> getUserBeneficiaries(String username) {
        log.info("Fetching all beneficiaries for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        List<Beneficiary> beneficiaries = beneficiaryRepository.findByUser(user);

        return beneficiaries.stream()
                .map(this::mapToBeneficiaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeneficiaryResponse> getActiveBeneficiaries(String username) {
        log.info("Fetching active beneficiaries for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        List<Beneficiary> beneficiaries = beneficiaryRepository.findByUserAndIsActiveTrue(user);

        return beneficiaries.stream()
                .map(this::mapToBeneficiaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BeneficiaryResponse toggleBeneficiaryStatus(Long beneficiaryId, boolean active) {
        log.info("Toggling beneficiary status: {} to active={}", beneficiaryId, active);

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary not found with ID: " + beneficiaryId));

        beneficiary.setActive(active);
        beneficiary = beneficiaryRepository.save(beneficiary);

        log.info("Beneficiary status updated: {} is now active={}", beneficiaryId, active);

        return mapToBeneficiaryResponse(beneficiary);
    }

    private BeneficiaryResponse mapToBeneficiaryResponse(Beneficiary beneficiary) {
        return BeneficiaryResponse.builder()
                .id(beneficiary.getId())
                .beneficiaryAccountNumber(beneficiary.getBeneficiaryAccountNumber())
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .bankName(beneficiary.getBankName())
                .relationship(beneficiary.getRelationship())
                .maxTransferLimit(beneficiary.getMaxTransferLimit())
                .isActive(beneficiary.isActive())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }
}
