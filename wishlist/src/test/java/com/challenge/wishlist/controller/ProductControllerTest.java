package com.challenge.wishlist.controller;

import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.service.ProductService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        // Given
        Product product = new Product("product1", "product", "description1", 10);
        given(productService.createProduct(any(Product.class))).willReturn(product);

        // When/Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.description").value(product.getDescription()));
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        // Given
        Product product = new Product("product1", "product", "description1", 10);
        given(productService.updateProduct(any(Product.class))).willReturn(product);

        // When/Then
        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.description").value(product.getDescription()));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        // Given
        String productId = "product1";

        // When/Then
        mockMvc.perform(delete("/api/products/{productId}", productId))
                .andExpect(status().isNoContent());
    }
}
