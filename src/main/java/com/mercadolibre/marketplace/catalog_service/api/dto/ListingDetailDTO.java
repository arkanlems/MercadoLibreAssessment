package com.mercadolibre.marketplace.catalog_service.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ListingDetailDTO {
    ListingDTO listing;    // metadata comercial
    ProductDTO product;    // árbol padre + variantes con precio/desc
    SellerDTO seller;      // info de tienda / reputación
}