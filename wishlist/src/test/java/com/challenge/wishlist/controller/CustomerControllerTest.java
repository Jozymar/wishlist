package com.challenge.wishlist.controller;

import com.challenge.wishlist.domain.Customer;
import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void shouldAddProductToWishlist() throws Exception {
        // Given
        Product product = new Product("product1", "product", "description1", 10);
        List<Product> wishlist = new ArrayList<>();
        wishlist.add(product);
        Customer customer = new Customer("customer1","customer", "email1", wishlist);
        given(customerService.addProductToWishlist(any(String.class), any(Product.class))).willReturn(customer);

        // When/Then
        mockMvc.perform(post("/api/customers/customer1/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.wishlist[0].id").value(product.getId()))
                .andExpect(jsonPath("$.wishlist[0].name").value(product.getName()))
                .andExpect(jsonPath("$.wishlist[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$.wishlist[0].price").value(product.getPrice()));
    }

    @Test
    public void shouldRemoveProductFromWishlist() throws Exception {
        // Given
        String customerId = "customer1";
        String productId = "product1";

        // When/Then
        mockMvc.perform(delete("/api/customers/{customerId}/wishlist/{productId}", customerId, productId))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllProductsFromWishlist() throws Exception {
        // Given
        String customerId = "customer1";
        Product product1 = new Product("product1", "product", "description1", 10);
        Product product2 = new Product("product2", "product2", "description2", 20);
        List<Product> wishlist = new ArrayList<>();
        wishlist.add(product1);
        wishlist.add(product2);
        given(customerService.getCustomerWishlist(customerId)).willReturn(wishlist);

        // When/Then
        mockMvc.perform(get("/api/customers/{customerId}/wishlist", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].description").value(product2.getDescription()));
    }

    @Test
    public void shouldCheckIfProductIsInWishlist() throws Exception {
        // Given
        String customerId = "customer1";
        String productId = "product1";
        given(customerService.isProductInWishlist(customerId, productId)).willReturn(true);

        // When/Then
        mockMvc.perform(get("/api/customers/{customerId}/wishlist/{productId}", customerId, productId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
