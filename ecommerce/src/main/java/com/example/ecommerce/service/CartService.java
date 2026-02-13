package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartItemResponseDto;
import com.example.ecommerce.dto.CartResponseDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart createCart(){
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }
    public Cart getCart(Long cartId){
        return cartRepository.findCartWithItems(cartId)
                .orElseThrow(()->
                        new IllegalArgumentException("Cart not found with id: "+cartId)
                );
    }

    public void addProductToCart(Long cartId,Long productId, int quantity){
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Cart cart = getCart(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("Product not found")
                );
        CartItem cartItem =cartItemRepository.findByCartIdAndProductId(cartId,productId)
                .orElse(null);

        if(cartItem!=null){
            cartItem.increaseQuantity(quantity);
        }else{
            CartItem newItem = new CartItem(product,quantity);
            cart.addItem(newItem);
        }
    }
    public void removeItem(Long cartItemId){
        cartItemRepository.deleteById(cartItemId);
    }
    public CartResponseDto getCartResponse(Long cartId){
        Cart cart = getCart(cartId);

        List<CartItemResponseDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ))
                .toList();
        return new CartResponseDto(
                cart.getId(),
                items,
                cart.getTotalPrice(),
                cart.getTotalQuantity()
        );
    }

    public Cart getCartEntity(Long cartId){
        Cart cart= cartRepository.findById(cartId)
                .orElseThrow(()-> new EntityNotFoundException("Cart Entity not found with id: "+cartId)
                );
        return cart;
    }
}
