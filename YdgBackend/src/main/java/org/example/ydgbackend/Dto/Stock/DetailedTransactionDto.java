package org.example.ydgbackend.Dto.Stock;

import org.example.ydgbackend.Entity.TransactionType;

import java.util.List;

public class DetailedTransactionDto {
    private Long transactionId;
    private TransactionType transactionType;
    private String werehouseName;
    private Boolean stuation;
    private List<ProductTransactionDto> productTransactionDtos;

    public DetailedTransactionDto(Long transactionId, TransactionType transactionType, String werehouseName, Boolean stuation, List<ProductTransactionDto> productTransactionDtos) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.werehouseName = werehouseName;
        this.stuation = stuation;
        this.productTransactionDtos = productTransactionDtos;
    }

    public DetailedTransactionDto() {
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
    public Boolean getStuation() {
        return stuation;
    }

    public void setStuation(Boolean stuation) {
        this.stuation = stuation;
    }
    public List<ProductTransactionDto> getProductTransactionDtos() {
        return productTransactionDtos;
    }
    public void setProductTransactionDtos(List<ProductTransactionDto> productTransactionDtos) {
        this.productTransactionDtos = productTransactionDtos;
    }
}
