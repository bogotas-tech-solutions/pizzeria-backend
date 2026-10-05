package com.tatotech.pizzeria.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(name = "whatsapp_phone", nullable = false)
    private String whatsappPhone;

    @Column(name = "default_locale", nullable = false)
    private String defaultLocale;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Requerido por JPA. */
    protected Restaurant() {
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getName() { return name; }
    public String getWhatsappPhone() { return whatsappPhone; }
    public String getDefaultLocale() { return defaultLocale; }
    public String getTimezone() { return timezone; }
    public boolean isActive() { return active; }
}
