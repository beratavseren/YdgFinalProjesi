package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "werehouseWorker")
public class WerehouseWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long werehouseWorkerId;

    @Column(name = "adSoyad")
    private String adSoyad;

    @Column(name = "telNo")
    private String telNo;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    private Werehouse werehouse;

    public WerehouseWorker(Long werehouseWorkerId, String adSoyad, String telNo, String email, String password, Werehouse werehouse) {
        this.werehouseWorkerId = werehouseWorkerId;
        this.adSoyad = adSoyad;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
        this.werehouse = werehouse;
    }

    public WerehouseWorker(String adSoyad, String telNo, String email, String password, Werehouse werehouse) {
        this.adSoyad = adSoyad;
        this.telNo = telNo;
        this.email = email;
        this.password = password;
        this.werehouse = werehouse;
    }

    public WerehouseWorker(Long werehouseWorkerId, String adSoyad, String telNo, Werehouse werehouse) {
        this.werehouseWorkerId = werehouseWorkerId;
        this.adSoyad = adSoyad;
        this.telNo = telNo;
        this.werehouse = werehouse;
    }

    public WerehouseWorker() {
    }

    public Long getWerehouseWorkerId() {
        return werehouseWorkerId;
    }

    public void setWerehouseWorkerId(Long werehouseWorkerId) {
        this.werehouseWorkerId = werehouseWorkerId;
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

    public Werehouse getWerehouse() {
        return werehouse;
    }

    public void setWerehouse(Werehouse werehouse) {
        this.werehouse = werehouse;
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
