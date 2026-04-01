# Requerimientos No Funcionales ⚙️

Este documento describe las capacidades, estándares y atributos de calidad que definen la robustez técnica del sistema **Academia Roller Speed**.

---

## 🏗️ 1. Arquitectura Monolítica
El sistema adopta una arquitectura de monolito modular, lo que facilita el despliegue centralizado y reduce la latencia en las comunicaciones entre componentes.
-   **Core:** Aplicación basada en **Spring Boot** y **Thymeleaf**.
-   **Patrón de Diseño:** Implementación estricta del patrón **MVC (Model-View-Controller)** para separar la lógica de negocio, el modelo de datos y la interfaz de usuario.

## 🔒 2. Seguridad y Control de Accesos
La integridad y confidencialidad de la información son pilares del sistema.
-   **Autenticación y Autorización:** Uso de **Spring Security** para gestionar el ciclo de vida de la sesión del usuario.
-   **Cifrado de Contraseñas:** Empleo de **BCrypt** para el almacenamiento seguro de credenciales.
-   **Validaciones:** Validación robusta en formularios tanto en el lado del cliente (HTML5) como en el servidor (`jakarta.validation`).
-   **Restricción por Rol:** Control de acceso granular que limita las funcionalidades disponibles según el rol (`ADMIN`, `INSTRUCTOR`, `ESTUDIANTE`).

## 🗄️ 3. Base de Datos Relacional
El sistema garantiza la persistencia y atomicidad de las operaciones financieras y académicas.
-   **Motor:** Uso de **PostgreSQL** (con compatibilidad para MySQL) como sistema de gestión de base de datos relacional.
-   **Capa de Persistencia:** Uso de **Spring Data JPA** para aprovechar el ecosistema de Spring, permitiendo el uso de prácticas modernas de ORM y reduciendo el código SQL manual.

## 🎨 4. Frontend Embebido con Thymeleaf
La experiencia de usuario está integrada directamente en el artefacto de la aplicación.
-   **Motor de Vistas:** Thymeleaf permite un renderizado dinámico y eficiente del lado del servidor.
-   **Diseño Responsivo:** Interfaz intuitiva construida con **Bootstrap 5**, asegurando la **compatibilidad móvil** y una visualización óptima en múltiples dispositivos.

## 📈 5. Escalabilidad y Mantenibilidad
El proyecto ha sido estructurado pensando en el crecimiento futuro.
-   **Código Modular:** Organización de paquetes por responsabilidad (Controller, Service, Repository, Model).
-   **Buenas Prácticas:** Adherencia a los estándares de desarrollo de Spring Boot y principios **SOLID**, facilitando la incorporación de nuevas funcionalidades sin comprometer la estabilidad del sistema existente.
