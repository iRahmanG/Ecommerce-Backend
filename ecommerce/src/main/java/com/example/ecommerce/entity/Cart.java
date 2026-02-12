package com.example.ecommerce.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "cart",
            cascade=CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> cartItems=new ArrayList<>();

//    protected Cart(){
//
//    }
    public Cart(){
        this.createdAt=LocalDateTime.now();
    }
    public Long getId(){
        return id;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public List<CartItem> getCartItems(){
        return cartItems;
    }

    public void addItem(CartItem item){
        item.setCart(this);
        cartItems.add(item);
    }

    public void remove(CartItem item){
        cartItems.remove(item);
        item.setCart(null);
    }
    public BigDecimal getTotalPrice(){
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public int getTotalQuantity(){
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

}
