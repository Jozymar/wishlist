package com.challenge.wishlist.service;

import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.repository.ProductRepository;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        if (optionalProduct.isPresent()) {
            return productRepository.save(product);
        }

        throw new RuntimeException("Product not found.");
    }

    public void deleteProduct(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
        } else {
            throw new RuntimeException("Product not found.");
        }
    }
}
