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
 * Tamaño o presentación de un producto (Pequena, Média, Grande, Lata…).
 * El precio vive aquí, nunca en Product.
 */
@Entity
@Table(name = "product_variant")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;

    @ElementCollection
    @CollectionTable(name = "product_variant_translation", joinColumns = @JoinColumn(name = "variant_id"))
    private Set<NameTranslation> translations = new HashSet<>();

    protected ProductVariant() {
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public BigDecimal getPrice() { return price; }
    public int getSortOrder() { return sortOrder; }
    public boolean isActive() { return active; }
    public Set<NameTranslation> getTranslations() { return translations; }
}
