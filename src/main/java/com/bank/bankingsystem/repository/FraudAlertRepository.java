package com.bank.bankingsystem.repository;

// FraudAlertRepository.java



import com.bank.bankingsystem.entity.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByIsResolvedFalse();
    List<FraudAlert> findByTransactionId(Long transactionId);
    List<FraudAlert> findBySeverity(FraudAlert.Severity severity);
}
