package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDto {
    private Long cartId;
    private List<CartItemResponseDto> items;
    private BigDecimal totalPrice;
    private int totalQuantity;

    public CartResponseDto(Long cartId, List<CartItemResponseDto> items, BigDecimal totalPrice, int totalQuantity) {
        this.cartId = cartId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartItemResponseDto> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
