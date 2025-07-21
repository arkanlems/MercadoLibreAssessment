package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.ProductDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvProductDiscountRepositoryTest {

    private CsvProductDiscountRepository repository;

    @BeforeEach
    void setUp() {
        ResourceLoader mockLoader = mock(ResourceLoader.class);
        Resource resource = new ClassPathResource("data_test/product_discounts.csv");
        when(mockLoader.getResource("classpath:data/product_discounts.csv")).thenReturn(resource);
        repository = new CsvProductDiscountRepository(mockLoader);
    }

    @Test
    @DisplayName("findById debe retornar el descuento correcto si existe")
    void testFindById() {
        Optional<ProductDiscount> d = repository.findById("D-1000");
        assertThat(d).isPresent();
        assertThat(d.get().getProductId()).isEqualTo("P-1000");
        assertThat(d.get().isActive()).isTrue();
    }

    @Test
    @DisplayName("findByProductId debe retornar todos los descuentos asociados a un producto")
    void testFindByProductId() {
        List<ProductDiscount> discounts = repository.findByProductId("P-1000");
        assertThat(discounts).hasSize(2); // S-001 y S-002
        assertThat(discounts).extracting(ProductDiscount::getDiscountId)
                .containsExactlyInAnyOrder("D-1000", "D-1001");
    }

    @Test
    @DisplayName("findBySellerAndProduct debe filtrar correctamente por sellerId y productId")
    void testFindBySellerAndProduct() {
        List<ProductDiscount> discounts = repository.findBySellerAndProduct("S-002", "P-1000");
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).getDiscountId()).isEqualTo("D-1001");
    }

    @Test
    @DisplayName("findActiveBySellerAndProduct debe devolver solo descuentos activos en la fecha dada")
    void testFindActiveBySellerAndProduct() {
        LocalDateTime date = LocalDateTime.parse("2025-07-10T12:00:00");
        List<ProductDiscount> activeDiscounts = repository.findActiveBySellerAndProduct("S-001", "P-1000", date);
        assertThat(activeDiscounts).hasSize(1);
        assertThat(activeDiscounts.get(0).getDiscountId()).isEqualTo("D-1000");
    }

    @Test
    @DisplayName("findActiveBySellerAndProduct debe devolver vacío si no hay descuentos activos en la fecha")
    void testFindActiveBySellerAndProductNoActive() {
        LocalDateTime date = LocalDateTime.parse("2026-01-01T00:00:00");
        List<ProductDiscount> activeDiscounts = repository.findActiveBySellerAndProduct("S-001", "P-1000", date);
        assertThat(activeDiscounts).isEmpty();
    }
}
