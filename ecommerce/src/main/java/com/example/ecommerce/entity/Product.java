package com.example.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Version
    private Long version;

    protected Product() {}

    public Product(String name, String description, BigDecimal price, int stock) {

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // ===== Business Methods =====

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (this.stock < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + name);
        }

        this.stock -= quantity;
    }

    // ===== Getters =====

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public BigDecimal getPrice() { return price; }

    public int getStock() { return stock; }
}
