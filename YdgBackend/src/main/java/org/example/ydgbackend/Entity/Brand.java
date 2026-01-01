package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(name = "brandName")
    private String brandName;

    public Brand(Long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public Brand(String brandName)
    {
        this.brandName=brandName;
    }

    public Brand() {
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
