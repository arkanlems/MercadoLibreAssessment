package com.mercadolibre.marketplace.catalog_service.domain.repository;

import com.mercadolibre.marketplace.catalog_service.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(String productId);
    List<Product> findBySellerId(String sellerId);
    List<Product> findChildrenOf(String parentProductId);
    Optional<Product> findParentOf(String productId);
}
