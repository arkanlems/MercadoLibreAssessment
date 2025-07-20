package com.mercadolibre.marketplace.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ProductDiscount {

    private String discountId;
    private String sellerId; // puede ser null
    private String productId;

    private DiscountType discountType;
    private BigDecimal discountValue;
    private String currency;

    private boolean active;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isCurrentlyActive(LocalDateTime now) {
        if (!active) return false;
        if (startAt != null && now.isBefore(startAt)) return false;
        return endAt == null || !now.isAfter(endAt);
    }
}