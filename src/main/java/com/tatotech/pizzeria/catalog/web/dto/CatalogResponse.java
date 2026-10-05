package com.tatotech.pizzeria.catalog.web.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Respuesta del catálogo público, ya traducida al idioma resuelto.
 */
public record CatalogResponse(
        RestaurantResponse restaurant,
        String locale,
        List<CategoryResponse> categories
) {

    public record RestaurantResponse(String name, String whatsappPhone) {
    }

    public record CategoryResponse(
            Long id,
            String name,
            List<OptionResponse> options,
            List<ProductResponse> products
    ) {
    }

    /** Opción de personalización (casilla). price = 0 → gratis. */
    public record OptionResponse(Long id, String name, BigDecimal price) {
    }

    public record ProductResponse(
            Long id,
            String name,
            String description,
            String imageUrl,
            boolean available,
            boolean featured,
            List<VariantResponse> variants
    ) {
    }

    public record VariantResponse(Long id, String name, BigDecimal price) {
    }
}
