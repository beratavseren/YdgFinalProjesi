package org.example.ydgbackend.Dto.Brand;

public class BrandDto {
    private String brandName;
    private Long brandId;

    public BrandDto(Long brandId, String brandName)
    {
        this.brandName=brandName;
        this.brandId=brandId;
    }

    public BrandDto(String brandName) {
        this.brandName = brandName;
    }

    public BrandDto(){}

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
