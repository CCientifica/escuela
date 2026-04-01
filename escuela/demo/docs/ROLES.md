# Usuarios y Roles del Sistema 👥

Este documento describe la matriz de roles, las responsabilidades de cada usuario y la implementación técnica de la seguridad en la **Academia Roller Speed**.

---

## 🎭 1. Matriz de Roles y Responsabilidades

| Rol | Descripción y Responsabilidades |
| :--- | :--- |
| **Administrador** | Responsable de la gestión global: alumnos, instructores, pagos y clases. Puede generar reportes financieros, gestionar la información institucional y autorizar nuevos usuarios. |
| **Instructor** | Perfil técnico encargado de la parte académica. Consulta sus horarios de clases, gestiona su lista de alumnos asignados, registra asistencia y asocia calificaciones/observaciones. |
| **Alumno / Aspirante** | Usuario final que se registra en la plataforma. Accede a su información académica personalizada, consulta sus horarios, realiza seguimiento de sus pagos y actualiza su perfil. |
| **Público General** | Visitante no autenticado. Puede navegar por la información corporativa (Misión, Visión, Valores), ver la oferta de servicios y acceder a los formularios de registro inicial. |

---

## 🛠️ 2. ¿Cómo se hizo? (Implementación Técnica)

La seguridad y diferenciación de roles se implementó utilizando el ecosistema de **Spring Security 6**, garantizando un Control de Acceso Basado en Roles (**RBAC**) robusto.

### A. Modelo de Datos (Capa de Persistencia)
Se definió la entidad **`Usuario`** como la fuente de verdad para la autenticación:
-   Contiene el campo `rol` (String) que almacena valores como `ADMIN`, `INSTRUCTOR` o `ESTUDIANTE`.
-   Las entidades `Estudiante` e `Instructor` tienen una relación `1:1` con `Usuario`, permitiendo vincular perfiles personales con credenciales de acceso.

### B. Configuración de Seguridad (`SecurityConfig.java`)
Se establecieron reglas de autorización por jerarquía de rutas:
```java
// Ejemplo de configuración de acceso por ruta
.requestMatchers("/panel/admin/**").hasRole("ADMIN")
.requestMatchers("/panel/instructor/**").hasAnyRole("INSTRUCTOR", "ADMIN")
.requestMatchers("/panel/estudiante/**").hasAnyRole("ESTUDIANTE", "ADMIN")
.requestMatchers("/", "/index", "/registro/**").permitAll()
```

### C. Proveedor de Identidad (`UserDetailsServiceImpl.java`)
Se implementó una clase de servicio que:
1.  Busca al usuario en la base de datos PostgreSQL por su correo electrónico.
2.  Crea un objeto `User` de Spring Security inyectando los **`GrantedAuthority`** correspondientes basados en el campo `rol` de la DB (prefijados con `ROLE_`).

### D. Interfaz Dinámica (Thymeleaf Security)
Para mejorar la experiencia de usuario (UX), se utilizó el dialecto de seguridad en las plantillas HTML para mostrar contenido condicional:
-   **`sec:authorize="hasRole('ADMIN')"`**: Muestra botones de gestión financiera solo al administrador.
-   **`sec:authorize="isAuthenticated()"`**: Cambia el botón de "Login" por el de "Mi Panel" y "Cerrar Sesión".
-   **`sec:authorize="isAnonymous()"`**: Muestra los enlaces de registro público.

---

## 🔒 3. Seguridad de Contraseñas
Las contraseñas no se almacenan en texto plano. Se utiliza el algoritmo **BCrypt** (vía `BCryptPasswordEncoder`) para generar hashes irreversibles, protegiendo los datos incluso en caso de acceso no autorizado a la base de datos.
