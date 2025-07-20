package com.mercadolibre.marketplace.catalog_service.domain.model;

import lombok.Getter;

@Getter
public enum ReputationTier {
    PLATINO("Platinum"),
    ORO("Gold"),
    PLATA("Silver"),
    BRONCE("Bronze"),
    SIN_CLASIFICAR("Unranked");

    private final String description;

    ReputationTier(String description) {
        this.description = description;
    }

    public static ReputationTier fromString(String value) {
        if (value == null || value.isBlank()) {
            return SIN_CLASIFICAR;
        }
        return switch (value.trim().toUpperCase()) {
            case "PLATINO" -> PLATINO;
            case "ORO" -> ORO;
            case "PLATA" -> PLATA;
            case "BRONCE" -> BRONCE;
            default -> SIN_CLASIFICAR;
        };
    }
}
