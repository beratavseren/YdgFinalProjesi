package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    private Werehouse werehouse;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "stuation")
    private Boolean situation;

    public Transaction(Long transactionId, Werehouse werehouse, TransactionType transactionType, Boolean situation) {
        this.transactionId = transactionId;
        this.werehouse = werehouse;
        this.transactionType = transactionType;
        this.situation = situation;
    }

    public Transaction(Werehouse werehouse, TransactionType transactionType, Boolean situation) {
        this.werehouse = werehouse;
        this.transactionType = transactionType;
        this.situation = situation;
    }

    public Transaction(Long transactionId, Werehouse werehouse, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.werehouse = werehouse;
        this.transactionType = transactionType;
    }

    public Transaction(Long transactionId, Werehouse werehouse) {
        this.transactionId = transactionId;
        this.werehouse = werehouse;
    }

    public Transaction(Werehouse werehouse, TransactionType transactionType) {
        this.werehouse = werehouse;
        this.transactionType = transactionType;
    }

    public Transaction() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }


    public Werehouse getWerehouse() {
        return werehouse;
    }

    public void setWerehouse(Werehouse werehouse) {
        this.werehouse = werehouse;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isSituation() {
        return situation;
    }

    public void setSituation(Boolean situation) {
        this.situation = situation;
    }
}
