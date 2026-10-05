# 🍕 Pizzeria Digital - Backend API

Esta es la API principal para el sistema de menú digital de **[NOMBRE DE LA PIZZERIA]**. Está diseñada bajo una arquitectura de **Monolito Modular**, asegurando una clara separación de dominios (Catálogo, Restaurante, Pedidos) para mantener los costos de infraestructura bajos en su etapa inicial, pero con la capacidad de escalar a microservicios en el futuro.

## 🚀 Tecnologías Principales

* **Java 17+**
* **Spring Boot 3** (Web, Data JPA, Validation)
* **Maven** (Gestión de dependencias)
* **PostgreSQL** (Base de datos relacional)
* **Docker & Docker Compose** (Contenerización del entorno local)

## 🏗️ Estructura del Proyecto (Monolito Modular)

El código está organizado por dominios de negocio, no por capas técnicas:
├── src/main/java/com/pizzeria/
│ ├── catalog/ (Gestión de categorías y productos)
│ ├── orders/ (Armado y validación del carrito)
│ └── core/ (Configuraciones globales, seguridad, excepciones)


## 🛠️ Configuración y Despliegue Local

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-org/pizzeria-backend.git](https://github.com/tu-org/pizzeria-backend.git)
   cd pizzeria-backend
