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
public class Listing {
    private String listingId;
    private String sellerId;
    private String productId; // Referencia al producto padre
    private String title;
    private BigDecimal price;
    private BigDecimal discountPct;
    private String installmentsText;
    private boolean freeShipping;
    private ListingStatus status;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
}
