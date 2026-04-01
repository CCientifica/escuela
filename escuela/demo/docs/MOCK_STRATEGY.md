# Estrategia de Datos Simulados (Mocks) 🧪

Este documento justifica el uso de datos estáticos en la versión actual del sistema y define la estrategia para su mantenimiento y eventual reemplazo.

---

## 🏛️ 1. Razón de Ser
Durante la fase de **MVP (Producto Mínimo Viable)** y prototipado, se han integrado datos "quemados" (*hardcoded*) por las siguientes razones:
-   **Velocidad de Demostración:** Permite mostrar la interfaz de usuario con contenido realista sin necesidad de una carga manual exhaustiva en la base de datos.
-   **Diseño de Interfaz (UI/UX):** Facilita la validación visual de componentes como galerías, noticias y testimonios antes de desarrollar sus respectivos CRUDs.
-   **Independencia de Datos:** El sistema puede ser presentado de inmediato en entornos locales sin requerir un dump de base de datos específico para el área comercial.

---

## 🔍 2. Identificación de Simulaciones

Actualmente, existen tres focos de datos simulados:

### A. Contenido de Marketing (`index.html`)
-   **Testimonios:** Los perfiles de "María C.", "Andrés S." y "Carlos P." son estáticos.
-   **Noticias:** Artículos sobre entrenamientos y festivales son solo demostrativos.
-   **Galería:** Las rutas de imágenes apuntan a archivos locales fijos.

### B. Lógica de Cálculo (`DashboardController.java`)
-   **Ingresos y Cartera:** Se calculan multiplicando el conteo de registros por una constante (`50,000`). Esto ignora el campo real de `monto` en cada factura para propósitos de demostración de gráficos.

### C. Usuarios de Prueba (`DataSeeder.java`)
-   **Cuentas de Acceso:** Se generan automáticamente cuentas para cada rol (`ADMIN`, `INSTRUCTOR`, `ESTUDIANTE`) al iniciar la aplicación.

---

## 🛠️ 3. Consideraciones de Transición
Aunque funcionales para demostración, estos puntos deben ser desacoplados antes de un lanzamiento real. Consulte el **[Plan de Reemplazo](./PLAN_REEMPLAZO_DATOS.md)** para conocer los pasos técnicos de migración.
