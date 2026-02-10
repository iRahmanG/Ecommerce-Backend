package com.example.ecommerce.dto;

import java.math.BigDecimal;

public class CartItemResponseDto {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;

    public CartItemResponseDto(
            Long productId,
            String productName,
            BigDecimal price,
            int quantity,
            BigDecimal totalPrice
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
