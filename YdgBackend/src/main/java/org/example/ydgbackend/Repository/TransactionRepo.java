package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Dto.Stock.TransactionDto;
import org.example.ydgbackend.Entity.Transaction;
import org.example.ydgbackend.Entity.TransactionType;
import org.example.ydgbackend.Entity.Werehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByTransactionType(TransactionType transactionType);

    @Query("SELECT t FROM Transaction t " +
           "WHERE (:transactionType IS NULL OR t.transactionType = :transactionType) " +
           "AND (:werehouseId IS NULL OR t.werehouse.werehouseId = :werehouseId) " +
           "AND (:situation IS NULL OR t.situation = :situation)")
    List<Transaction> findTransactionsByTransactionTypeAndWerehouseIdAndSituation(
            @Param("transactionType") TransactionType transactionType,
            @Param("werehouseId") Long werehouseId,
            @Param("situation") Boolean situation
    );

    Transaction findTransactionByTransactionId(Long transactionId);

    List<Transaction> findTransactionsByWerehouse(Werehouse werehouse);

    @Query("SELECT new org.example.ydgbackend.Dto.Stock.TransactionDto(t.transactionId, t.transactionType, t.werehouse.werehouseName) " +
            "FROM Transaction t " +
            "WHERE (:transactionId IS NULL OR t.transactionId = :transactionId)")
    List<TransactionDto> searchTransactionsByTransactionId(@Param("transactionId") Long transactionId);
}
