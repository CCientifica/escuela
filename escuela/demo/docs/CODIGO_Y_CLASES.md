# 🏗️ Deep-Dive: Arquitectura de Clases y Código Java

Este documento profundiza en el diseño de ingeniería del software aplicado a la **Academia Roller Speed**. Es la base técnica que justifica el uso de **Spring Boot 3**.

---

## 🏛️ 1. Patrón Arquitectónico: N-Tier (Por Capas)
Hemos implementado una separación física y lógica de responsabilidades para evitar el "Big Ball of Mud" (código espagueti).

### A. Capa de Dominio (Paquete `model`)
*   **Entidades:** Clases como `Estudiante`, `Pago` y `Testimonio`.
*   **Tecnología:** **JPA (Java Persistence API)**.
*   **Detalle:** "Cada clase es un espejo de una tabla en PostgreSQL. Usamos anotaciones `@Column(nullable = false)` para forzar la integridad de datos desde el corazón de la aplicación, no solo en la DB."

### B. Capa de Acceso a Datos (Paquete `repository`)
*   **Componentes:** Interfaces que extienden `JpaRepository<T, ID>`.
*   **Abstracción:** "Esta capa implementa el **patrón Repository**. No escribimos SQL manual; Spring genera los Proxies en tiempo de ejecución. Esto nos protege contra inyecciones SQL y garantiza que el código sea agnóstico a la base de datos (podríamos cambiar de PostgreSQL a MySQL sin tocar una sola línea de código Java)."

### C. Capa de Servicio (Paquete `service`)
*   **Componentes:** Clases anotadas con `@Service`.
*   **Lógica:** "Es la orquestadora. Aquí se manejan las transacciones (`@Transactional`). Si un pago falla a mitad de camino, la base de datos hace rollback automático, manteniendo la consistencia."

### D. Capa de Presentación (Paquete `controller`)
*   **Componentes:** Clases anotadas con `@Controller`.
*   **Motor de Plantillas:** **Thymeleaf**.
*   **MVC:** "Implementamos el patrón **Model-View-Controller**. El controlador recibe el `Model` de Spring, le inyecta los objetos de negocio y lo entrega a la `View` (HTML). La lógica de renderizado se queda en el servidor (Server-Side Rendering), lo que mejora el SEO y la seguridad."

---

## 🛠️ 2. Cumplimiento de Principios SOLID

| Principio | Aplicación en el Proyecto |
| :--- | :--- |
| **S**ingle Responsibility (SRP) | Los Repositorios solo persisten. Los Servicios solo ejecutan lógica. Los Controladores solo enrutan. |
| **O**pen/Closed (OCP) | El sistema está diseñado para extenderse (ej. nuevos tipos de Pagos) sin modificar la lógica base del `IndexController`. |
| **L**iskov Substitution | Usamos herencia estándar y cumplimos los contratos de las interfaces de Spring. |
| **I**nterface Segregation | Preferimos interfaces pequeñas. Noten cómo cada entidad tiene su propio Repositorio especializado. |
| **D**ependency Inversion | **Inyección de Dependencias (DI)**. Inyectamos interfaces, no implementaciones concretas, facilitando el desacoplamiento. |

---

## 🛡️ 3. Seguridad a Nivel de Clase (`SecurityConfig`)
*   **BCrypt:** "Las contraseñas nunca fluyen en texto plano. Usamos `BCryptPasswordEncoder` (fuerza 10) para el hash."
*   **RBAC:** "El control de acceso está centralizado. Si el día de mañana queremos añadir un rol de 'Invitado', solo modificamos una línea en la configuración de seguridad, sin tocar los controladores."

---

## 📂 4. Mapa de Relaciones (Para el Profesor)
*   **IndexController** → **TestimonioRepository** (Lectura de comunidad)
*   **DataSeeder** → **Todos los Repositorios** (Bootstrap de datos)
*   **SecurityConfig** → **UsuarioRepository** (Autenticación)

---

> [!TIP]
> **Defensa del Proyecto:** "Profesor, el código no solo funciona; está **diseñado para crecer**. La estructura de estas clases permite integrar pruebas unitarias con JUnit y Mockito de manera natural gracias a la inyección de dependencias."
