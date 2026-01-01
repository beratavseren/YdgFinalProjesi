package org.example.ydgbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table
public class ProductTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productTransactionId;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Transaction transaction;

    @Column(name = "expectedQuantity")
    private int expectedQuantity;

    @Column(name = "quantityReceived")
    private int quantityReceived;

    public ProductTransaction(Long productTransactionId, Product product, Transaction transaction, int expectedQuantity, int quantityReceived) {
        this.productTransactionId = productTransactionId;
        this.product = product;
        this.transaction = transaction;
        this.expectedQuantity = expectedQuantity;
        this.quantityReceived = quantityReceived;
    }

    public ProductTransaction(Long productTransactionId, Product product, Transaction transaction, int quantity) {
        this.productTransactionId = productTransactionId;
        this.product = product;
        this.transaction = transaction;
        this.expectedQuantity = quantity;
    }

    public ProductTransaction(int quantity, Product product, Transaction transaction) {
        this.expectedQuantity = quantity;
        this.product = product;
        this.transaction = transaction;
    }

    public ProductTransaction() {}

    public Long getProductTransactionId() {return productTransactionId;}

    public void setProductTransactionId(Long productTransactionId) {this.productTransactionId = productTransactionId;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public int getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(int quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public Transaction getTransaction() {return transaction;}

    public void setTransaction(Transaction transaction) {this.transaction = transaction;}

}
