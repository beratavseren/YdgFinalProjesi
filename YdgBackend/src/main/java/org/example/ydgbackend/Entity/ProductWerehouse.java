package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_werehouse")
public class ProductWerehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productWerehouseId;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Werehouse werehouse;

    @Column(name = "quantity")
    private int quantity;


    @Column(name = "criticalStockLevel")
    private int criticalStockLevel;



    public ProductWerehouse(Long productWerehouseId, Product product, Werehouse werehouse, int quantity, int criticalStockLevel) {
        this.productWerehouseId = productWerehouseId;
        this.product = product;
        this.werehouse = werehouse;
        this.quantity = quantity;
        this.criticalStockLevel = criticalStockLevel;
    }

    public ProductWerehouse(Product product, Werehouse werehouse, int quantity) {
        this.product = product;
        this.werehouse = werehouse;
        this.quantity = quantity;
    }

    public ProductWerehouse(Product product, Werehouse werehouse, int quantity, int criticalStockLevel) {
        this.product = product;
        this.werehouse = werehouse;
        this.quantity = quantity;
        this.criticalStockLevel = criticalStockLevel;
    }

    public ProductWerehouse() {
    }

    public Long getProductWerehouseId() {
        return productWerehouseId;
    }

    public void setProductWerehouseId(Long productWerehouseId) {
        this.productWerehouseId = productWerehouseId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Werehouse getWerehouse() {
        return werehouse;
    }

    public void setWerehouse(Werehouse werehouse) {
        this.werehouse = werehouse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCriticalStockLevel() {
        return criticalStockLevel;
    }

    public void setCriticalStockLevel(int criticalStockLevel) {
        this.criticalStockLevel = criticalStockLevel;
    }
}
