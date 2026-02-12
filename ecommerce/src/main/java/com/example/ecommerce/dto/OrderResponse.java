package com.example.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse(Long orderId,
                         String status,
                         LocalDateTime createdAt,
                         List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}
