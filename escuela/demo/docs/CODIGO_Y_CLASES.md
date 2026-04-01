# 🏗️ Arquitectura de Código y Clases Java

Este documento detalla la estructura técnica del proyecto **Academia Roller Speed**, diseñada bajo estándares profesionales de desarrollo con **Spring Boot**.

---

## 🏛️ 1. Estructura de Paquetes (Arquitectura N-Tier)
Hemos separado las responsabilidades del sistema en un modelo de **4 capas**, lo que garantiza la mantenibilidad y escalabilidad del código:

*   **`com.example.demo.model` (Entidades JPA):** Son clases Java (POJOs) que representan fielmente las tablas de la base de datos PostgreSQL. Usamos anotaciones de Hibernate (`@Entity`, `@Table`, `@Id`) para el mapeo objeto-relacional (ORM).
*   **`com.example.demo.repository` (Interfaces de Persistencia):** Aprovechamos **Spring Data JPA**. Al heredar de `JpaRepository`, Spring genera automáticamente las operaciones CRUD, evitando el uso de SQL manual propenso a errores.
*   **`com.example.demo.service` (Lógica de Negocio):** Esta capa encapsula las reglas académicas y de negocio de la escuela, manteniendo los controladores livianos y enfocados únicamente en el flujo web.
*   **`com.example.demo.controller` (Manejadores MVC):** Gestionan las peticiones HTTP, interactúan con la capa de servicios e inyectan datos en las vistas de **Thymeleaf**.

---

## 🛠️ 2. Principios de Diseño Aplicados (SOLID)

### Responsabilidad Única (SRP)
Cada paquete y clase tiene una misión clara: los repositorios solo hablan con la base de datos, los servicios ejecutan lógica y los controladores manejan el tráfico web.

### Inversión de Control (IoC) y DI
No instanciamos clases manualmente con `new` dentro de otras. Usamos **Inyección de Dependencias** mediante la anotación `@Autowired`, permitiendo que Spring gestione el ciclo de vida de los objetos (Beans).

### Segregación de Interfaces
Nuestros repositorios son interfaces limpias que exponen solo lo necesario para la persistencia de datos, siguiendo el contrato de Spring Data.

---

## 📑 3. Relación de Clases Principales

| Capa | Clases de Ejemplo |
| :--- | :--- |
| **Model** | `Estudiante`, `Instructor`, `Clase`, `Pago`, `Usuario`, `Noticia`, `Testimonio`, `Evento`. |
| **Repository** | `EstudianteRepository`, `ClaseRepository`, `NoticiaRepository`, `TestimonioRepository`. |
| **Service** | `EscuelaService`. |
| **Controller** | `EscuelaController`, `DashboardController`, `IndexController`, `AuthController`. |

---

> [!IMPORTANT]
> Esta estructura permite que el sistema sea fácil de testear (Unit Testing) y esté listo para una migración rápida a microservicios si el negocio lo requiere en el futuro.
