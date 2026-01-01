package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory,Long> {
}
