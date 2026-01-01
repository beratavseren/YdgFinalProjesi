package org.example.ydgbackend.Dto.WerehouseWorker;

public class WerehouseWorkerProfileDto {
    private String nameSurname;
    private String telNo;
    private String email;
    private String werehouseName;

    public WerehouseWorkerProfileDto(String nameSurname, String telNo, String email, String werehouseName) {
        this.nameSurname = nameSurname;
        this.telNo = telNo;
        this.email = email;
        this.werehouseName = werehouseName;
    }

    public WerehouseWorkerProfileDto() {}

    public String getNameSurname() {
        return nameSurname;
    }
    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
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
    public String getWerehouseName() {
        return werehouseName;
    }
    public void setWerehouseName(String werehouseName) {
        this.werehouseName = werehouseName;
    }
}
