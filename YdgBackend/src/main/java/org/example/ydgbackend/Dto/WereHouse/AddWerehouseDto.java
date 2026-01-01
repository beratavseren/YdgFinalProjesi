package org.example.ydgbackend.Dto.WereHouse;

public class AddWerehouseDto {
    private String werehouseName;
    private String werehouseLocation;
    private Double weigthLimit;
    private Double volumeLimit;

    public AddWerehouseDto(String werehouseName, String werehouseLocation, Double weigthLimit, Double volumeLimit) {
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
    }

    public AddWerehouseDto(String werehouseName, String werehouseLocation) {
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
    }

    public AddWerehouseDto() {
    }

    public String getWerehouseName() {
        return werehouseName;
    }

    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }

    public String getWerehouseLocation() {
        return werehouseLocation;
    }

    public void setWerehouseLocation(String werehouseLocation) {
        this.werehouseLocation = werehouseLocation;
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
