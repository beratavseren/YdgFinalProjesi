package org.example.ydgbackend.Dto.WereHouse;

public class UpdateWerehouseDto {
    private Long werehouseId;

    private String werehouseName;

    private Double weigthLimit;

    private Double volumeLimit;

    public UpdateWerehouseDto(Long werehouseId, String werehouseName, Double weigthLimit, Double volumeLimit) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
    }

    public UpdateWerehouseDto(Long werehouseId, String werehouseName) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
    }

    public UpdateWerehouseDto() {
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

    public Double getVolumeLimit() {
        return volumeLimit;
    }

    public void setVolumeLimit(Double volumeLimit) {
        this.volumeLimit = volumeLimit;
    }
}
