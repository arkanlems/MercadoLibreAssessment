package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ListingDTO {
    String listingId;
    String sellerId;
    String productId;
    String title;
    Boolean freeShipping;
    String status;
    LocalDateTime publishedAt;
    LocalDateTime updatedAt;
}
