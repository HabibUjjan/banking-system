// TransactionRepository.java
package com.bank.bankingsystem.repository;

import com.bank.bankingsystem.entity.Account;
import com.bank.bankingsystem.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    Page<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(
            Account fromAccount, Account toAccount, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findAccountTransactions(@Param("account") Account account);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.fromAccount = :account OR t.toAccount = :account) AND " +
            "t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountAndDateRange(
            @Param("account") Account account,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE " +
            "(t.fromAccount = :account OR t.toAccount = :account) AND " +
            "t.createdAt >= :since")
    long countTransactionsSince(@Param("account") Account account,
                                @Param("since") LocalDateTime since);
}