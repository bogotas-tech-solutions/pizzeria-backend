package com.tatotech.pizzeria.shared.i18n;

import java.util.Locale;
import java.util.Optional;

/**
 * Idiomas que soporta el sistema. pt-BR es el idioma por defecto y obligatorio.
 */
public enum SupportedLocale {

    PT_BR("pt-BR"),
    ES("es");

    public static final SupportedLocale DEFAULT = PT_BR;

    private final String tag;

    SupportedLocale(String tag) {
        this.tag = tag;
    }

    /** Código tal como se guarda en la base de datos y se expone en la API. */
    public String tag() {
        return tag;
    }

    /**
     * Interpreta un código de idioma de forma tolerante: "pt", "pt-BR", "PT_br" → PT_BR;
     * "es", "es-AR", "es-CO" → ES. Devuelve vacío si el idioma no está soportado.
     */
    public static Optional<SupportedLocale> fromTag(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        String language = value.trim().toLowerCase(Locale.ROOT).split("[-_]")[0];
        return switch (language) {
            case "pt" -> Optional.of(PT_BR);
            case "es" -> Optional.of(ES);
            default -> Optional.empty();
        };
    }
}
