# Alcance del Proyecto 🎯

Este documento detalla los objetivos técnicos alcanzados, los estándares de desarrollo aplicados y la funcionalidad core implementada en el sistema **Academia Roller Speed**.

---

## 🏗️ 1. Configuración de Base de Datos (PostgreSQL)

Se implementó una capa de persistencia robusta utilizando el motor relacional **PostgreSQL**, configurada para garantizar la integridad de los datos y la escalabilidad del sistema.

### Objetivos Alcanzados:
-   **Configuración JDBC:** Conexión segura mediante `spring-boot-starter-data-jpa` con soporte para SSL.
-   **Mapeo ORM:** Uso de **Hibernate** para transformar entidades Java en tablas físicas de forma automática (`ddl-auto=update`).
-   **Estandarización de Tipos:** Empleo de tipos de datos adecuados como `BIGSERIAL` para identificadores, `DECIMAL` para montos financieros y `DATE/TIMESTAMP` para cronología académica.
-   **Integridad Referencial:** Definición de llaves foráneas (`FK`) y restricciones de unicidad (`UNIQUE`) en campos críticos como correos y documentos de identidad.

---

## 🗺️ 2. Navegación Funcional con Thymeleaf

El sistema utiliza **Thymeleaf** como motor de plantillas para renderizar dinámicamente las vistas desde el servidor, garantizando una navegación fluida y segura.

### Características de la Navegación:
-   **Fragmentos Reutilizables:** Implementación de componentes comunes (Navbar, Sidebar, Footer) mediante `th:fragment` para evitar redundancia de código.
-   **Ruteo Dinámico:** Uso de `@RequestMapping` y `@GetMapping` para orquestar el flujo entre las secciones públicas (Inicio, Registro) y privadas (Paneles de Estudiante, Instructor y Administrador).
-   **Seguridad en Vistas:** Integración con **Spring Security Dialect** (`sec:authorize`) para mostrar u ocultar elementos del menú según los permisos del usuario autenticado.

---

## 🎮 3. Desarrollo de Controladores (Estructura MVC)

Se desarrollaron controladores especializados para desacoplar la lógica de presentación de la lógica de negocio.

### Flujo de Vistas:
-   **Carga de Modelos:** Los controladores utilizan la interfaz `Model` para inyectar datos dinámicos (estudiantes, clases, pagos) en las plantillas HTML.
-   **Manejo de Formularios:** Implementación de métodos `@PostMapping` con validación de datos (`@Valid`) para el registro de nuevos alumnos e instructores.
-   **Redireccionamiento Inteligente:** Lógica post-login que dirige a cada usuario a su entorno de trabajo correspondiente basado en su rol institucional.

---

## 💎 4. Estándares SOLID y Calidad de Código

El diseño de las clases sigue los principios **SOLID** para asegurar un código mantenible, extensible y legible.

### Aplicación de Principios:
-   **S - Responsabilidad Única (SRP):** Cada clase tiene un propósito definido. Los controladores solo manejan el flujo web, mientras que las entidades representan el modelo de datos y los repositorios el acceso a los mismos.
-   **O - Abierto/Cerrado (OCP):** El uso de interfaces en la capa de servicios (`EscuelaService`, `UserDetailsService`) permite extender la funcionalidad sin modificar el código base.
-   **L - Sustitución de Liskov (LSP):** Las herencias y especializaciones (como en la configuración de seguridad) respetan contractualmente el comportamiento de las clases superiores.
-   **I - Segregación de Interfaces (ISP):** Se utilizan repositorios específicos para cada entidad (`EstudianteRepository`, `PagoRepository`) en lugar de repositorios genéricos y sobrecargados.
-   **D - Inversión de Dependencias (DIP):** Aplicación extensiva de **Inyección de Dependencias** mediante Spring (`@Autowired`) para desacoplar componentes y facilitar las pruebas unitarias.
