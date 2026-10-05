package com.tatotech.pizzeria.shared.i18n;

import java.util.Collection;
import java.util.Optional;

/**
 * Elige la mejor traducción disponible para un idioma solicitado.
 * Orden de preferencia: idioma solicitado → pt-BR → cualquiera disponible.
 */
public final class LocalizedTexts {

    private LocalizedTexts() {
    }

    public static <T extends LocalizedText> Optional<T> resolve(Collection<T> texts, SupportedLocale requested) {
        return find(texts, requested)
                .or(() -> find(texts, SupportedLocale.DEFAULT))
                .or(() -> texts.stream().findFirst());
    }

    private static <T extends LocalizedText> Optional<T> find(Collection<T> texts, SupportedLocale locale) {
        return texts.stream()
                .filter(text -> locale.tag().equals(text.locale()))
                .findFirst();
    }
}
