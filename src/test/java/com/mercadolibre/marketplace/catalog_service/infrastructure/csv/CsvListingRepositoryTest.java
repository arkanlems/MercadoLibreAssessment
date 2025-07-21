package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.Listing;
import com.mercadolibre.marketplace.catalog_service.domain.model.ListingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CsvListingRepositoryTest {

    private CsvListingRepository repository;

    @BeforeEach
    void setUp() {
        ResourceLoader mockLoader = mock(ResourceLoader.class);
        Resource testResource = new ClassPathResource("data_test/listings.csv");
        when(mockLoader.getResource("classpath:data/listings.csv")).thenReturn(testResource);

        repository = new CsvListingRepository(mockLoader);
    }

    @Test
    @DisplayName("Carga todas las publicaciones")
    void testFindAll() {
        List<Listing> all = repository.findAll();
        assertThat(all).isNotNull().hasSize(5);
    }

    @Test
    @DisplayName("findById retorna publicacion conocida")
    void testFindById() {
        Optional<Listing> listing = repository.findById("L-9001");
        assertThat(listing).isPresent();
        assertThat(listing.get().getTitle()).contains("Moto Edge 50 Fusion");
    }

    @Test
    @DisplayName("freeShipping se parsea correctamente")
    void testFreeShippingParsed() {
        Listing l1 = repository.findById("L-9000").orElseThrow();
        assertThat(l1.isFreeShipping()).isTrue();

        Listing l2 = repository.findById("L-9002").orElseThrow();
        assertThat(l2.isFreeShipping()).isFalse();
    }

    @Test
    @DisplayName("status se parsea a enum")
    void testStatusParsed() {
        Listing l = repository.findById("L-9003").orElseThrow();
        assertThat(l.getStatus()).isEqualTo(ListingStatus.ACTIVA);
    }

    @Test
    @DisplayName("findBySellerId retorna publicaciones del vendedor")
    void testFindBySellerId() {
        List<Listing> listings = repository.findBySellerId("S-001");
        assertThat(listings).hasSize(1);
        assertThat(listings.get(0).getListingId()).isEqualTo("L-9000");
    }

    @Test
    @DisplayName("findByProductId retorna multiples sellers")
    void testFindByProductId() {
        List<Listing> listings = repository.findByProductId("P-1000");
        assertThat(listings).hasSize(2); // L-9000 y L-9004
        assertThat(listings).extracting(Listing::getListingId)
                .containsExactlyInAnyOrder("L-9000", "L-9004");
    }
}
