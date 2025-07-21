package com.mercadolibre.marketplace.catalog_service.domain.repository;


import com.mercadolibre.marketplace.catalog_service.domain.model.Listing;
import com.mercadolibre.marketplace.catalog_service.domain.model.ListingStatus;

import java.util.List;
import java.util.Optional;

public interface ListingRepository {
    List<Listing> findAll();

    List<Listing> findByStatus(ListingStatus status);

    Optional<Listing> findById(String listingId);

    List<Listing> findBySellerId(String sellerId);

    List<Listing> findByProductId(String productId);

}