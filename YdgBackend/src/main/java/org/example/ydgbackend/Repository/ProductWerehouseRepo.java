package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Product;
import org.example.ydgbackend.Entity.ProductWerehouse;
import org.example.ydgbackend.Entity.Werehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductWerehouseRepo extends JpaRepository<ProductWerehouse,Long> {
    public List<ProductWerehouse> findProductWerehousesByProduct(Product product);
    ProductWerehouse findProductWerehousesByProductAndWerehouse(Product product, Werehouse werehouse);

    List<ProductWerehouse> findProductWerehousesByWerehouse(Werehouse werehouse);

    void deleteProductWerehousesByWerehouse(Werehouse werehouse);
}
