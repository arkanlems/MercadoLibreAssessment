package com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingCsv {

    @CsvBindByName(column = "listing_id")
    private String listingId;

    @CsvBindByName(column = "seller_id")
    private String sellerId;

    @CsvBindByName(column = "product_id")
    private String productId;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "discount_pct")
    private String discountPct;

    @CsvBindByName(column = "free_shipping")
    private String freeShipping;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "published_at")
    private String publishedAt;

    @CsvBindByName(column = "updated_at")
    private String updatedAt;
}
