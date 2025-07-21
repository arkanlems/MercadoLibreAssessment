package com.mercadolibre.marketplace.catalog_service.infrastructure.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCsv {

    @CsvBindByName(column = "product_id")
    private String productId;

    @CsvBindByName(column = "seller_id")
    private String sellerId;

    @CsvBindByName(column = "parent_product_id")
    private String parentProductId;

    @CsvBindByName(column = "sku_code")
    private String skuCode;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "brand")
    private String brand;

    @CsvBindByName(column = "category")
    private String category;

    @CsvBindByName(column = "variant_attrs")
    private String variantAttributes;

    @CsvBindByName(column = "short_desc")
    private String shortDescription;

    @CsvBindByName(column = "long_desc")
    private String longDescription;

    @CsvBindByName(column = "image_urls")
    private String imageUrls;

    @CsvBindByName(column = "base_price")
    private String basePrice;

    @CsvBindByName(column = "currency")
    private String currency;

    @CsvBindByName(column = "stock_qty")
    private String stockQty;

    @CsvBindByName(column = "active")
    private String active;

    @CsvBindByName(column = "created_at")
    private String createdAt;

    @CsvBindByName(column = "updated_at")
    private String updatedAt;
}
