package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ProductDiscountDTO {
    String discountId;
    BigDecimal percentage;
    BigDecimal amount;
    LocalDateTime startAt;
    LocalDateTime endAt;
    Boolean active;
}
