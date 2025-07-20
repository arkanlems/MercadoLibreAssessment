package com.mercadolibre.marketplace.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class Product {
    private String productId;
    private String sellerId;
    private String parentProductId; // null si es padre
    private String skuCode;
    private String name;
    private String brand;
    private String category;
    private Map<String, String> variantAttributes;
    private String shortDesc;
    private String longDesc;
    private List<String> imageUrls;
    private BigDecimal basePrice;
    private String currency;
    private int stockQty;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isParent() {
        return parentProductId == null || parentProductId.isBlank();
    }

}
