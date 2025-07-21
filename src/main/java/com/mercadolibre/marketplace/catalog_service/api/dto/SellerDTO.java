package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SellerDTO {
    String sellerId;
    String storeName;
    String countryCode;
    String reputationTier;
    long totalSales;
    boolean officialStore;
}
