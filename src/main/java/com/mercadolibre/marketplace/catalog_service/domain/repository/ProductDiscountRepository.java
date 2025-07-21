package com.mercadolibre.marketplace.catalog_service.domain.repository;

import com.mercadolibre.marketplace.catalog_service.domain.model.ProductDiscount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductDiscountRepository {

    Optional<ProductDiscount> findById(String discountId);
    List<ProductDiscount> findByProductId(String productId);
    List<ProductDiscount> findBySellerAndProduct(String sellerId, String productId);
    List<ProductDiscount> findActiveBySellerAndProduct(String sellerId, String productId, LocalDateTime at);

}