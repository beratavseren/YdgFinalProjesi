package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productCategory")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryId;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Category category;

    public ProductCategory(Long productCategoryId, Product product, Category category) {
        this.productCategoryId = productCategoryId;
        this.product = product;
        this.category = category;
    }

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
    }

    public ProductCategory(){

    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }
    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Category getCategory() {
        return category;
    }
}
