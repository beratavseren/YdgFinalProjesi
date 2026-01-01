package org.example.ydgbackend.Dto.WereHouse;

import java.util.List;

public class DetailedWerehouseDto {
    private Long werehouseId;
    private String werehouseName;
    private String werehouseLocation;
    private Double weigthLimit;
    private Double volumeLimit;
    private Double currentWeigth;
    private Double currentVolume;
    private List<ProductQuantityDtoForDetailedWerehouseDto> productQuantityDtoForDetailedWerehouseDtos;

    public DetailedWerehouseDto(Long werehouseId, String werehouseName, String werehouseLocation, Double weigthLimit, Double volumeLimit, Double currentWeigth, Double currentVolume, List<ProductQuantityDtoForDetailedWerehouseDto> productQuantityDtoForDetailedWerehouseDto) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
        this.currentWeigth = currentWeigth;
        this.currentVolume = currentVolume;
        this.productQuantityDtoForDetailedWerehouseDtos=productQuantityDtoForDetailedWerehouseDto;
    }

    public DetailedWerehouseDto() {
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

    public Double getCurrentWeigth() {
        return currentWeigth;
    }

    public void setCurrentWeigth(Double currentWeigth) {
        this.currentWeigth = currentWeigth;
    }

    public Double getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(Double currentVolume) {
        this.currentVolume = currentVolume;
    }

    public List<ProductQuantityDtoForDetailedWerehouseDto> getProductQuantityDtoForDetailedWerehouseDtos() {
        return productQuantityDtoForDetailedWerehouseDtos;
    }

    public void setProductQuantityDtoForDetailedWerehouseDtos(List<ProductQuantityDtoForDetailedWerehouseDto> productQuantityDtoForDetailedWerehouseDtos) {
        this.productQuantityDtoForDetailedWerehouseDtos = productQuantityDtoForDetailedWerehouseDtos;
    }
}
