package com.bank.bankingsystem.repository;

// BeneficiaryRepository.java


import com.bank.bankingsystem.entity.Beneficiary;
import com.bank.bankingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUser(User user);
    List<Beneficiary> findByUserAndIsActiveTrue(User user);
    List<Beneficiary> findByUserAndIsActiveFalse(User user);

    Optional<Beneficiary> findByUserAndBeneficiaryAccountNumber(
            User user, String beneficiaryAccountNumber);

    boolean existsByUserAndBeneficiaryAccountNumber(
            User user, String beneficiaryAccountNumber);

    long countByUserAndIsActiveTrue(User user);

    List<Beneficiary> findByBeneficiaryAccountNumber(String beneficiaryAccountNumber);

    void deleteByUserAndId(User user, Long id);
}
