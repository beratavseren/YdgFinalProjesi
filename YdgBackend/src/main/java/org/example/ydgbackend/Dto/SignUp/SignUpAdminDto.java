package org.example.ydgbackend.Dto.SignUp;

public class SignUpAdminDto {
    private String nameSurname;
    private String telNo;
    private String email;
    private String password;

    public SignUpAdminDto(String nameSurname, String telNo, String email, String password) {
        this.nameSurname = nameSurname;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
    }

    public SignUpAdminDto() {
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
}
