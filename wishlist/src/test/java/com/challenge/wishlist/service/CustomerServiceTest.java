package com.challenge.wishlist.service;

import com.challenge.wishlist.domain.Customer;
import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void addProductToWishlist() {
        // Given
        Product product = new Product("product1", "product", "description1", 10);
        Customer customer = new Customer("customer1", "customer", "email1", new ArrayList<>());
        given(customerRepository.findById(any(String.class))).willReturn(Optional.of(customer));

        // When
        customerService.addProductToWishlist("customer1", product);

        // Then
        then(customerRepository).should().save(any(Customer.class));
    }

    @Test
    public void removeProductFromWishlist() {
        // Given
        String customerId = "customer1";
        String productId = "product1";
        Product product = new Product(productId, "product", "description1", 10);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Customer customer = new Customer(customerId, "customer","email1", productList);
        given(customerRepository.findById(any(String.class))).willReturn(Optional.of(customer));

        // When
        customerService.removeProductFromWishlist(customerId, productId);

        // Then
        then(customerRepository).should().save(any(Customer.class));
    }

    @Test
    public void isProductInWishlist() {
        // Given
        String customerId = "customer1";
        String productId = "product1";
        Product product = new Product(productId, "product", "description1", 10);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Customer customer = new Customer(customerId, "customer","email1", productList);
        given(customerRepository.findById(any(String.class))).willReturn(Optional.of(customer));

        // When
        boolean isInWishlist = customerService.isProductInWishlist(customerId, productId);

        // Then
        then(customerRepository).should().findById(any(String.class));
        assertTrue(isInWishlist);
    }

    @Test
    public void getCustomerWishlist() {
        // Given
        String customerId = "customer1";
        String productId = "product1";
        Product product = new Product(productId, "product", "description1", 10);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Customer customer = new Customer(customerId, "customer", "email1", productList);
        given(customerRepository.findById(any(String.class))).willReturn(Optional.of(customer));

        // When
        List<Product> wishlist = customerService.getCustomerWishlist(customerId);

        // Then
        then(customerRepository).should().findById(any(String.class));
        assertEquals(1, wishlist.size());
    }
}
