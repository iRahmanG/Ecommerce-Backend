package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtAdd;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="cart_id",nullable=false)
    private Cart cart;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    protected CartItem(){
        //JPA
    }
    public  CartItem(Product product,int quantity){
        this.product=product;
        this.quantity=quantity;
        this.priceAtAdd=product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtAdd() {
        return priceAtAdd;
    }

    public Product getProduct() {
        return product;
    }

    public void increaseQuantity(int amount){
        if(amount<=0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity+=amount;
    }

    public void setCart(Cart cart){
        this.cart=cart;
    }
    public BigDecimal getTotalPrice(){
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }
}
