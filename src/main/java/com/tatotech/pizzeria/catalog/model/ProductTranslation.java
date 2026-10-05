package com.tatotech.pizzeria.catalog.model;

import com.tatotech.pizzeria.shared.i18n.LocalizedText;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ProductTranslation(
        @Column(name = "locale", nullable = false) String locale,
        @Column(name = "name", nullable = false) String name,
        @Column(name = "description") String description
) implements LocalizedText {
}
