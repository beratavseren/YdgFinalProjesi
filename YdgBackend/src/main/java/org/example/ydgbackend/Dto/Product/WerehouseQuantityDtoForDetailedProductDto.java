package org.example.ydgbackend.Dto.Product;

public class WerehouseQuantityDtoForDetailedProductDto {
    private String werehouseName;
    private int quantity;

    public WerehouseQuantityDtoForDetailedProductDto(String werehouseName, int quantity) {
        this.werehouseName = werehouseName;
        this.quantity = quantity;
    }

    public WerehouseQuantityDtoForDetailedProductDto() {
    }

    public String getWerehouseName() {
        return werehouseName;
    }

    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
