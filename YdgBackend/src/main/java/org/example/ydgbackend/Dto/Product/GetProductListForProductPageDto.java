package org.example.ydgbackend.Dto.Product;

import org.example.ydgbackend.Dto.Brand.BrandDto;

public class GetProductListForProductPageDto {

    private Long productId;
    private String productName;
    private int totalQuantity;
    private BrandDto brandDto;


    public GetProductListForProductPageDto(Long productId, String productName, int totalQuantity, BrandDto brandDto) {
        this.productId = productId;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.brandDto = brandDto;
    }

    public GetProductListForProductPageDto() {
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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BrandDto getBrandDto() {
        return brandDto;
    }

    public void setBrandDto(BrandDto brandDto) {
        this.brandDto = brandDto;
    }

}
