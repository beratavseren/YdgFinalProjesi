package org.example.ydgbackend.Dto.Product;

public class UpdateProductDto {
    private Long productId;
    private String productName;
    private String comment;

    public UpdateProductDto(Long productId, String productName, String comment) {
        this.productId = productId;
        this.productName = productName;
        this.comment = comment;
    }

    public UpdateProductDto() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
