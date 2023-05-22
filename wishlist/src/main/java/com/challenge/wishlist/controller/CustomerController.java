package com.challenge.wishlist.controller;

import com.challenge.wishlist.domain.Customer;
import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    @PostMapping("/{customerId}/wishlist")
    public ResponseEntity<Customer> addProductToWishlist(
            @PathVariable String customerId,
            @RequestBody Product product) {
        Customer customer = customerService.addProductToWishlist(customerId, product);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{customerId}/wishlist/{productId}")
    public ResponseEntity<Customer> removeProductFromWishlist(
            @PathVariable String customerId,
            @PathVariable String productId) {
        Customer customer = customerService.removeProductFromWishlist(customerId, productId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{customerId}/wishlist/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(
            @PathVariable String customerId,
            @PathVariable String productId) {
        boolean isInWishlist = customerService.isProductInWishlist(customerId, productId);
        return ResponseEntity.ok(isInWishlist);
    }

    @GetMapping("/{customerId}/wishlist")
    public ResponseEntity<List<Product>> getCustomerWishlist(
            @PathVariable String customerId) {
        List<Product> wishlist = customerService.getCustomerWishlist(customerId);
        return ResponseEntity.ok(wishlist);
    }
}
