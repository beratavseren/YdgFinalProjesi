package org.example.ydgbackend.Dto.Stock;


import org.example.ydgbackend.Entity.TransactionType;

import java.util.List;

public class AddTransactionDto {

    private Long werehouseId;
    private TransactionType transactionType;
    private List<ProductTransactionDto> productTransactionDtos;

    public AddTransactionDto(Long werehouseId, TransactionType transactionType, List<ProductTransactionDto> productTransactionDtos) {
        this.werehouseId = werehouseId;
        this.transactionType = transactionType;
        this.productTransactionDtos = productTransactionDtos;
    }

    public AddTransactionDto(Long werehouseId, List<ProductTransactionDto> productTransactionDtos) {
        this.werehouseId = werehouseId;
        this.productTransactionDtos = productTransactionDtos;
    }

    public AddTransactionDto() {
    }


    public Long getWerehouseId() {
        return werehouseId;
    }

    public void setWerehouseId(Long werehouseId) {
        this.werehouseId = werehouseId;
    }

    public List<ProductTransactionDto> getProductTransactionDtos() {
        return productTransactionDtos;
    }

    public void setProductTransactionDtos(List<ProductTransactionDto> productTransactionDtos) {
        this.productTransactionDtos = productTransactionDtos;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
