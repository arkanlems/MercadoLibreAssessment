package com.mercadolibre.marketplace.catalog_service.infrastructure.csv;

import com.mercadolibre.marketplace.catalog_service.domain.model.Product;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ProductRepository;
import com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model.ProductCsv;
import com.opencsv.bean.CsvToBean;
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

@Slf4j
@Repository
public class CsvProductRepository implements ProductRepository {

    private static final String DEFAULT_LOCATION = "classpath:data/products.csv";

    private final List<Product> products = new ArrayList<>();
    private final Map<String, Product> byId = new HashMap<>();
    private final ResourceLoader resourceLoader;

    public CsvProductRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        loadProducts();
    }

    private void loadProducts() {
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);
        if (!resource.exists()) {
            log.error("Products CSV not found at {}", DEFAULT_LOCATION);
            return;
        }

        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {

            CsvToBean<ProductCsv> csvToBean = new CsvToBeanBuilder<ProductCsv>(reader)
                    .withType(ProductCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')  // adjust if needed
                    .build();

            List<ProductCsv> raw = csvToBean.parse();
            log.info("Parsing {} rows from products.csv", raw.size());

            for (ProductCsv csv : raw) {
                Product product = mapCsvToDomain(csv);
                products.add(product);
                byId.put(product.getProductId(), product);
            }

            log.info("Loaded {} products ({} indexed)", products.size(), byId.size());

        } catch (Exception e) {
            log.error("Error loading products CSV", e);
        }
    }

    private Product mapCsvToDomain(ProductCsv csv) {
        return Product.builder()
                .productId(trim(csv.getProductId()))
                .sellerId(trim(csv.getSellerId()))
                .parentProductId(blankToNull(csv.getParentProductId()))
                .skuCode(trim(csv.getSkuCode()))
                .name(trim(csv.getName()))
                .brand(trim(csv.getBrand()))
                .category(trim(csv.getCategory()))
                .variantAttributes(parseVariantAttrs(csv.getVariantAttributes()))
                .shortDesc(trim(csv.getShortDescription()))
                .longDesc(trim(csv.getLongDescription()))
                .imageUrls(parseList(csv.getImageUrls(), "\\|"))
                .basePrice(parseBigDecimal(csv.getBasePrice()))
                .currency(trim(csv.getCurrency()))
                .stockQty(parseInt(csv.getStockQty(), 0)) // padres quedan en 0
                .active(parseBoolean(csv.getActive(), true))
                .createdAt(parseDate(csv.getCreatedAt()))
                .updatedAt(parseDate(csv.getUpdatedAt()))
                .build();
    }

    // ---------- Parsing helpers ----------

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String blankToNull(String s) {
        s = trim(s);
        return (s == null || s.isEmpty()) ? null : s;
    }

    private static List<String> parseList(String raw, String regexSep) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return List.of();
        return Arrays.stream(raw.split(regexSep))
                .map(String::trim)
                .filter(v -> !v.isEmpty())
                .toList();
    }

    /**
     * variant_attrs like: "color=Azul oscuro|ram=8GB|rom=256GB"
     */
    private static Map<String, String> parseVariantAttrs(String raw) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return Map.of();
        Map<String, String> map = new LinkedHashMap<>();
        String[] pairs = raw.split("\\|");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            } else if (kv.length == 1 && !kv[0].isBlank()) {
                map.put(kv[0].trim(), "");
            }
        }
        return map;
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

    private static int parseInt(String raw, int def) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return def;
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static boolean parseBoolean(String raw, boolean def) {
        raw = trim(raw);
        if (raw == null || raw.isEmpty()) return def;
        return Boolean.parseBoolean(raw);
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

    // ---------- ProductRepository methods ----------

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(byId.get(productId));
    }

    @Override
    public List<Product> findBySellerId(String sellerId) {
        return products.stream()
                .filter(p -> Objects.equals(p.getSellerId(), sellerId))
                .toList();
    }

    @Override
    public List<Product> findChildrenOf(String parentProductId) {
        return products.stream()
                .filter(p -> Objects.equals(p.getParentProductId(), parentProductId))
                .toList();
    }

    @Override
    public Optional<Product> findParentOf(String productId) {
        return Optional.empty();
    }
}
