package com.example.ecommerce.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Order(User user){
        this.user=user;
        this.createdAt=LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    //Bidirectional consistency
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount){
        totalAmount=totalAmount;
    }
    //    Lifecycle methods
//    CREATED → PAID → SHIPPED → DELIVERED
//                ↘
//                PAYMENT_FAILED

    public  void markAsPaid(){
        if(this.status!=OrderStatus.CREATED){
            throw new IllegalStateException("Order cannot be marked as PAID");
        }
        this.status=OrderStatus.PAID;
    }
    public void markAsPaymentFailed(){
        if(this.status!=OrderStatus.CREATED){
            throw new IllegalStateException("payment failure only allowed from CREATED state");
        }
        this.status=OrderStatus.PAYMENT_FAILED;
    }

    public void markAsShipped(){
        if(this.status!=OrderStatus.PAID){
            throw new IllegalStateException("Order must be paid before shipping");
        }
        this.status=OrderStatus.SHIPPED;
    }

    public void markAsDelivered(){
        if(this.status!=OrderStatus.SHIPPED){
            throw new IllegalStateException("Order must be SHIPPED before delivery");
        }
        this.status=OrderStatus.DELIVERED;
    }

}
