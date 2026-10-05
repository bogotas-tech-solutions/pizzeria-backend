package com.tatotech.pizzeria.shared.i18n;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LocalizedTextsTest {

    record Text(String locale, String value) implements LocalizedText {
    }

    private static final Text PT = new Text("pt-BR", "Calabresa");
    private static final Text ES = new Text("es", "Calabresa ES");

    @Test
    void devuelveElIdiomaSolicitadoSiExiste() {
        assertThat(LocalizedTexts.resolve(List.of(PT, ES), SupportedLocale.ES)).contains(ES);
    }

    @Test
    void usaPortuguesSiFaltaElIdiomaSolicitado() {
        assertThat(LocalizedTexts.resolve(List.of(PT), SupportedLocale.ES)).contains(PT);
    }

    @Test
    void usaCualquierTraduccionSiNoHayPortugues() {
        assertThat(LocalizedTexts.resolve(List.of(ES), SupportedLocale.PT_BR)).contains(ES);
    }

    @Test
    void devuelveVacioSiNoHayTraducciones() {
        assertThat(LocalizedTexts.resolve(List.<Text>of(), SupportedLocale.ES)).isEmpty();
    }
}
