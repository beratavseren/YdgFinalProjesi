package org.example.ydgbackend.Dto.Product;

import org.example.ydgbackend.Dto.Brand.BrandDto;

public class AddProductDto {
    private String productName;
    private Long barcodeNumber;
    private BrandDto brandDto;
    private String comment;
    private int weigth;
    private int volume;

    public AddProductDto(String productName, Long barcodeNumber, BrandDto brandDto, String comment, int weigth, int volume) {
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.brandDto = brandDto;
        this.comment = comment;
        this.weigth = weigth;
        this.volume = volume;
    }

    public AddProductDto() {
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

    public BrandDto getBrandDto() {
        return brandDto;
    }

    public void setBrandDto(BrandDto brandDto) {
        this.brandDto = brandDto;
    }

    public int getWeigth() {
        return weigth;
    }

    public void setWeigth(int weigth) {
        this.weigth = weigth;
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
