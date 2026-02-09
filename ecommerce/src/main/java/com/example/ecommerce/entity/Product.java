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
    private BigDecimal price;
    private int quantity;

    protected Product(){

    }

    public Product(String name, String description, BigDecimal price, int quantity) {
        if(price.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Price must be positive");
        }
        if(quantity<0){
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
