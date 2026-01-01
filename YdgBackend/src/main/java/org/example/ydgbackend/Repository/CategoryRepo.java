package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findCategoryByCategoryId(Long categoryId);
    Category findByCategoryId(Long categoryId);
}
