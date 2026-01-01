package org.example.ydgbackend.Dto.Stock;

import java.util.List;

public class TransactionBetweenWerehouseDto {

    private Long fromWerehouseId;
    private Long toWerehouseId;
    private List<ProductTransactionDto> productTransactionDtos;

    public TransactionBetweenWerehouseDto(Long fromWerehouseId, Long toWerehouseId, List<ProductTransactionDto> productTransactionDtos) {
        this.fromWerehouseId = fromWerehouseId;
        this.toWerehouseId = toWerehouseId;
        this.productTransactionDtos = productTransactionDtos;
    }

    public TransactionBetweenWerehouseDto() {
    }


    public Long getFromWerehouseId() {
        return fromWerehouseId;
    }

    public void setFromWerehouseId(Long fromWerehouseId) {
        this.fromWerehouseId = fromWerehouseId;
    }

    public Long getToWerehouseId() {
        return toWerehouseId;
    }

    public void setToWerehouseId(Long toWerehouseId) {
        this.toWerehouseId = toWerehouseId;
    }

    public List<ProductTransactionDto> getProductTransactionDtos() {
        return productTransactionDtos;
    }

    public void setProductTransactionDtos(List<ProductTransactionDto> productTransactionDtos) {
        this.productTransactionDtos = productTransactionDtos;
    }
}
