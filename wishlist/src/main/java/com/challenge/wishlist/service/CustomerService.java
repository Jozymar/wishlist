package com.challenge.wishlist.service;

import com.challenge.wishlist.domain.Customer;
import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final int WISHLIST_LIMIT = 20;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer addProductToWishlist(String customerId, Product product) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            if (customer.getWishlist() == null) {
                customer.setWishlist(new ArrayList<>());
            }

            if (customer.getWishlist().size() >= WISHLIST_LIMIT) {
                throw new RuntimeException("Wishlist has reached its limit.");
            }

            customer.getWishlist().add(product);

            return customerRepository.save(customer);
        }

        throw new RuntimeException("Customer not found.");
    }

    public Customer removeProductFromWishlist(String customerId, String productId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            customer.getWishlist().removeIf(product -> product.getId().equals(productId));

            return customerRepository.save(customer);
        }

        throw new RuntimeException("Customer not found.");
    }

    public boolean isProductInWishlist(String customerId, String productId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get().getWishlist().stream()
                    .anyMatch(product -> product.getId().equals(productId));
        }

        throw new RuntimeException("Customer not found.");
    }

    public List<Product> getCustomerWishlist(String customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get().getWishlist();
        }

        throw new RuntimeException("Customer not found.");
    }
}
