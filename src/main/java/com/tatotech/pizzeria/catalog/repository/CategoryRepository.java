package com.tatotech.pizzeria.catalog.repository;

import com.tatotech.pizzeria.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByRestaurantIdAndActiveTrueOrderBySortOrderAscIdAsc(Long restaurantId);
}
