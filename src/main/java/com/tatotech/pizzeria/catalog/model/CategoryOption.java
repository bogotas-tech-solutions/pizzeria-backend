package com.tatotech.pizzeria.catalog.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Opción que el cliente puede marcar al pedir un producto de la categoría
 * (ej. cebola, orégano). Selección múltiple. price = 0 significa gratis.
 */
@Entity
@Table(name = "category_option")
public class CategoryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;

    @ElementCollection
    @CollectionTable(name = "category_option_translation", joinColumns = @JoinColumn(name = "option_id"))
    private Set<NameTranslation> translations = new HashSet<>();

    protected CategoryOption() {
    }

    public Long getId() { return id; }
    public Category getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getSortOrder() { return sortOrder; }
    public boolean isActive() { return active; }
    public Set<NameTranslation> getTranslations() { return translations; }
}
