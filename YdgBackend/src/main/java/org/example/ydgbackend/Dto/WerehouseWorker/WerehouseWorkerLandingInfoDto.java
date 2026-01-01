package org.example.ydgbackend.Dto.WerehouseWorker;

public class WerehouseWorkerLandingInfoDto {
    private int totalQuantity;
    private Double weigthLimit;
    private Double currentWeigth;
    private int criticalLevelProductQuantity;

    public WerehouseWorkerLandingInfoDto(int totalQuantity, Double weigthLimit, Double currentWeigth, int criticalLevelProductQuantity) {
        this.totalQuantity = totalQuantity;
        this.weigthLimit = weigthLimit;
        this.currentWeigth = currentWeigth;
        this.criticalLevelProductQuantity = criticalLevelProductQuantity;
    }

    public WerehouseWorkerLandingInfoDto(){}

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public int getCriticalLevelProductQuantity() {
        return criticalLevelProductQuantity;
    }

    public void setCriticalLevelProductQuantity(int criticalLevelProductQuantity) {
        this.criticalLevelProductQuantity = criticalLevelProductQuantity;
    }
}
