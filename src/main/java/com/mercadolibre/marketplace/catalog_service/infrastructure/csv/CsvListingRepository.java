package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.Listing;
import com.mercadolibre.marketplace.catalog_service.domain.model.ListingStatus;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ListingRepository;
import com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model.ListingCsv;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Repository
public class CsvListingRepository implements ListingRepository {

    private static final String DEFAULT_LOCATION = "classpath:data/listings.csv";

    private final List<Listing> listings = new ArrayList<>();
    private final Map<String, Listing> byId = new HashMap<>();
    private final ResourceLoader resourceLoader;

    public CsvListingRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        loadListings(); // <--- IMPORTANTE
    }

    private void loadListings() {
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);
        if (!resource.exists()) {
            log.error("Listings CSV not found at {}", DEFAULT_LOCATION);
            return;
        }

        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            var csvToBean = new CsvToBeanBuilder<ListingCsv>(reader)
                    .withType(ListingCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ListingCsv> raw = csvToBean.parse();
            log.info("Parsing {} rows from listings.csv", raw.size());

            for (ListingCsv csv : raw) {
                Listing listing = mapCsvToDomain(csv);
                listings.add(listing);
                byId.put(listing.getListingId(), listing);
            }

            log.info("Loaded {} listings ({} indexed)", listings.size(), byId.size());
        } catch (Exception e) {
            log.error("Error loading listings CSV", e);
        }
    }

    private Listing mapCsvToDomain(ListingCsv csv) {
        return Listing.builder()
                .listingId(csv.getListingId())
                .sellerId(csv.getSellerId())
                .productId(csv.getProductId())
                .title(csv.getTitle())
                .freeShipping(Boolean.parseBoolean(csv.getFreeShipping()))
                .status(parseStatus(csv.getStatus()))
                .publishedAt(parseDate(csv.getPublishedAt()))
                .updatedAt(parseDate(csv.getUpdatedAt()))
                .build();
    }

    private static ListingStatus parseStatus(String raw) {
        try {
            return ListingStatus.valueOf(raw);
        } catch (Exception e) {
            return ListingStatus.ACTIVA; // default
        }
    }

    private static LocalDateTime parseDate(String raw) {
        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Override
    public List<Listing> findAll() {
        return Collections.unmodifiableList(listings);
    }

    @Override
    public List<Listing> findByStatus(ListingStatus status) {
        return listings.stream()
                .filter(l -> l.getStatus() == status)
                .toList();
    }

    @Override
    public Optional<Listing> findById(String listingId) {
        return Optional.ofNullable(byId.get(listingId));
    }

    @Override
    public List<Listing> findBySellerId(String sellerId) {
        return listings.stream()
                .filter(l -> Objects.equals(l.getSellerId(), sellerId))
                .toList();
    }

    @Override
    public List<Listing> findByProductId(String productId) {
        return listings.stream()
                .filter(l -> Objects.equals(l.getProductId(), productId))
                .toList();
    }
}
