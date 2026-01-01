package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Brand;
import org.example.ydgbackend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Product findByProductId(Long productId);
    Product findByBarcodeNumber(Long barcodeNumber);
    List<Product> findProductsByBrand(Brand brand);
}
