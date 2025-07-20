package com.mercadolibre.marketplace.catalog_service.domain.model;

import lombok.Getter;

@Getter
public enum ListingStatus {
    ACTIVA("Publicación activa y visible"),
    PAUSADA("Publicación pausada por el vendedor"),
    FINALIZADA("Publicación finalizada o sin stock"),
    PENDIENTE("Publicación pendiente de aprobación"),
    CANCELADA("Publicación cancelada");

    private final String description;

    ListingStatus(String description) {
        this.description = description;
    }

    public static ListingStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            return PENDIENTE;
        }
        return switch (value.trim().toUpperCase()) {
            case "ACTIVA" -> ACTIVA;
            case "PAUSADA" -> PAUSADA;
            case "FINALIZADA" -> FINALIZADA;
            case "CANCELADA" -> CANCELADA;
            default -> PENDIENTE;
        };
    }
}
