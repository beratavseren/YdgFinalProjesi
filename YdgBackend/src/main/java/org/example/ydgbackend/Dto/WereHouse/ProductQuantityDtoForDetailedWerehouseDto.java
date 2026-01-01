package org.example.ydgbackend.Dto.WereHouse;

public class ProductQuantityDtoForDetailedWerehouseDto {
    private String productName;
    private int quantity;
    private Long productId;
    private int criticalLevel;

    public ProductQuantityDtoForDetailedWerehouseDto(String productName, int quantity, Long productId, int criticalLevel) {
        this.productName = productName;
        this.quantity = quantity;
        this.productId = productId;
        this.criticalLevel = criticalLevel;
    }

    public ProductQuantityDtoForDetailedWerehouseDto() {
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public int getCriticalLevel() {
        return criticalLevel;
    }
    public void setCriticalLevel(int criticalLevel) {
        this.criticalLevel = criticalLevel;
    }
}
