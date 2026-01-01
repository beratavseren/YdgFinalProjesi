package org.example.ydgbackend.Dto.WerehouseWorker;

import org.example.ydgbackend.Dto.Stock.ProductTransactionDto;

import java.util.List;

public class ApproveTransactionDto {
    private Long transactionId;

    private List<ProductTransactionDto> productTransactionDtos;

    public ApproveTransactionDto(Long transactionId, List<ProductTransactionDto> productTransactionDtos) {
        this.transactionId = transactionId;
        this.productTransactionDtos = productTransactionDtos;
    }

    public ApproveTransactionDto() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public List<ProductTransactionDto> getProductTransactionDtos() {
        return productTransactionDtos;
    }

    public void setProductTransactionDtos(List<ProductTransactionDto> productTransactionDtos) {
        this.productTransactionDtos = productTransactionDtos;
    }
}

