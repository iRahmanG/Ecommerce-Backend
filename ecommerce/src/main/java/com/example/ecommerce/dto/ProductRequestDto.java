package com.example.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class ProductRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    @Positive
    private BigDecimal price;

    @NotBlank
    @PositiveOrZero
    private int quantity;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
