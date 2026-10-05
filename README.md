# 🍕 Pizzeria Digital — Backend API

API REST del menú digital por QR de **[NOMBRE DE LA PIZZERÍA]**. Expone el catálogo público (en portugués y español) y un panel de administración protegido para que el dueño gestione productos, precios y fotos.

> Frontend: [pizzeria-frontend](https://github.com/tato-tech-solutions/pizzeria-frontend)

## 🧭 Alcance del MVP

| Incluido | Fuera del MVP (plan "Delivery Pro") |
|---|---|
| Catálogo público bilingüe (pt-BR / es) | Cuentas de clientes |
| Login de administrador (JWT) | Pagos en línea |
| CRUD de categorías, productos, variantes y fotos | Seguimiento de pedidos |
| Datos del restaurante (WhatsApp, horarios) | App móvil (la API ya queda lista para ella) |

El carrito vive en el navegador y el pedido se envía por WhatsApp, por lo que **el backend no procesa pedidos en el MVP**.

## 🚀 Tecnologías

| Herramienta | Versión |
|---|---|
| Java | 21 (LTS) |
| Spring Boot | 4.1.x (Web MVC, Data JPA, Validation, Security, Actuator) |
| Maven | vía Maven Wrapper (`mvnw`), no requiere instalación |
| PostgreSQL | 17 |
| Flyway | migraciones versionadas del esquema |
| Docker Compose | base de datos local |

## 🏗️ Arquitectura: Monolito Modular

El código se organiza por **dominio de negocio**, no por capa técnica:

```
src/main/java/com/tatotech/pizzeria/
├── catalog/       Categorías, productos, variantes de tamaño y traducciones
├── restaurant/    Datos del local: nombre, WhatsApp, horarios, idiomas
├── identity/      Usuarios administradores, login y emisión de JWT
└── shared/        Configuración global, seguridad, manejo de errores
```

Cada módulo contiene sus propios subpaquetes `web` (controladores y DTOs), `service`, `repository` y `model`.

**Regla de dependencias:** un módulo solo puede usar los *services* públicos de otro módulo. Nunca accede directamente a sus repositorios ni entidades. Esto es lo que permite separar un módulo en un servicio independiente en el futuro.

### Internacionalización
Los textos traducibles del catálogo se guardan en tablas de traducción (`product_translation`, `category_translation`) con una columna `locale` (`pt-BR`, `es`). El idioma por defecto es **pt-BR**: si falta la traducción solicitada, la API devuelve el texto en portugués.

```
GET /api/v1/catalog?lang=es
```

## 🛠️ Configuración local

### Requisitos previos
- JDK 21
- Docker Desktop
- Git

### Pasos

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tato-tech-solutions/pizzeria-backend.git
   cd pizzeria-backend
   ```

2. **Crear el archivo de variables de entorno**
   ```bash
   cp .env.example .env
   ```
   Edita `.env` con tus valores. Este archivo **nunca** se sube al repositorio.

3. **Levantar PostgreSQL**
   ```bash
   docker compose up -d
   ```

4. **Ejecutar la aplicación**
   ```bash
   ./mvnw spring-boot:run        # macOS / Linux / Git Bash
   mvnw.cmd spring-boot:run      # Windows (CMD / PowerShell)
   ```
   Flyway aplica las migraciones automáticamente al arrancar.

5. **Verificar**
   ```
   http://localhost:8080/actuator/health   →  {"status":"UP"}
   ```

### Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `POSTGRES_DB` | Nombre de la base de datos | `pizzeria` |
| `POSTGRES_USER` | Usuario de la base | `pizzeria` |
| `POSTGRES_PASSWORD` | Contraseña de la base | `cambiar-esto` |
| `DB_URL` | URL JDBC | `jdbc:postgresql://localhost:5432/pizzeria` |
| `JWT_SECRET` | Clave para firmar tokens (mín. 32 caracteres) | *(generar una aleatoria)* |
| `CORS_ALLOWED_ORIGINS` | Origen del frontend | `http://localhost:5173` |

## 🧪 Pruebas

```bash
./mvnw test
```

## 🌿 Flujo de trabajo

- **Ramas:** `main` siempre desplegable. Trabajo en `feature/US-<id>-descripcion-corta` (ej. `feature/US-12-crud-productos`) y se integra mediante Pull Request.
- **Commits:** [Conventional Commits](https://www.conventionalcommits.org/es/) y referencia al work item de Azure DevOps con `AB#<id>`:
  ```
  feat(catalog): agregar endpoint de productos por categoría AB#12
  fix(identity): corregir expiración del token AB#27
  ```
- **Migraciones:** una migración de Flyway ya aplicada **nunca se edita**. Cualquier cambio de esquema va en una nueva (`V3__agregar_columna_x.sql`).

## 👥 Equipo

- **Juan Daniel** — Desarrollo
- **Santiago (Tato)** — Producto, diseño y relación con el cliente

---
© Tato Tech Solutions. Todos los derechos reservados.
