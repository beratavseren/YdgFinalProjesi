package org.example.ydgbackend.Dto.Stock;

public class ProductTransactionDto {

    private Long productId;
    private int expectedQuantity;
    private int quantityReceived;

    public ProductTransactionDto(Long productId, int quantity) {
        this.productId = productId;
        this.expectedQuantity = quantity;
    }

    public ProductTransactionDto(Long productId, int quantityReceived, int expectedQuantity) {
        this.productId = productId;
        this.quantityReceived = quantityReceived;
        this.expectedQuantity = expectedQuantity;
    }

    public ProductTransactionDto() {}

    public Long getProductId() {return productId;}
    public void setProductId(Long productId) {this.productId = productId;}

    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public int getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(int quantityReceived) {
        this.quantityReceived = quantityReceived;
    }
}
