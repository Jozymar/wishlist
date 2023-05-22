package com.challenge.wishlist.service;

import com.challenge.wishlist.domain.Product;
import com.challenge.wishlist.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldCreateProduct() {
        // Given
        Product product = new Product("product1", "product", "description1", 10);
        given(productRepository.save(any(Product.class))).willReturn(product);

        // When
        Product createdProduct = productService.createProduct(product);

        // Then
        then(productRepository).should().save(any(Product.class));
        assertNotNull(createdProduct);
        assertEquals(product.getId(), createdProduct.getId());
        assertEquals(product.getDescription(), createdProduct.getDescription());
    }

    @Test
    public void shouldUpdateExistingProduct() {
        // Given
        Product existingProduct = new Product("product1", "product", "description1", 10);
        given(productRepository.findById("product1")).willReturn(Optional.of(existingProduct));
        given(productRepository.save(any(Product.class))).willReturn(existingProduct);

        // When
        Product updatedProduct = productService.updateProduct(existingProduct);

        // Then
        then(productRepository).should().findById("product1");
        then(productRepository).should().save(any(Product.class));
        assertNotNull(updatedProduct);
        assertEquals(existingProduct.getId(), updatedProduct.getId());
        assertEquals(existingProduct.getDescription(), updatedProduct.getDescription());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonexistentProduct() {
        // Given
        Product nonExistentProduct = new Product("product1", "product", "description1", 10);
        given(productRepository.findById("product1")).willReturn(Optional.empty());

        // When/Then
        assertThrows(RuntimeException.class, () -> productService.updateProduct(nonExistentProduct));
        then(productRepository).should().findById("product1");
        then(productRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void shouldDeleteExistingProduct() {
        // Given
        Product existingProduct = new Product("product1", "product", "description1", 10);
        given(productRepository.findById("product1")).willReturn(Optional.of(existingProduct));

        // When
        productService.deleteProduct("product1");

        // Then
        then(productRepository).should().findById("product1");
        then(productRepository).should().delete(existingProduct);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonexistentProduct() {
        // Given
        given(productRepository.findById("product1")).willReturn(Optional.empty());

        // When/Then
        assertThrows(RuntimeException.class, () -> productService.deleteProduct("product1"));
        then(productRepository).should().findById("product1");
        then(productRepository).shouldHaveNoMoreInteractions();
    }
}
