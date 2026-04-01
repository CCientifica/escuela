# 🎓 Guía de Presentación: Código Academia Roller Speed

Esta guía te servirá paso a paso para presentar el proyecto ante tu profesor. Está diseñada para resaltar la calidad técnica y la solidez de la arquitectura de clases.

---

## 🕒 1. Introducción (Visión General)
*   **Qué decir:** "Este es el sistema de gestión para la **Academia Roller Speed**. El objetivo es centralizar la administración académica, financiera y la comunicación institucional en una sola plataforma robusta basada en Spring Boot."
*   **Qué mostrar:** Abre el navegador en `localhost:8080/index` y muestra las noticias, testimonios y la agenda de eventos. Menciona: *"Ahora todo este contenido es dinámico, gestionado desde la base de datos PostgreSQL."*

---

## 🏛️ 2. Arquitectura de Clases (El punto fuerte)
*   **Qué decir:** "Diseñamos el sistema bajo una **arquitectura por capas** (N-Tier Architecture), lo cual garantiza la separación de responsabilidades y facilita el mantenimiento."
*   **Archivos a mostrar:**
    1.  **Paquete `model`:** Abre `Estudiante.java` o `Noticia.java`. Explica: *"Usamos Entidades JPA para mapear los objetos Java directamente a PostgreSQL mediante Hibernate."*
    2.  **Paquete `repository`:** Abre `NoticiaRepository.java`. Explica: *"Aprovechamos Spring Data JPA para las operaciones CRUD automáticas, evitando escribir SQL manual y reduciendo errores."*
    3.  **Paquete `service`:** Abre `EscuelaService.java`. Explica: *"Aquí reside la lógica de negocio, manteniendo a los controladores delgados y enfocados únicamente en el flujo de la web."*
    4.  **Paquete `controller`:** Abre `IndexController.java`. Explica: *"Como es una arquitectura MVC, los controladores inyectan los datos en vistas de Thymeleaf para renderizar contenido en tiempo real."*

---

## 🛡️ 3. Seguridad y Roles (BCrypt y RBAC)
*   **Qué decir:** "Implementamos seguridad basada en roles (RBAC) mediante **Spring Security**."
*   **Archivo a mostrar:** `SecurityConfig.java`. Explica: *"Definimos reglas claras: solo el ADMIN puede gestionar pagos, mientras que los instructores solo ven sus clases. Además, todas las contraseñas se almacenan con el algoritmo de hash BCrypt."*

---

## 🏗️ 4. Principios SOLID aplicados
Si el profesor pregunta por la arquitectura de las clases, usa estas frases:
*   **Single Responsibility Principle (SRP):** "Cada clase tiene un solo propósito. El controlador no sabe cómo se guarda el dato, solo cómo recibirlo."
*   **Dependency Injection (DI):** "Usamos inyección de dependencias (`@Autowired`) para desacoplar las clases y facilitar las pruebas unitarias."

---

## 💡 5. Conclusión y Estrategia de Datos
*   **Qué decir:** "Actualmente el sistema está en fase **Demo Funcional**. Usamos un `DataSeeder` para poblar la base de datos automáticamente con escenarios reales para esta presentación, pero el esquema ya está 100% preparado para escalar a una producción total."

---

> [!TIP]
> Recuerda que tienes el documento de **[CODIGO_Y_CLASES.md](./CODIGO_Y_CLASES.md)** por si el profesor desea entrar en detalles más técnicos sobre un diagrama específico. ¡Mucho éxito!
