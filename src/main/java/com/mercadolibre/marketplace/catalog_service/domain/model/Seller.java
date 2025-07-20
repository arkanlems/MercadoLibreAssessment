package com.mercadolibre.marketplace.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class Seller {
    private String sellerId;
    private String storeName;
    private String countryCode;
    private ReputationTier reputationTier;
    private long totalSales;
    private boolean officialStore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}