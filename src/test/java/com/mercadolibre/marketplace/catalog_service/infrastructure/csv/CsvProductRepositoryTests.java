package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvProductRepositoryTests {

    private CsvProductRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        ResourceLoader resourceLoader = mock(ResourceLoader.class);

        // Apuntar al archivo real en test/resources/data_test/products.csv
        Resource resource = new ClassPathResource("data_test/products.csv");
        when(resourceLoader.getResource("classpath:data/products.csv")).thenReturn(resource);

        repository = new CsvProductRepository(resourceLoader);
    }

    @Test
    void testFindAll() {
        List<Product> all = repository.findAll();
        assertFalse(all.isEmpty(), "Product list should not be empty");
    }

    @Test
    void testFindById() {
        Optional<Product> productOpt = repository.findById("P-1001");
        assertTrue(productOpt.isPresent(), "Product P-1001 should be found");
        assertNotNull(productOpt.get().getName(), "Product name should not be null");
    }

    @Test
    void testFindBySellerId() {
        List<Product> sellerProducts = repository.findBySellerId("S-001");
        assertFalse(sellerProducts.isEmpty(), "Seller S-001 should have products");
    }

    @Test
    void testFindChildrenOf() {
        List<Product> children = repository.findChildrenOf("P-1000");
        assertFalse(children.isEmpty(), "P-1000 should have children");
    }

    @Test
    void testFindParentOfAlwaysEmpty() {
        assertTrue(repository.findParentOf("P-1000").isEmpty(), "Parent lookup currently returns empty");
    }
}
