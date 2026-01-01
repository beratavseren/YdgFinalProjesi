package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "werehouse")
public class Werehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long werehouseId;

    @Column(name = "werehouseName")
    private String werehouseName;

    @Column(name = "werehouseLocation")
    private String werehouseLocation;

    @Column(name = "currentWeigth")
    private Double currentWeigth;

    @Column(name = "currentVolume")
    private Double currentVolume;

    @Column(name = "weightLimit")
    private Double weigthLimit;

    @Column(name = "volumeLimit")
    private Double volumeLimit;

    public Werehouse(Long werehouseId, String werehouseName, String werehouseLocation, Double currentWeigth, Double currentVolume, Double weigthLimit, Double volumeLimit) {
        this.werehouseId = werehouseId;
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
        this.currentWeigth = currentWeigth;
        this.currentVolume = currentVolume;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
    }

    public Werehouse(String werehouseName, String werehouseLocation, Double currentWeigth, Double currentVolume, Double weigthLimit, Double volumeLimit) {
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
        this.currentWeigth = currentWeigth;
        this.currentVolume = currentVolume;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
    }

    public Werehouse(String werehouseName, String werehouseLocation, Double weigthLimit, Double volumeLimit) {
        this.werehouseName = werehouseName;
        this.werehouseLocation = werehouseLocation;
        this.weigthLimit = weigthLimit;
        this.volumeLimit = volumeLimit;
    }

    public Werehouse() {
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
