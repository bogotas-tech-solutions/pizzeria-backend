package com.tatotech.pizzeria.restaurant.repository;

import com.tatotech.pizzeria.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findBySlugAndActiveTrue(String slug);
}
