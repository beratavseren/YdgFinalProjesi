package org.example.ydgbackend.Dto.Stock;

public class SetCriticalStockLevelDto {
    private Long productId;
    private Long werehouseId;
    private int criticalStockLevel;

    public SetCriticalStockLevelDto(Long productId, Long werehouseId, int criticalStockLevel) {
        this.productId = productId;
        this.werehouseId = werehouseId;
        this.criticalStockLevel = criticalStockLevel;
    }

    public SetCriticalStockLevelDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getWerehouseId() {
        return werehouseId;
    }

    public void setWerehouseId(Long werehouseId) {
        this.werehouseId = werehouseId;
    }

    public int getCriticalStockLevel() {
        return criticalStockLevel;
    }

    public void setCriticalStockLevel(int criticalStockLevel) {
        this.criticalStockLevel = criticalStockLevel;
    }
}
