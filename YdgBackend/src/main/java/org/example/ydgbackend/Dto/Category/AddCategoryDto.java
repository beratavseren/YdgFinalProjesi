package org.example.ydgbackend.Dto.Category;

public class AddCategoryDto {
    private String categoryName;

    public AddCategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public AddCategoryDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
