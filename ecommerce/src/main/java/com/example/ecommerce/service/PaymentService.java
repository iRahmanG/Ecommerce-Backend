package com.example.ecommerce.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {
    private final Random random=new Random();

    public boolean processPayment(){
        //80% success, 20% failure
        int chance=random.nextInt(100);
        return chance<80;
    }
}
