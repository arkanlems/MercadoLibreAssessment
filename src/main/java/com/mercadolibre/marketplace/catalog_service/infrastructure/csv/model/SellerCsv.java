package com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerCsv {

    @CsvBindByName(column = "seller_id")
    private String sellerId;

    @CsvBindByName(column = "store_name")
    private String storeName;

    @CsvBindByName(column = "country_code")
    private String countryCode;

    @CsvBindByName(column = "reputation_tier")
    private String reputationTier;

    @CsvBindByName(column = "total_sales")
    private String totalSales;

    @CsvBindByName(column = "is_official_store")
    private String isOfficialStore;

    @CsvBindByName(column = "created_at")
    private String createdAt;

    @CsvBindByName(column = "updated_at")
    private String updatedAt;
}