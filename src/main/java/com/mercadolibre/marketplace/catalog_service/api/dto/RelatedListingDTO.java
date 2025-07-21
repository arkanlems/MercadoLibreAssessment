package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RelatedListingDTO {
    private String listingId;
    private String title;
    private BigDecimal price;
    private boolean freeShipping;
}
