package com.tatotech.pizzeria.catalog.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Solo guardamos el ID, sin @ManyToOne hacia Restaurant: el módulo catalog
     * no debe depender de las entidades del módulo restaurant.
     */
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;

    @ElementCollection
    @CollectionTable(name = "category_translation", joinColumns = @JoinColumn(name = "category_id"))
    private Set<NameTranslation> translations = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Category() {
    }

    public Long getId() { return id; }
    public Long getRestaurantId() { return restaurantId; }
    public int getSortOrder() { return sortOrder; }
    public boolean isActive() { return active; }
    public Set<NameTranslation> getTranslations() { return translations; }
}
