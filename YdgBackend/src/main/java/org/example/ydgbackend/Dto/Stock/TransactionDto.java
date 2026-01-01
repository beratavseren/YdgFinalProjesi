package org.example.ydgbackend.Dto.Stock;

import org.example.ydgbackend.Entity.TransactionType;

public class TransactionDto {

    private Long transactionId;
    private TransactionType transactionType;
    private String werehouseName;

    public TransactionDto(Long transactionId, TransactionType transactionType, String werehouseName) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.werehouseName = werehouseName;
    }

    public TransactionDto() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getWerehouseName() {
        return werehouseName;
    }

    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }
}
