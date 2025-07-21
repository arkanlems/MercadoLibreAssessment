package com.mercadolibre.marketplace.catalog_service.application;

import com.mercadolibre.marketplace.catalog_service.api.dto.ListingDetailDTO;
import com.mercadolibre.marketplace.catalog_service.api.dto.ProductVariantDTO;
import com.mercadolibre.marketplace.catalog_service.domain.model.Listing;
import com.mercadolibre.marketplace.catalog_service.domain.model.Product;
import com.mercadolibre.marketplace.catalog_service.domain.model.ProductDiscount;
import com.mercadolibre.marketplace.catalog_service.domain.model.Seller;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ListingRepository;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ProductDiscountRepository;
import com.mercadolibre.marketplace.catalog_service.domain.repository.ProductRepository;
import com.mercadolibre.marketplace.catalog_service.domain.repository.SellerRepository;
import com.mercadolibre.marketplace.catalog_service.application.mapper.ListingDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListingDetailService {

    private final ListingRepository listingRepository;
    private final ProductRepository productRepository;
    private final ProductDiscountRepository discountRepository;
    private final SellerRepository sellerRepository;
    private final ListingDetailMapper mapper;

    public ListingDetailDTO getListingDetail(String listingId, LocalDateTime at) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found: " + listingId));

        Product parent = productRepository.findById(listing.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + listing.getProductId()));

        List<Product> variants = productRepository.findChildrenOf(parent.getProductId());

        Optional<ProductDiscount> parentDiscount = discountRepository
                .findActiveBySellerAndProduct(parent.getSellerId(), parent.getProductId(), at)
                .stream().findFirst();

        List<ProductVariantDTO> variantDTOs = variants.stream()
                .map(variant -> {
                    ProductDiscount discount = discountRepository
                            .findActiveBySellerAndProduct(variant.getSellerId(), variant.getProductId(), at)
                            .stream().findFirst()
                            .orElse(parentDiscount.orElse(null));

                    BigDecimal finalPrice = calculateFinalPrice(variant.getBasePrice(), discount);

                    return mapper.toVariantDTO(variant, finalPrice, discount);
                })
                .toList();

        Seller seller = sellerRepository.findById(parent.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Seller not found: " + parent.getSellerId()));

        return mapper.toListingDetailDTO(listing, parent, variantDTOs, parentDiscount.orElse(null), seller);
    }

    private BigDecimal calculateFinalPrice(BigDecimal basePrice, ProductDiscount discount) {
        if (discount == null) return basePrice;
        BigDecimal percentage = discount.getDiscountValue().divide(BigDecimal.valueOf(100));
        return basePrice.subtract(basePrice.multiply(percentage));
    }
}
