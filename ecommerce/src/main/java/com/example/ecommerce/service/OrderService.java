package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    public OrderService(CartService cartService, OrderRepository orderRepository, ProductRepository productRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(Long cartId){
        Cart cart = cartService.getCartEntity(cartId);
        if(cart.getCartItems().isEmpty()){
            throw new IllegalStateException("Cart is Empty");
        }
        Order order = new Order();
        order.setUser(cart.getUser());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem: cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Product out of stock:");
            }
        }
        for(CartItem cartItem:cart.getCartItems()){
            Product product = cartItem.getProduct();

            product.setStock(product.getStock() - cartItem.getQuantity());
//            productRepository.save(product);  Hibernate handles automatically b checking Dirty Read

            OrderItem orderItem = new OrderItem(
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setPaymentStatus("CREATED")   ;

        Order savedOrder = orderRepository.save(order);
        cart.getCartItems().clear();

        return savedOrder;
    }
}
