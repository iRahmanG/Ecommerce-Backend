package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request){
        OrderResponse response = orderService.placeOrder(request.getCartId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getOrders(Cart cart){
        return orderService.getAllOrders();
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> myOrders(){
        List<OrderResponse> response = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(response);

    }


}
