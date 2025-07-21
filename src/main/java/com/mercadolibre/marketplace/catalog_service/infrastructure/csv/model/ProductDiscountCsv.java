package com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

/**
 * Raw binding to /data/product_discounts.csv.
 * All fields as String; conversion happens in the repository.
 */
@Getter
@Setter
public class ProductDiscountCsv {

    @CsvBindByName(column = "discount_id")
    private String discountId;

    @CsvBindByName(column = "seller_id")
    private String sellerId; // may be blank -> global discount

    @CsvBindByName(column = "product_id")
    private String productId;

    @CsvBindByName(column = "discount_type")
    private String discountType; // PERCENTAGE | AMOUNT

    @CsvBindByName(column = "discount_value")
    private String discountValue;

    @CsvBindByName(column = "currency")
    private String currency; // optional for percentage

    @CsvBindByName(column = "active")
    private String active;

    @CsvBindByName(column = "start_at")
    private String startAt;

    @CsvBindByName(column = "end_at")
    private String endAt;

    @CsvBindByName(column = "notes")
    private String notes;

    @CsvBindByName(column = "created_at")
    private String createdAt;

    @CsvBindByName(column = "updated_at")
    private String updatedAt;
}

