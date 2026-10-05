package com.tatotech.pizzeria.restaurant.service;

import com.tatotech.pizzeria.restaurant.model.Restaurant;
import com.tatotech.pizzeria.restaurant.repository.RestaurantRepository;
import com.tatotech.pizzeria.shared.i18n.SupportedLocale;
import com.tatotech.pizzeria.shared.web.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantInfo getActiveBySlug(String slug) {
        return restaurantRepository.findBySlugAndActiveTrue(slug)
                .map(RestaurantService::toInfo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un restaurante activo con slug '" + slug + "'"));
    }

    private static RestaurantInfo toInfo(Restaurant restaurant) {
        return new RestaurantInfo(
                restaurant.getId(),
                restaurant.getSlug(),
                restaurant.getName(),
                restaurant.getWhatsappPhone(),
                SupportedLocale.fromTag(restaurant.getDefaultLocale()).orElse(SupportedLocale.DEFAULT)
        );
    }
}
