-- =====================================================================
-- V2: Opciones de personalización por categoría y productos destacados
-- Motivo: el menú real de Dejoma permite elegir cebolla y/u orégano en
-- las pizzas saladas, y destaca una pizza como "Exclusividade Dejoma".
-- =====================================================================

-- Producto destacado (especialidad de la casa)
ALTER TABLE product
    ADD COLUMN featured BOOLEAN NOT NULL DEFAULT FALSE;

-- Opciones que el cliente puede marcar al pedir (casillas, selección múltiple).
-- Viven en la categoría porque aplican igual a todos sus productos.
-- price = 0 → opción gratuita. Permite en el futuro extras con costo (ej. borde relleno).
CREATE TABLE category_option (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id  BIGINT         NOT NULL REFERENCES category (id) ON DELETE CASCADE,
    price        NUMERIC(10, 2) NOT NULL DEFAULT 0,
    sort_order   INTEGER        NOT NULL DEFAULT 0,
    active       BOOLEAN        NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_option_price CHECK (price >= 0)
);
CREATE INDEX idx_category_option_category ON category_option (category_id);

CREATE TABLE category_option_translation (
    option_id  BIGINT      NOT NULL REFERENCES category_option (id) ON DELETE CASCADE,
    locale     VARCHAR(5)  NOT NULL,
    name       VARCHAR(60) NOT NULL,
    PRIMARY KEY (option_id, locale),
    CONSTRAINT chk_option_translation_locale CHECK (locale IN ('pt-BR', 'es'))
);
