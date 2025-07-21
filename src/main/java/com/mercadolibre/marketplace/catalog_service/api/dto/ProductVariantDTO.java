package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class ProductVariantDTO {
    String productId;
    String skuCode;
    Map<String, String> variantAttributes;
    BigDecimal basePrice;
    ProductDiscountDTO discount;
    BigDecimal finalPrice;
    String currency;
    Integer stockQty;
    Boolean active;
    
}
