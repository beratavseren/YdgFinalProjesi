package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.ProductTransaction;
import org.example.ydgbackend.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTransactionRepo extends JpaRepository<ProductTransaction,Long> {
    List<ProductTransaction> findProductTransactionsByTransaction(Transaction transaction);
}
