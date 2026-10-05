package com.tatotech.pizzeria.restaurant.service;

import com.tatotech.pizzeria.shared.i18n.SupportedLocale;

/**
 * Vista pública del restaurante para otros módulos.
 * Los demás módulos usan este record, nunca la entidad Restaurant.
 */
public record RestaurantInfo(
        Long id,
        String slug,
        String name,
        String whatsappPhone,
        SupportedLocale defaultLocale
) {
}
