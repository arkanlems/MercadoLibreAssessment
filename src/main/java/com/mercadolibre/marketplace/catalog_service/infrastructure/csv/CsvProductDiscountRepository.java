package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.DiscountType;
import com.mercadolibre.marketplace.catalog_service.domain.model.ProductDiscount;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ProductDiscountRepository;
import com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model.ProductDiscountCsv;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * CSV-backed implementation of ProductDiscountRepository.
 * Loads once at construction; read-only.
 */
@Slf4j
@Repository
public class CsvProductDiscountRepository implements ProductDiscountRepository {

    private static final String DEFAULT_LOCATION = "classpath:data/product_discounts.csv";

    private final List<ProductDiscount> discounts = new ArrayList<>();
    private final Map<String, ProductDiscount> byId = new HashMap<>();
    private final ResourceLoader resourceLoader;

    public CsvProductDiscountRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        loadDiscounts();
    }

    private void loadDiscounts() {
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);
        if (!resource.exists()) {
            log.error("Product discounts CSV not found at {}", DEFAULT_LOCATION);
            return;
        }

        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            var csvToBean = new CsvToBeanBuilder<ProductDiscountCsv>(reader)
                    .withType(ProductDiscountCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ProductDiscountCsv> raw = csvToBean.parse();
            log.info("Parsing {} rows from product_discounts.csv", raw.size());

            for (ProductDiscountCsv csv : raw) {
                ProductDiscount discount = mapCsvToDomain(csv);
                discounts.add(discount);
                byId.put(discount.getDiscountId(), discount);
            }

            log.info("Loaded {} product discounts ({} indexed)", discounts.size(), byId.size());
        } catch (Exception e) {
            log.error("Error loading product_discounts CSV", e);
        }
    }

    private ProductDiscount mapCsvToDomain(ProductDiscountCsv csv) {
        return ProductDiscount.builder()
                .discountId(trim(csv.getDiscountId()))
                .sellerId(blankToNull(csv.getSellerId()))
                .productId(trim(csv.getProductId()))
                .discountType(parseType(csv.getDiscountType()))
                .discountValue(parseBigDecimal(csv.getDiscountValue()))
                .currency(blankToNull(csv.getCurrency()))
                .active(parseBoolean(csv.getActive()))
                .startAt(parseDate(csv.getStartAt()))
                .endAt(parseDate(csv.getEndAt()))
                .notes(trim(csv.getNotes()))
                .createdAt(parseDate(csv.getCreatedAt()))
                .updatedAt(parseDate(csv.getUpdatedAt()))
                .build();
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
    private static String blankToNull(String s) {
        s = trim(s);
        return (s == null || s.isEmpty()) ? null : s;
    }
    private static DiscountType parseType(String raw) {
        raw = trim(raw);
        if (raw == null) return DiscountType.PERCENTAGE;
        try {
            return DiscountType.valueOf(raw);
        } catch (IllegalArgumentException ex) {
            return DiscountType.PERCENTAGE;
        }
    }
    private static BigDecimal parseBigDecimal(String raw) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    private static boolean parseBoolean(String raw) {
        raw = trim(raw);
        return raw == null || raw.isEmpty() || Boolean.parseBoolean(raw);
    }
    private static LocalDateTime parseDate(String raw) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return null;
        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    @Override
    public Optional<ProductDiscount> findById(String discountId) {
        return Optional.ofNullable(byId.get(discountId));
    }

    @Override
    public List<ProductDiscount> findByProductId(String productId) {
        return discounts.stream()
                .filter(d -> Objects.equals(d.getProductId(), productId))
                .toList();
    }

    @Override
    public List<ProductDiscount> findBySellerAndProduct(String sellerId, String productId) {
        return discounts.stream()
                .filter(d -> Objects.equals(d.getProductId(), productId))
                .filter(d -> d.getSellerId() == null || Objects.equals(d.getSellerId(), sellerId))
                .toList();
    }

    @Override
    public List<ProductDiscount> findActiveBySellerAndProduct(String sellerId, String productId, LocalDateTime at) {
        return findBySellerAndProduct(sellerId, productId).stream()
                .filter(d -> isActive(d, at))
                .toList();
    }

    private static boolean isActive(ProductDiscount d, LocalDateTime at) {
        if (!d.isActive()) return false;
        return within(d.getStartAt(), d.getEndAt(), at);
    }

    private static boolean within(LocalDateTime start, LocalDateTime end, LocalDateTime at) {
        if (start != null && at.isBefore(start)) return false;
        return end == null || !at.isAfter(end);
    }
}
