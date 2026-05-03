package com.bank.bankingsystem.repository;

// ScheduledTransferRepository.java
import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.ScheduledTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduledTransferRepository extends JpaRepository<ScheduledTransfer, Long> {
    List<ScheduledTransfer> findByFromAccount(Account account);
    List<ScheduledTransfer> findByStatus(ScheduledTransfer.Status status);

    @Query("SELECT s FROM ScheduledTransfer s WHERE s.status = 'ACTIVE' " +
            "AND s.nextExecutionDate <= :date")
    List<ScheduledTransfer> findScheduledTransfersForExecution(
            @Param("date") LocalDate date);
}
