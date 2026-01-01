package org.example.ydgbackend.Dto.WerehouseWorker;

public class UpdateWerehouseWorkerProfileDto {
    private String telNo;
    private String email;

    public UpdateWerehouseWorkerProfileDto(String telNo, String email) {
        this.telNo = telNo;
        this.email = email;
    }

    public UpdateWerehouseWorkerProfileDto() {
    }

    public String getTelNo() {
        return telNo;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
