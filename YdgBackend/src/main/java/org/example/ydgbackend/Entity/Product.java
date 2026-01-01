package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "barcodeNumber")
    private Long barcodeNumber;

    @Column(name = "wight")
    private int weight;

    @Column(name = "volume")
    private int volume;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    private Brand brand;

    public Product(Long productId, String productName, Long barcodeNumber, int weight, int volume, String comment, Brand brand) {
        this.productId = productId;
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.weight = weight;
        this.volume = volume;
        this.comment = comment;
        this.brand = brand;
    }

    public Product(Long productId, String productName, Long barcodeNumber, int weight, int volume, Brand brand) {
        this.productId = productId;
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.weight = weight;
        this.volume = volume;
        this.brand = brand;
    }


    public Product(Long productId, String productName, Long barcodeNumber, Brand brand) {
        this.productId = productId;
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.brand = brand;
    }

    public Product(String productName, Long barcodeNumber, int weight, int volume, Brand brand) {
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.weight = weight;
        this.volume = volume;
        this.brand = brand;
    }

    public Product(Long barcodeNumber, String productName, Brand brand) {
        this.barcodeNumber=barcodeNumber;
        this.productName=productName;
        this.brand=brand;
    }

    public Product(){}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(Long barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
