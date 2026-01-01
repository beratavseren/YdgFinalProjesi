package org.example.ydgbackend.Dto.Brand;


import java.util.List;

public class UpdateBrandDto {
    private Long brandId;
    private String brandName;
    private List<CategoryDto> updatedCategoryList;
    private List<CategoryDto> addedCategoryList;

    public UpdateBrandDto(Long brandId, String brandName, List<CategoryDto> updatedCategoryList, List<CategoryDto> addedCategoryList) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.updatedCategoryList = updatedCategoryList;
        this.addedCategoryList = addedCategoryList;
    }

    public UpdateBrandDto(String brandName, List<CategoryDto> updatedCategoryList, List<CategoryDto> addedCategoryList) {
        this.brandName = brandName;
        this.updatedCategoryList = updatedCategoryList;
        this.addedCategoryList = addedCategoryList;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public UpdateBrandDto() {
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<CategoryDto> getUpdatedCategoryList() {
        return updatedCategoryList;
    }

    public void setUpdatedCategoryList(List<CategoryDto> updatedCategoryList) {
        this.updatedCategoryList = updatedCategoryList;
    }

    public List<CategoryDto> getAddedCategoryList() {
        return addedCategoryList;
    }

    public void setAddedCategoryList(List<CategoryDto> addedCategoryList) {
        this.addedCategoryList = addedCategoryList;
    }
}
