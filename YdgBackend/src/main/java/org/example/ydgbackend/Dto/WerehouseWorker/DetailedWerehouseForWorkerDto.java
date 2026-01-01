package org.example.ydgbackend.Dto.WerehouseWorker;

import org.example.ydgbackend.Dto.WereHouse.ProductQuantityDtoForDetailedWerehouseDto;

import java.util.List;

public class DetailedWerehouseForWorkerDto {
    private String werehouseName;
    private List<ProductQuantityDtoForDetailedWerehouseDto> productForWorkerDtos;
    private String location;
    private Double weigthLimit;
    private Double volumeLimit;
    private Double currentWeigth;
    private Double currentVolume;

    public DetailedWerehouseForWorkerDto(String werehouseName, List<ProductQuantityDtoForDetailedWerehouseDto> productForWorkerDtos, String location, Double weigthLimit, Double volumeLimit, Double currentWeigth, Double currentVolume) {
        this.werehouseName = werehouseName;
        this.productForWorkerDtos = productForWorkerDtos;
        this.location = location;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
        this.currentWeigth = currentWeigth;
        this.currentVolume = currentVolume;
    }

    public DetailedWerehouseForWorkerDto() {}

    public String getWerehouseName() {
        return werehouseName;
    }

    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }

    public List<ProductQuantityDtoForDetailedWerehouseDto> getProductForWorkerDtos() {
        return productForWorkerDtos;
    }

    public void setProductForWorkerDtos(List<ProductQuantityDtoForDetailedWerehouseDto> productForWorkerDtos) {
        this.productForWorkerDtos = productForWorkerDtos;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
