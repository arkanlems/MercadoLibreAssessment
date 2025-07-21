package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class ProductDTO {
    String productId;
    String sellerId;
    String skuCode;
    String name;
    String brand;
    String category;
    String shortDesc;
    String longDesc;
    List<String> imageUrls;
    Map<String, String> variantAttributes;
    ProductDiscountDTO discount;
    List<ProductVariantDTO> variants;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
