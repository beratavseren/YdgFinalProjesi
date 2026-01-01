package org.example.ydgbackend.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AdminId;

    @Column(name = "adSoyad")
    private String adSoyad;

    @Column(name = "telNo")
    private String telNo;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;


    public Admin(Long adminId, String adSoyad, String telNo, String email, String password) {
        AdminId = adminId;
        this.adSoyad = adSoyad;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
    }

    public Admin(String adSoyad, String telNo, String email, String password) {
        this.adSoyad = adSoyad;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
    }

    public Admin() {
    }

    public Long getAdminId() {
        return AdminId;
    }

    public void setAdminId(Long adminId) {
        AdminId = adminId;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
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
