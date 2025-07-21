package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.ReputationTier;
import com.mercadolibre.marketplace.catalog_service.domain.model.Seller;
import com.mercadolibre.marketplace.catalog_service.domain.repository.SellerRepository;
import com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model.SellerCsv;
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
public class CsvSellerRepository implements SellerRepository {

    private static final String DEFAULT_LOCATION = "classpath:data/sellers.csv";

    private final List<Seller> sellers = new ArrayList<>();
    private final Map<String, Seller> byId = new HashMap<>();
    private final ResourceLoader resourceLoader;

    public CsvSellerRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        loadSellers();
    }

    private void loadSellers() {
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);
        if (!resource.exists()) {
            log.error("Sellers CSV not found at {}", DEFAULT_LOCATION);
            return;
        }

        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            var csvToBean = new CsvToBeanBuilder<SellerCsv>(reader)
                    .withType(SellerCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<SellerCsv> raw = csvToBean.parse();
            log.info("Parsing {} rows from sellers.csv", raw.size());

            for (SellerCsv csv : raw) {
                Seller seller = mapCsvToDomain(csv);
                sellers.add(seller);
                byId.put(seller.getSellerId(), seller);
            }

            log.info("Loaded {} sellers ({} indexed)", sellers.size(), byId.size());
        } catch (Exception e) {
            log.error("Error loading sellers CSV", e);
        }
    }

    private Seller mapCsvToDomain(SellerCsv csv) {
        return Seller.builder()
                .sellerId(trim(csv.getSellerId()))
                .storeName(trim(csv.getStoreName()))
                .countryCode(trim(csv.getCountryCode()))
                .reputationTier(parseReputation(csv.getReputationTier()))
                .totalSales(parseLong(csv.getTotalSales(), 0L))
                .officialStore(parseBooleanFlexible(csv.getIsOfficialStore(), false))
                .createdAt(parseDate(csv.getCreatedAt()))
                .updatedAt(parseDate(csv.getUpdatedAt()))
                .build();
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static ReputationTier parseReputation(String raw) {
        if (raw == null) return ReputationTier.BRONCE;
        try {
            return ReputationTier.valueOf(raw.trim().toUpperCase());
        } catch (Exception e) {
            return ReputationTier.BRONCE;
        }
    }

    private static long parseLong(String raw, long def) {
        try {
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private static boolean parseBooleanFlexible(String raw, boolean def) {
        if (raw == null) return def;
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "true", "t" -> true;
            case "false", "f"-> false;
            default -> def;
        };
    }

    private static LocalDateTime parseDate(String raw) {
        try {
            return LocalDateTime.parse(raw.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Seller> findAll() {
        return Collections.unmodifiableList(sellers);
    }

    @Override
    public Optional<Seller> findById(String sellerId) {
        return Optional.ofNullable(byId.get(sellerId));
    }
}
