package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.ReputationTier;
import com.mercadolibre.marketplace.catalog_service.domain.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CsvSellerRepository")
class CsvSellerRepositoryTest {

    private CsvSellerRepository repository;

    @BeforeEach
    void setUp() {
        ResourceLoader mockLoader = mock(ResourceLoader.class);
        Resource resource = new ClassPathResource("data_test/sellers.csv");
        when(mockLoader.getResource("classpath:data/sellers.csv")).thenReturn(resource);
        repository = new CsvSellerRepository(mockLoader);
    }

    @Test
    @DisplayName("Debe cargar todos los sellers")
    void testFindAll() {
        List<Seller> all = repository.findAll();
        assertThat(all).isNotNull().hasSize(5);
    }

    @Test
    @DisplayName("Debe encontrar un seller por ID")
    void testFindById() {
        Optional<Seller> s = repository.findById("S-001");
        assertThat(s).isPresent();
        Seller seller = s.get();
        assertThat(seller.getStoreName()).isEqualTo("Samsung Oficial");
        assertThat(seller.getCountryCode()).isEqualTo("UY");
    }

    @Test
    @DisplayName("Debe parsear ReputationTier correctamente")
    void testReputationTierParsed() {
        Seller s = repository.findById("S-002").orElseThrow();
        assertThat(s.getReputationTier()).isEqualTo(ReputationTier.ORO);
    }

    @Test
    @DisplayName("Debe parsear officialStore correctamente")
    void testOfficialStoreParsed() {
        Seller s1 = repository.findById("S-001").orElseThrow();
        Seller s2 = repository.findById("S-002").orElseThrow();
        assertThat(s1.isOfficialStore()).isTrue();   // CSV: true
        assertThat(s2.isOfficialStore()).isFalse();  // CSV: false
    }

    @Test
    @DisplayName("Debe parsear totalSales como long")
    void testTotalSalesParsed() {
        Seller s = repository.findById("S-003").orElseThrow();
        assertThat(s.getTotalSales()).isEqualTo(3200L);
    }

    @Test
    @DisplayName("findById debe devolver vacío si no existe")
    void testFindByIdNotFound() {
        assertThat(repository.findById("S-999")).isEmpty();
    }
}
