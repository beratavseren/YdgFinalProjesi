package org.example.ydgbackend.Dto.WereHouse;

public class WerehouseDto {
    private Long werehouseId;
    private String werehouseName;
    private Double weigthLimit;
    private Double currentWeigth;

    public WerehouseDto(Long werehouseId, String werehouseName) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
    }

    public WerehouseDto(Long werehouseId, String werehouseName, Double weigthLimit, Double currentWeigth) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
        this.weigthLimit = weigthLimit;
        this.currentWeigth = currentWeigth;
    }

    public WerehouseDto() {
    }

    public Long getWerehouseId() {
        return werehouseId;
    }

    public void setWerehouseId(Long werehouseId) {
        this.werehouseId = werehouseId;
    }

    public String getWerehouseName() {
        return werehouseName;
    }

    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }

    public Double getWeigthLimit() {
        return weigthLimit;
    }

    public void setWeigthLimit(Double weigthLimit) {
        this.weigthLimit = weigthLimit;
    }

    public Double getCurrentWeigth() {
        return currentWeigth;
    }

    public void setCurrentWeigth(Double currentWeigth) {
        this.currentWeigth = currentWeigth;
    }
}
