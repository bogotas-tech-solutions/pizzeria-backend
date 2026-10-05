# Contexto para Claude Code — pizzeria-backend

## Proyecto
API REST del menú digital por QR de una pizzería en Brasil. Catálogo público bilingüe (pt-BR por defecto, es) y panel de administración protegido. El carrito vive en el frontend y los pedidos se envían por WhatsApp: **el backend no gestiona pedidos en el MVP**.

## Stack
Java 21, Spring Boot 4.1.x, Maven Wrapper, PostgreSQL 17, Flyway, Spring Security con JWT.

## Comandos
- Base de datos: `docker compose up -d`
- Ejecutar: `./mvnw spring-boot:run`
- Pruebas: `./mvnw test`

## Arquitectura (monolito modular)
Paquete raíz `com.tatotech.pizzeria`. Módulos: `catalog`, `restaurant`, `identity`, `shared`.
Dentro de cada módulo: `web` (controladores + DTOs), `service`, `repository`, `model`.

Reglas:
- Un módulo solo usa los services públicos de otro módulo. Nunca sus repositorios ni entidades.
- Los controladores nunca devuelven entidades JPA: siempre DTOs (usar `record`).
- Validación de entrada con Bean Validation (`@Valid`, `@NotBlank`, etc.).
- Errores manejados de forma centralizada en `shared` con `@RestControllerAdvice` y formato ProblemDetail.
- Rutas versionadas: `/api/v1/...`. Rutas de administración bajo `/api/v1/admin/...` (requieren rol ADMIN).
- Textos traducibles en tablas `*_translation` con columna `locale`; si falta un idioma, se devuelve pt-BR.
- Dinero con `BigDecimal` (nunca `double`). Moneda: BRL.

## Base de datos
- Todo cambio de esquema va en una migración Flyway nueva en `src/main/resources/db/migration`.
- **Nunca modificar una migración ya existente.**
- `spring.jpa.hibernate.ddl-auto=validate` (Hibernate nunca crea ni altera tablas).

## Cómo trabajar conmigo
- Soy estudiante de último semestre y quiero aprender: explica brevemente el porqué de cada decisión no obvia.
- Pregunta antes de agregar cualquier dependencia nueva al `pom.xml`.
- Prefiere soluciones simples. Evita abstracciones que el MVP no necesita.
- Cambios pequeños y enfocados en una historia de usuario a la vez.
- Escribe pruebas para la lógica de los services.
- Nunca escribas secretos en el código: usa variables de entorno.
