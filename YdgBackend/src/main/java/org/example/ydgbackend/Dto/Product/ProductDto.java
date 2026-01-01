package org.example.ydgbackend.Dto.Product;

public class ProductDto {

    private Long productId;
    private String productName;

    public ProductDto(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public ProductDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
