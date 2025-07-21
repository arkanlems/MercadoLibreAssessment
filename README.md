# Catalog Service (CSV Prototype)

Este proyecto implementa un servicio de catálogo para un marketplace. Toda la información (productos, listados, descuentos, vendedores) se carga desde archivos CSV en la carpeta `resources/data`.  
La arquitectura sigue **DDD (Domain-Driven Design)** con **repositorios en CSV**, y expone un API REST con Spring Boot.

---

## **Tecnologías utilizadas**
- **Java 17**
- **Spring Boot 3.x**
- **Lombok**
- **OpenCSV** (carga de datos CSV)
- **JUnit 5 + Mockito** (tests)
- **Docker** (para integración en contenedores)
- **Maven**

---

## **Estructura principal**
- **Domain**: Contiene las entidades (`Product`, `Listing`, `Seller`, `ProductDiscount`) y sus repositorios (`ProductRepository`, etc.).
- **Infrastructure**: Implementaciones CSV de los repositorios (por ejemplo, `CsvProductRepository`).
- **API**: Controladores REST y DTOs para exponer los datos al frontend.
- **Tests**: Cubren repositorios, servicios y controladores.

---

## **Endpoints principales**

| Método | Endpoint                            | Descripción                                        |
|--------|-------------------------------------|----------------------------------------------------|
| GET    | `/api/listings/{listingId}`         | Obtiene detalle de una publicación (producto + vendedor). |
| GET    | `/api/products/{productId}`         | Obtiene información de un producto específico.     |
| GET    | `/api/sellers/{sellerId}`           | Obtiene información de un vendedor.                |
| GET    | `/api/listings?sellerId=S-001`      | Lista las publicaciones de un vendedor.            |

---

