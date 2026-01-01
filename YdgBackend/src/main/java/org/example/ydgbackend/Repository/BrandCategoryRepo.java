package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.BrandCategory;
import org.example.ydgbackend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandCategoryRepo extends JpaRepository<BrandCategory,Long> {
    List<BrandCategory> findBrandCategoriesByBrand(Brand brand);
    List<BrandCategory> findBrandCategoriesByCategory(Category category);
    void deleteBrandCategoriesByBrand(Brand brand);
}
