package com.mercadolibre.marketplace.catalog_service.application.mapper;

import com.mercadolibre.marketplace.catalog_service.api.dto.*;
import com.mercadolibre.marketplace.catalog_service.domain.model.Listing;
import com.mercadolibre.marketplace.catalog_service.domain.model.Product;
import com.mercadolibre.marketplace.catalog_service.domain.model.ProductDiscount;
import com.mercadolibre.marketplace.catalog_service.domain.model.Seller;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class ListingDetailMapper {

    public ProductVariantDTO toVariantDTO(Product product, BigDecimal finalPrice, ProductDiscount discount) {
        return ProductVariantDTO.builder()
                .productId(product.getProductId())
                .skuCode(product.getSkuCode())
                .variantAttributes(product.getVariantAttributes())
                .basePrice(product.getBasePrice())
                .finalPrice(finalPrice)
                .currency(product.getCurrency())
                .stockQty(product.getStockQty())
                .active(product.isActive())
                .discount(discount != null ? toDiscountDTO(discount) : null)
                .build();
    }

    public ProductDiscountDTO toDiscountDTO(ProductDiscount discount) {
        return ProductDiscountDTO.builder()
                .discountId(discount.getDiscountId())
                .percentage(discount.getDiscountValue()) // siempre porcentaje
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .build();
    }

    public ListingDetailDTO toListingDetailDTO(
            Listing listing,
            Product parent,
            List<ProductVariantDTO> variants,
            ProductDiscount parentDiscount,
            Seller seller
    ) {
        return ListingDetailDTO.builder()
                .listing(toListingDTO(listing))
                .product(toProductDTO(parent, variants, parentDiscount))
                .seller(toSellerDTO(seller))
                .build();
    }

    private ListingDTO toListingDTO(Listing listing) {
        return ListingDTO.builder()
                .listingId(listing.getListingId())
                .sellerId(listing.getSellerId())
                .productId(listing.getProductId())
                .title(listing.getTitle())
                .freeShipping(listing.isFreeShipping())
                .status(listing.getStatus().name())
                .publishedAt(listing.getPublishedAt())
                .updatedAt(listing.getUpdatedAt())
                .build();
    }

    private ProductDTO toProductDTO(Product parent, List<ProductVariantDTO> variants, ProductDiscount discount) {
        return ProductDTO.builder()
                .productId(parent.getProductId())
                .name(parent.getName())
                .brand(parent.getBrand())
                .category(parent.getCategory())
                .longDesc(parent.getLongDesc())
                .imageUrls(parent.getImageUrls())
                .variants(variants)
                .discount(discount != null ? toDiscountDTO(discount) : null)
                .build();
    }

    private SellerDTO toSellerDTO(Seller seller) {
        return SellerDTO.builder()
                .sellerId(seller.getSellerId())
                .storeName(seller.getStoreName())
                .countryCode(seller.getCountryCode())
                .reputationTier(seller.getReputationTier().name())
                .officialStore(seller.isOfficialStore())
                .totalSales(seller.getTotalSales())
                .build();
    }
}


