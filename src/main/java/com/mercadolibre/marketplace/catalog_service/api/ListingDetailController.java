package com.mercadolibre.marketplace.catalog_service.api;

import com.mercadolibre.marketplace.catalog_service.api.dto.ListingDetailDTO;
import com.mercadolibre.marketplace.catalog_service.application.ListingDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingDetailController {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("America/Bogota");

    private final ListingDetailService listingDetailService;

    @Operation(
            summary = "Obtiene el detalle completo de una publicación (listing)",
            description = """
                    Retorna la metadata del listing, el producto padre con sus variantes vendibles 
                    (incluyendo precio y descuento calculado por variante), y la información del vendedor.
                    
                    El parámetro opcional 'at' (ISO-8601) determina la fecha/hora a evaluar si hay descuentos activos.
                    Si no se envía, se usa la hora actual en America/Bogota.
                    """
    )
    @GetMapping("/{listingId}")
    public ResponseEntity<ListingDetailDTO> getListingDetail(
            @PathVariable String listingId,
            @Parameter(
                    description = "Fecha/hora (ISO-8601) para evaluar descuentos. Ej: 2025-07-10T12:00:00.",
                    example = "2025-07-10T12:00:00"
            )
            @RequestParam(name = "at", required = false) String atParam
    ) {
        final var at = resolveAt(atParam);
        try {
            ListingDetailDTO dto = listingDetailService.getListingDetail(listingId, at);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException notFound) {
            // Propagamos como 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFound.getMessage(), notFound);
        } catch (Exception ex) {
            // Errores inesperados
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving listing detail.", ex);
        }
    }

    /**
     * Convierte el parámetro de fecha/hora (string) a LocalDateTime en zona America/Bogota.
     * Si es null/blank, retorna ahora().
     */
    private LocalDateTime resolveAt(String raw) {
        if (raw == null || raw.isBlank()) {
            return LocalDateTime.now(DEFAULT_ZONE);
        }
        try {
            // Intentar parseo simple a LocalDateTime
            return LocalDateTime.parse(raw.trim());
        } catch (DateTimeParseException ignored) {
            // Intentar parseo a ZonedDateTime u OffsetDateTime
            try {
                return ZonedDateTime.parse(raw.trim()).withZoneSameInstant(DEFAULT_ZONE).toLocalDateTime();
            } catch (DateTimeParseException ignored2) {
                try {
                    return OffsetDateTime.parse(raw.trim()).atZoneSameInstant(DEFAULT_ZONE).toLocalDateTime();
                } catch (DateTimeParseException ex) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Formato de fecha inválido para parámetro 'at': " + raw,
                            ex
                    );
                }
            }
        }
    }
}
