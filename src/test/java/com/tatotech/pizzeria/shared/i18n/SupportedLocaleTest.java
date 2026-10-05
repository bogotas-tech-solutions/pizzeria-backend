package com.tatotech.pizzeria.shared.i18n;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedLocaleTest {

    @ParameterizedTest
    @ValueSource(strings = {"pt", "pt-BR", "PT-br", "pt_BR", " pt-BR "})
    void reconoceVariantesDePortugues(String tag) {
        assertThat(SupportedLocale.fromTag(tag)).contains(SupportedLocale.PT_BR);
    }

    @ParameterizedTest
    @ValueSource(strings = {"es", "ES", "es-AR", "es-CO", "es_CO"})
    void reconoceVariantesDeEspanol(String tag) {
        assertThat(SupportedLocale.fromTag(tag)).contains(SupportedLocale.ES);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"en", "fr-FR", "   "})
    void devuelveVacioParaIdiomasNoSoportados(String tag) {
        assertThat(SupportedLocale.fromTag(tag)).isEmpty();
    }

    @Test
    void elIdiomaPorDefectoEsPortugues() {
        assertThat(SupportedLocale.DEFAULT).isEqualTo(SupportedLocale.PT_BR);
        assertThat(SupportedLocale.DEFAULT.tag()).isEqualTo("pt-BR");
    }
}
