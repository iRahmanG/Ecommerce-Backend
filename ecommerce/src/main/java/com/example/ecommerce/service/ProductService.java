package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequestDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDto dto){
        Product product = new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getQuantity()
        );
        return productRepository.save(product);
    }
    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Product not found with id: "+id
                ));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
