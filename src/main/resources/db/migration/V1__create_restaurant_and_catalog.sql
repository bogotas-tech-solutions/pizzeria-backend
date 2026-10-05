-- =====================================================================
-- V1: Restaurante y catálogo
-- Idiomas soportados: 'pt-BR' (por defecto) y 'es'
-- =====================================================================

-- ---------------------------------------------------------------------
-- Restaurante
-- ---------------------------------------------------------------------
CREATE TABLE restaurant (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug            VARCHAR(60)  NOT NULL UNIQUE,          -- usado en la URL del QR
    name            VARCHAR(120) NOT NULL,
    whatsapp_phone  VARCHAR(15)  NOT NULL,                 -- solo dígitos, con código de país: 5511999998888
    default_locale  VARCHAR(5)   NOT NULL DEFAULT 'pt-BR',
    timezone        VARCHAR(50)  NOT NULL DEFAULT 'America/Sao_Paulo',
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT chk_restaurant_slug   CHECK (slug ~ '^[a-z0-9]+(-[a-z0-9]+)*$'),
    CONSTRAINT chk_restaurant_phone  CHECK (whatsapp_phone ~ '^[0-9]{10,15}$'),
    CONSTRAINT chk_restaurant_locale CHECK (default_locale IN ('pt-BR', 'es'))
);

-- Si closes_at < opens_at, el turno cruza la medianoche (ej. 18:00 → 01:00)
CREATE TABLE opening_hours (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    restaurant_id  BIGINT   NOT NULL REFERENCES restaurant (id) ON DELETE CASCADE,
    day_of_week    SMALLINT NOT NULL,                      -- ISO-8601: 1 = lunes … 7 = domingo
    opens_at       TIME     NOT NULL,
    closes_at      TIME     NOT NULL,
    CONSTRAINT chk_opening_day   CHECK (day_of_week BETWEEN 1 AND 7),
    CONSTRAINT chk_opening_range CHECK (opens_at <> closes_at)
);
CREATE INDEX idx_opening_hours_restaurant ON opening_hours (restaurant_id);

-- ---------------------------------------------------------------------
-- Categorías
-- ---------------------------------------------------------------------
CREATE TABLE category (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    restaurant_id  BIGINT      NOT NULL REFERENCES restaurant (id),
    sort_order     INTEGER     NOT NULL DEFAULT 0,
    active         BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_category_restaurant ON category (restaurant_id);

CREATE TABLE category_translation (
    category_id  BIGINT       NOT NULL REFERENCES category (id) ON DELETE CASCADE,
    locale       VARCHAR(5)   NOT NULL,
    name         VARCHAR(80)  NOT NULL,
    PRIMARY KEY (category_id, locale),
    CONSTRAINT chk_category_translation_locale CHECK (locale IN ('pt-BR', 'es'))
);

-- ---------------------------------------------------------------------
-- Productos
-- available = false → agotado hoy (se muestra, pero no se puede pedir)
-- active    = false → oculto / eliminado lógicamente (no se muestra)
-- ---------------------------------------------------------------------
CREATE TABLE product (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id  BIGINT       NOT NULL REFERENCES category (id),
    image_url    VARCHAR(500),
    sort_order   INTEGER      NOT NULL DEFAULT 0,
    available    BOOLEAN      NOT NULL DEFAULT TRUE,
    active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_product_category ON product (category_id);

CREATE TABLE product_translation (
    product_id   BIGINT        NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    locale       VARCHAR(5)    NOT NULL,
    name         VARCHAR(120)  NOT NULL,
    description  VARCHAR(500),
    PRIMARY KEY (product_id, locale),
    CONSTRAINT chk_product_translation_locale CHECK (locale IN ('pt-BR', 'es'))
);

-- ---------------------------------------------------------------------
-- Variantes: todo producto tiene al menos una.
-- Pizza → Pequena / Média / Grande. Bebida → una sola variante.
-- El precio vive SIEMPRE en la variante, nunca en el producto.
-- ---------------------------------------------------------------------
CREATE TABLE product_variant (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id  BIGINT         NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    price       NUMERIC(10, 2) NOT NULL,
    sort_order  INTEGER        NOT NULL DEFAULT 0,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_variant_price CHECK (price >= 0)
);
CREATE INDEX idx_product_variant_product ON product_variant (product_id);

CREATE TABLE product_variant_translation (
    variant_id  BIGINT      NOT NULL REFERENCES product_variant (id) ON DELETE CASCADE,
    locale      VARCHAR(5)  NOT NULL,
    name        VARCHAR(60) NOT NULL,
    PRIMARY KEY (variant_id, locale),
    CONSTRAINT chk_variant_translation_locale CHECK (locale IN ('pt-BR', 'es'))
);
