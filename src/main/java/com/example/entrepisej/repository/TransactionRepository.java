package com.example.entrepisej.repository;

import java.time.LocalDateTime;
import com.example.entrepisej.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByContractEmployeeId(Long employeeId);

    // CORRECCIÓN: Quitamos el WHERE t.type porque no existe en tu modelo
    @Query("SELECT SUM(t.amount) FROM Transaction t")
    Double sumTotalRevenue();

    @Query("SELECT SUM(t.amount) FROM Transaction t")
    Double getTotalEnterpriseFlow();

    @Query("SELECT AVG(t.amount) FROM Transaction t WHERE t.paymentDate > :startDate")
    Double getAverageTransactionSince(LocalDateTime startDate);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.amount > 1000")
    Long countLargeTransactions();
}