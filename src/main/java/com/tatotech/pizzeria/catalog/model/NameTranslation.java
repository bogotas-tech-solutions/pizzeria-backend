package com.tatotech.pizzeria.catalog.model;

import com.tatotech.pizzeria.shared.i18n.LocalizedText;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Traducción de un nombre. Se reutiliza en categorías y variantes,
 * cada una con su propia tabla (category_translation, product_variant_translation).
 */
@Embeddable
public record NameTranslation(
        @Column(name = "locale", nullable = false) String locale,
        @Column(name = "name", nullable = false) String name
) implements LocalizedText {
}
