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
    private List<CartItem> items=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

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
        return items;
    }

    public void addItem(CartItem item){
        item.setCart(this);
        items.add(item);
    }

    public void removeItem(CartItem item){
        items.remove(item);
        item.setCart(null);
    }
    public BigDecimal getTotalPrice(){
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public int getTotalQuantity(){
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public User getUser() {
        return user;
    }
}
