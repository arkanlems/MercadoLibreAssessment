package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ProductDiscountDTO {
    String discountId;
    BigDecimal percentage;  // porcentaje (0-100)
    LocalDateTime startAt;
    LocalDateTime endAt;
    Boolean active;
}
