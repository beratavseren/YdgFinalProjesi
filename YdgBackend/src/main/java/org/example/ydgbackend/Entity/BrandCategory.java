package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brandCategory")
public class BrandCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandCategoryId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    public BrandCategory(Long brandCategoryId, Brand brand, Category category) {
        this.brandCategoryId = brandCategoryId;
        this.brand = brand;
        this.category = category;
    }

    public BrandCategory(Brand brand, Category category) {
        this.brand = brand;
        this.category = category;
    }

    public BrandCategory() {
    }

    public Long getBrandCategoryId() {
        return brandCategoryId;
    }

    public void setBrandCategoryId(Long brandCategoryId) {
        this.brandCategoryId = brandCategoryId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}