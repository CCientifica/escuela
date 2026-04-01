# Escuela de Formación Roller Speed 🎓

Bienvenido a la documentación técnica del sistema de gestión para la **Academia Roller Speed**. Este proyecto es una aplicación integral desarrollada con **Spring Boot 4** y **PostgreSQL**, diseñada para centralizar la administración académica y financiera de la institución.

## 📋 Contenido

1. [Visión General](#visión-general)
2. [Alcance del Proyecto](./docs/SCOPE.md)
3. [Usuarios y Roles del Sistema](./docs/ROLES.md)
4. [Requerimientos No Funcionales](./docs/NON_FUNCTIONAL.md)
5. [Arquitectura del Sistema](./docs/ARCHITECTURE.md)
6. [Modelo y Configuración de DB](./docs/DATABASE.md)
7. [Estrategia de Datos Simulados (Mocks)](./docs/MOCK_STRATEGY.md)
8. [Plan de Reemplazo para Producción](./docs/PLAN_REEMPLAZO_DATOS.md)
9. [Guía del API y Controladores](./docs/API.md)
10. [Instalación y Despliegue](./docs/SETUP.md)

---

## 🏛️ Visión General

El sistema permite la gestión integral de:
- **Estudiantes:** Registro, seguimiento de progreso y gestión de perfiles.
- **Instructores:** Gestión de docentes, asignación de clases y valoraciones.
- **Academia:** Control de clases, horarios, toma de asistencia y calificaciones.
- **Finanzas:** Registro de pagos y control de cartera.
- **Atención al Usuario (PQRS):** Módulo para capturar peticiones, quejas y reclamos de la comunidad.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17+**: Lenguaje base.
- **Spring Boot 4.0.3**: Framework principal para el backend.
- **Spring Data JPA**: Implementación de capa de persistencia.
- **Spring Security**: Seguridad basada en sesiones locales y roles.
- **Maven**: Gestión de construcción y dependencias.
- **Lombok**: Para reducir el código repetitivo (*boilerplate*).

### Frontend
- **Thymeleaf**: Motor de plantillas servidor.
- **Bootstrap 5 & Font Awesome**: Estilizado y componentes visuales.
- **JavaScript (Vanilla)**: Lógica en el cliente e interactividad.
- **Google Fonts (Inter)**: Tipografía institucional.

### Base de Datos
- **PostgreSQL**: Motor relacional para almacenamiento persistente.

---

## 🚀 Próximos Pasos

Para comenzar con la configuración del proyecto, por favor consulte la [Guía de Instalación](./docs/SETUP.md).
