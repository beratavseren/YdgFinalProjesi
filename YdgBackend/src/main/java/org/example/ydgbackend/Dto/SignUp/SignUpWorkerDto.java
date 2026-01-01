package org.example.ydgbackend.Dto.SignUp;

public class SignUpWorkerDto {
    private String nameSurname;
    private String telNo;
    private String email;
    private String password;
    private Long werehouseId;

    public SignUpWorkerDto(String nameSurname, String telNo, String email, String password, Long werehouseId) {
        this.nameSurname = nameSurname;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
        this.werehouseId = werehouseId;
    }

    public SignUpWorkerDto() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getWerehouseId() {
        return werehouseId;
    }

    public void setWerehouseId(Long werehouseId) {
        this.werehouseId = werehouseId;
    }
}
