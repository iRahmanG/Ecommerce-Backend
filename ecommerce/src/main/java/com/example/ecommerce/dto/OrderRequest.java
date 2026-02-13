package com.example.ecommerce.dto;

public class OrderRequest {
    private Long cartId;
    public Long getCartId(){
        return cartId;
    }
    public void setCartId(Long cartId){
        this.cartId=cartId;
    }
}
