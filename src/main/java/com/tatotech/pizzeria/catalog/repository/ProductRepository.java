package com.tatotech.pizzeria.catalog.repository;

import com.tatotech.pizzeria.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Productos que el público puede ver: activos y dentro de una categoría activa.
     * Incluye los agotados (available = false), porque se muestran en gris.
     */
    @Query("""
            select p from Product p
            where p.category.restaurantId = :restaurantId
              and p.active = true
              and p.category.active = true
            order by p.sortOrder asc, p.id asc
            """)
    List<Product> findVisibleByRestaurant(@Param("restaurantId") Long restaurantId);
}
