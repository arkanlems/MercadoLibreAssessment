package com.mercadolibre.marketplace.catalog_service.domain.repository;


import com.mercadolibre.marketplace.catalog_service.domain.model.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepository {
    List<Seller> findAll();
    Optional<Seller> findById(String sellerId);

}