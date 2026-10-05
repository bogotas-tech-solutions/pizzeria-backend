-- =====================================================================
-- Datos de prueba SOLO para desarrollo (perfil dev).
-- Migración repetible (R__): Flyway la vuelve a ejecutar cada vez que
-- este archivo cambia. Por eso empieza borrando todo: es idempotente.
--
-- ⚠️ Nunca activar el perfil dev contra la base de datos de producción.
--
-- Como TRUNCATE ... RESTART IDENTITY reinicia los contadores,
-- los IDs generados son predecibles (1, 2, 3…) y se usan abajo.
-- =====================================================================

TRUNCATE restaurant RESTART IDENTITY CASCADE;

-- ---------- Restaurante (id 1) ----------
INSERT INTO restaurant (slug, name, whatsapp_phone)
VALUES ('pizzaria-teste', 'Pizzaria Teste', '5511999998888');

-- Turnos que cruzan la medianoche (18:00 → 00:30)
INSERT INTO opening_hours (restaurant_id, day_of_week, opens_at, closes_at)
SELECT 1, d, '18:00', '00:30' FROM generate_series(2, 7) AS d;   -- martes a domingo

-- ---------- Categorías ----------
INSERT INTO category (restaurant_id, sort_order, active) VALUES
    (1, 1, TRUE),    -- 1 Pizzas salgadas
    (1, 2, TRUE),    -- 2 Pizzas doces
    (1, 3, TRUE),    -- 3 Bebidas
    (1, 4, FALSE);   -- 4 Sobremesas (INACTIVA → no debe aparecer)

INSERT INTO category_translation (category_id, locale, name) VALUES
    (1, 'pt-BR', 'Pizzas Salgadas'), (1, 'es', 'Pizzas Saladas'),
    (2, 'pt-BR', 'Pizzas Doces'),    (2, 'es', 'Pizzas Dulces'),
    (3, 'pt-BR', 'Bebidas'),         (3, 'es', 'Bebidas'),
    (4, 'pt-BR', 'Sobremesas'),      (4, 'es', 'Postres');

-- ---------- Productos ----------
INSERT INTO product (category_id, sort_order, available, active) VALUES
    (1, 1, TRUE,  TRUE),    -- 1 Calabresa
    (1, 2, TRUE,  TRUE),    -- 2 Margherita
    (1, 3, FALSE, TRUE),    -- 3 Portuguesa (AGOTADA → aparece con available=false)
    (1, 4, TRUE,  TRUE),    -- 4 Frango com Catupiry (SIN traducción al español)
    (1, 5, TRUE,  FALSE),   -- 5 Quatro Queijos (INACTIVO → no debe aparecer)
    (2, 1, TRUE,  TRUE),    -- 6 Chocolate com Morango
    (3, 1, TRUE,  TRUE),    -- 7 Coca-Cola 2L
    (3, 2, TRUE,  TRUE),    -- 8 Guaraná Antarctica lata
    (4, 1, TRUE,  TRUE);    -- 9 Pudim (categoría inactiva → no debe aparecer)

INSERT INTO product_translation (product_id, locale, name, description) VALUES
    (1, 'pt-BR', 'Calabresa', 'Molho de tomate, mussarela, calabresa fatiada e cebola'),
    (1, 'es',    'Calabresa', 'Salsa de tomate, mozzarella, salchicha calabresa en rodajas y cebolla'),
    (2, 'pt-BR', 'Margherita', 'Molho de tomate, mussarela, tomate e manjericão fresco'),
    (2, 'es',    'Margarita', 'Salsa de tomate, mozzarella, tomate y albahaca fresca'),
    (3, 'pt-BR', 'Portuguesa', 'Mussarela, presunto, ovo, cebola, ervilha e azeitona'),
    (3, 'es',    'Portuguesa', 'Mozzarella, jamón, huevo, cebolla, arvejas y aceitunas'),
    (4, 'pt-BR', 'Frango com Catupiry', 'Frango desfiado com catupiry original'),
    (5, 'pt-BR', 'Quatro Queijos', 'Mussarela, provolone, parmesão e gorgonzola'),
    (5, 'es',    'Cuatro Quesos', 'Mozzarella, provolone, parmesano y gorgonzola'),
    (6, 'pt-BR', 'Chocolate com Morango', 'Chocolate ao leite e morangos frescos'),
    (6, 'es',    'Chocolate con Fresas', 'Chocolate con leche y fresas frescas'),
    (7, 'pt-BR', 'Coca-Cola', NULL),
    (7, 'es',    'Coca-Cola', NULL),
    (8, 'pt-BR', 'Guaraná Antarctica', NULL),
    (8, 'es',    'Guaraná Antarctica', NULL),
    (9, 'pt-BR', 'Pudim', 'Pudim de leite condensado'),
    (9, 'es',    'Flan', 'Flan de leche condensada');

-- ---------- Variantes de pizzas: Pequena / Média / Grande ----------
INSERT INTO product_variant (product_id, price, sort_order)
SELECT pr.product_id, s.price, s.sort_order
FROM (VALUES
        (1, 34.90, 44.90, 54.90),
        (2, 32.90, 42.90, 52.90),
        (3, 36.90, 46.90, 56.90),
        (4, 36.90, 46.90, 56.90),
        (5, 38.90, 48.90, 58.90),
        (6, 39.90, 49.90, 59.90)
     ) AS pr(product_id, small, medium, large)
CROSS JOIN LATERAL (VALUES (1, pr.small), (2, pr.medium), (3, pr.large)) AS s(sort_order, price)
ORDER BY pr.product_id, s.sort_order;

INSERT INTO product_variant_translation (variant_id, locale, name)
SELECT v.id, t.locale, t.name
FROM product_variant v
JOIN (VALUES
        (1, 'pt-BR', 'Pequena'), (1, 'es', 'Pequeña'),
        (2, 'pt-BR', 'Média'),   (2, 'es', 'Mediana'),
        (3, 'pt-BR', 'Grande'),  (3, 'es', 'Grande')
     ) AS t(sort_order, locale, name) ON t.sort_order = v.sort_order
WHERE v.product_id BETWEEN 1 AND 6;

-- ---------- Variantes únicas: bebidas y postre ----------
INSERT INTO product_variant (product_id, price, sort_order) VALUES
    (7, 14.00, 1),
    (8,  6.50, 1),
    (9, 12.00, 1);

INSERT INTO product_variant_translation (variant_id, locale, name)
SELECT v.id, t.locale, t.name
FROM product_variant v
JOIN (VALUES
        (7, 'pt-BR', '2 litros'),    (7, 'es', '2 litros'),
        (8, 'pt-BR', 'Lata 350 ml'), (8, 'es', 'Lata 350 ml'),
        (9, 'pt-BR', 'Fatia'),       (9, 'es', 'Porción')
     ) AS t(product_id, locale, name) ON t.product_id = v.product_id;
