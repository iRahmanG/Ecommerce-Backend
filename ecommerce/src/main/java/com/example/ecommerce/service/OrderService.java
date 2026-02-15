package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderItemResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final UserRepository userRepository;


    public OrderService(CartService cartService,
                        OrderRepository orderRepository,
                        ProductRepository productRepository,
                        PaymentService paymentService,
                        UserRepository userRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService=paymentService;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse placeOrder(Long cartId) {

        try{
            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(()-> new RuntimeException("User not found"));

            Cart cart = cartService.getCartEntity(cartId);

            //verify cart ownership
            if(!cart.getUser().getUsername().equals(username)){
                throw new AccessDeniedException("you cannot place order for this cart");
            }
            if(cart.getCartItems().isEmpty()){
                throw  new IllegalStateException("Cart is empty");
            }
            Order order = new Order(user);
            BigDecimal totalAmount = BigDecimal.ZERO;
            //  Validate stock
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = productRepository.findById(cartItem.getProduct().getId())
                        .orElseThrow(()-> new RuntimeException("Product not found"));

                // deduct stock
                product.reduceStock(cartItem.getQuantity());

                OrderItem orderItem = new OrderItem(
                        product,
                        cartItem.getQuantity(),
                        product.getPrice()
                );
                order.addOrderItem(orderItem);

                totalAmount = totalAmount.add(
                        product.getPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                );
            }
            //set total amount and save order in database
            order.setTotalAmount(totalAmount);
            order=orderRepository.save(order);

            // Simulate Payment
            boolean paymentSuccess = paymentService.processPayment();

            if(!paymentSuccess){
                order.markAsPaymentFailed();
                throw new RuntimeException("Payment failed. Transaction rolled back");
            }
            order.markAsPaid();

            // Clear cart
            cartService.clearCart(cart);

            return mapToResponse(order);

        }catch (OptimisticLockException e){
            throw new RuntimeException("Product was updated by another transaction. Please retry.");
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }


    private OrderResponse mapToResponse(Order order){

        List<OrderItemResponse> itemResponses = order.getOrderItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();
        return new OrderResponse(
                order.getOrderId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                itemResponses
        );
    }

    public List<OrderResponse> getOrdersForCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("user not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatus(Long orderId,OrderStatus status){
        Order order  = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("Order not found"));

        switch (status){
            case PAID -> order.markAsPaid();
            case SHIPPED -> order.markAsShipped();
            case DELIVERED -> order.markAsDelivered();
            case CANCELLED -> order.markAsCancel();
            default -> throw new IllegalStateException("Invalid transition");
        }

        return mapToResponse(orderRepository.save(order));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public OrderResponse cancelOrder(Long orderId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("Order not found with id: "+orderId));

        // verify ownership
        if(!order.getUser().getUsername().equals(username)){
            throw new AccessDeniedException("You cannot cancel this order");
        }
        // Restore stock

        for(OrderItem item: order.getOrderItems()){
            Product product=item.getProduct();
            product.increaseStock(item.getQuantity());
        }
        //change Status
        order.markAsCancel();

        return mapToResponse(order);
    }

}

