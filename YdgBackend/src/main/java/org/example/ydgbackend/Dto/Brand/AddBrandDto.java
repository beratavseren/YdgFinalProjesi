package org.example.ydgbackend.Dto.Brand;

import java.util.List;

public class AddBrandDto {
    private String brandName;
    private List<CategoryDto> categoryDtos;

    public AddBrandDto(String brandName, List<CategoryDto> categoryDtos)
    {
        this.brandName=brandName;
        this.categoryDtos=categoryDtos;
    }

    public AddBrandDto() {
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<CategoryDto> getCategoryDtos() {
        return categoryDtos;
    }

    public void setCategoryDtos(List<CategoryDto> categoryDtos) {
        this.categoryDtos = categoryDtos;
    }
}
