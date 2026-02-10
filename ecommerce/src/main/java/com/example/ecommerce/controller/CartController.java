package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartResponseDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public Cart createCart(){
        return cartService.createCart();
    }

    @GetMapping("/{cardId}")
    public CartResponseDto getCart(@PathVariable Long cartId){
        return cartService.getCartResponse(cartId);
    }

    @PostMapping("/{cartId}/items")
    public void addItem(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        cartService.addProductToCart(cartId,productId,quantity);
    }


}
