package org.example.ydgbackend.Dto.Product;

import org.example.ydgbackend.Dto.Brand.BrandDto;

import java.util.List;

public class DetailedProductDto {
    private Long productId;
    private String productName;
    private Long barcodeNumber;
    private String comment;
    private int weigth;
    private int volume;
    private List<WerehouseQuantityDtoForDetailedProductDto> werehouseQuantityDtoForDetailedProductDtos;
    private BrandDto brandDto;

    public DetailedProductDto(Long productId, String productName, Long barcodeNumber, String comment, int weigth, int volume, List<WerehouseQuantityDtoForDetailedProductDto> werehouseQuantityDtoForDetailedProductDtos, BrandDto brandDto) {
        this.productId = productId;
        this.productName = productName;
        this.barcodeNumber = barcodeNumber;
        this.comment = comment;
        this.weigth = weigth;
        this.volume = volume;
        this.werehouseQuantityDtoForDetailedProductDtos = werehouseQuantityDtoForDetailedProductDtos;
        this.brandDto = brandDto;
    }

    public DetailedProductDto() {
    }

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

    public List<WerehouseQuantityDtoForDetailedProductDto> getWerehouseQuantityDtoForDetailedProductDtos() {
        return werehouseQuantityDtoForDetailedProductDtos;
    }

    public void setWerehouseQuantityDtoForDetailedProductDtos(List<WerehouseQuantityDtoForDetailedProductDto> werehouseQuantityDtoForDetailedProductDtos) {
        this.werehouseQuantityDtoForDetailedProductDtos = werehouseQuantityDtoForDetailedProductDtos;
    }
}
