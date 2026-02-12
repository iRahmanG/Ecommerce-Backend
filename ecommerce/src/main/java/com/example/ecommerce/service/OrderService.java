package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;


    public OrderService(CartService cartService,
                        OrderRepository orderRepository,
                        ProductRepository productRepository,
                        PaymentService paymentService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService=paymentService;
    }

    @Transactional
    public Order placeOrder(Long cartId) {

        try{
            Cart cart = cartService.getCartEntity(cartId);

            if (cart.getCartItems().isEmpty()) {
                throw new IllegalStateException("Cart is empty");
            }

            //  Validate stock
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();

                if (product.getStock() < cartItem.getQuantity()) {
                    throw new IllegalStateException(
                            "Product out of stock: " + product.getName()
                    );
                }
            }

            //  Create Order
            Order order = new Order();
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.CREATED);

            List<OrderItem> orderItems = new ArrayList<>();

            //  Reduce stock + create order items
            for (CartItem cartItem : cart.getCartItems()) {

                Product product = cartItem.getProduct();
                product.setStock(product.getStock() - cartItem.getQuantity());

                OrderItem orderItem = new OrderItem(
                        product,
                        cartItem.getQuantity(),
                        product.getPrice()
                );
                order.addOrderItem(orderItem);
            }

            for (OrderItem item : orderItems) {
                order.addOrderItem(item);
            }
            Order savedOrder = orderRepository.save(order);

            // Simulate Payment
            boolean paymentSuccess = paymentService.processPayment();

            if(!paymentSuccess){
                savedOrder.markAsPaymentFailed();
                throw new RuntimeException("Payment failed. Transaction rolled back");
            }
            savedOrder.markAsPaid();
            // Clear cart
            cart.getCartItems().clear();

            return savedOrder;
        }catch (OptimisticLockException e){
            throw new RuntimeException("Product was updated by another transaction. Please retry.");
        }
    }
}

