package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepo extends JpaRepository<Brand,Long> {
    Brand findByBrandId(Long brandId);
}
