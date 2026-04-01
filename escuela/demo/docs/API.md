# Guía del API y Controladores 🚀

Este documento detalla los principales controladores y sus puntos finales (*endpoints*) dentro de la aplicación **Academia Roller Speed**.

## 📁 Controladores Principales

### 1. `DashboardController` (`/panel/**`)
Es el motor central del área administrativa y privada para todos los perfiles de usuario.
-   **Endpoints Administrativos:**
    -   `GET /panel/admin/estudiantes`: Listado y búsqueda global de estudiantes.
    -   `GET /panel/admin/instructores`: Gestión de perfiles y aprobación de instructores.
    -   `GET /panel/admin/clases`: Administración de oferta académica y asignación de docentes.
    -   `GET /panel/admin/pagos`: Reporte global de ingresos y deudores (cartera).
-   **Endpoints Estudiante:**
    -   `GET /panel/estudiante`: Dashboard con resumen de progreso.
    -   `GET /panel/estudiante/horarios`: Consulta de clases disponibles e inscripciones.
    -   `POST /panel/estudiante/clases/{id}/matricular`: Auto-inscripción a clases.
-   **Endpoints Instructor:**
    -   `GET /panel/instructor`: Resumen de carga académica y alumnos.
    -   `GET /panel/instructor/clases/{id}`: Detalle para toma de asistencia y registro de notas.
-   **Vista Relacionada:** Carpeta `src/main/resources/templates/dashboard/`

### 2. `AuthController` (`/login/**`)
Maneja el ciclo de vida de la autenticación de usuarios.
-   **Endpoints:**
    -   `GET /login`: Despliega la página institucional de acceso.
    -   `POST /login`: Procesa las credenciales locales contra el repositorio de usuarios.
    -   `GET /login/success`: Redireccionador inteligente basado en el rol del usuario autenticado.
    -   `POST /logout`: Finaliza la sesión de forma segura y destruye el contexto.
-   **Mecánica:** Integración nativa con `UserDetailsService` de Spring Security.

### 3. `RegistroController` (`/registro/**`)
Permite la captura de nuevos aspirantes de forma pública.
-   **Endpoints:**
    -   `GET /registro/estudiantes`: Formulario público de pre-inscripción para alumnos.
    -   `POST /registro/estudiantes`: Guarda el nuevo perfil en la base de datos vinculado a un usuario.
    -   `GET /registro/instructor`: Aplicación para nuevos docentes en la academia.
-   **Vistas Relacionadas:** `registro-estudiante.html`, `registro-instructor.html`

### 4. `EscuelaController` (`/contacto/**`, `/index`)
Controlador para la presencia pública institucional.
-   **Endpoints:**
    -   `GET /index`: Página principal con noticias, galerías y testimonios.
    -   `POST /contacto`: Captura mensajes de prospectos y los registra como **PQRS**.
    -   `GET /corporativo`: Información de misión, visión y valores.

---

## 🏗️ Respuestas y Formatos

-   **HTML (SSR):** El sistema utiliza Thymeleaf para inyectar modelos de datos directamente en las vistas antes de ser servidas al navegador.
-   **Seguridad:** Los endpoints sensibles están protegidos mediante el contexto de `SecurityConfig`, requiriendo roles específicos (`ADMIN`, `ESTUDIANTE`, `INSTRUCTOR`).
-   **Redirecciones:** Uso extensivo de `RedirectAttributes` para enviar mensajes de éxito o error parpadeantes (*flash attributes*) entre vistas.

---

## 🏗️ Roles y Permisos

| Prefijo de Ruta | Permisos Requeridos | Propósito |
| :--- | :--- | :--- |
| `/panel/admin/**` | `ROLE_ADMIN` | Gestión operativa total. |
| `/panel/instructor/**` | `ROLE_INSTRUCTOR`, `ROLE_ADMIN` | Gestión académica y seguimiento de alumnos. |
| `/panel/estudiante/**` | `ROLE_ESTUDIANTE`, `ROLE_ADMIN` | Consulta de progreso, pagos y horarios. |
| `/registro/**`, `/index` | Públicos | K-prospección y marketing. |
