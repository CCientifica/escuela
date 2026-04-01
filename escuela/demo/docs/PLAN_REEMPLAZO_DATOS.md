# Plan de Reemplazo de Datos a Producción ⚙️

Este documento describe los cambios técnicos necesarios para transicionar el sistema **Academia Roller Speed** de una versión demostrativa a una versión de producción 100% dinâmica.

---

## 🛠️ 1. Lógica Financiera (Crítico)
Actualmente, el `DashboardController` utiliza una constante de **50,000** para cálculos de cartera e ingresos totales, ignorando los montos reales en cada factura.

### Acciones:
1.  **Modificar el `DashboardController.java`:** Cambiar el cálculo del stream para que sea:
    ```java
    java.math.BigDecimal ingresosTotales = todosLosPagos.stream()
            .map(pago -> pago.getMonto()) // <--- Cambiar aquí en lugar de constante
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    ```
2.  **Validar Inserción:** Asegurar que los formularios de registro incluyan el monto exacto en el campo `monto`.

---

## 🎨 2. Contenido de Marketing a Base de Datos
Las noticias, testimonios y eventos en `index.html` están estáticos dentro del archivo HTML.

### Acciones:
1.  **Implementar Entidades `@Entity`:** Crear tablas para `Noticia`, `Testimonio` y `Evento`.
2.  **Desarrollar Controladores Administrativos:** Crear el módulo en el dashboard administrativo para que el usuario pueda agregar, editar o borrar estos elementos.
3.  **Inyectar Vistas Dinámicas:** Modificar el `EscuelaController` para que cargue la información desde los repositorios y la envíe al `index.html` mediante `th:each`.

---

## 📁 3. Almacenamiento de Multimedia
Las fotos y videos actuales apuntan a la carpeta `/static/images/` que es solo de lectura.

### Acciones:
1.  **Servicio de Almacenamiento (I/O):** Implementar un servicio que guarde archivos en el sistema local o un servicio de nube (Cloudinary/S3).
2.  **Referencias en DB:** Guardar el nombre del archivo o la URL en el campo correspondiente del alumno o instructor.

---

## 🔒 4. Usuarios de Producción
El `DataSeeder.java` genera usuarios de prueba dinámicamente cada vez que se levanta la aplicación.

### Acciones:
1.  **Desactivar Semilla:** Cambiar `app.seeding.enabled=false` en el entorno de producción.
2.  **Migración SQL:** Crear un archivo `data.sql` que contenga únicamente el usuario administrador institucional principal.
3.  **Prohibir Eliminación Accidental:** Implementar protecciones en el repositorio de usuarios para el ID del administrador principal.
