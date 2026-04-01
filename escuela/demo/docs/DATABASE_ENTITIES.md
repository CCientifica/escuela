# Modelo de Datos Académicos y de Contenido 📂

Este documento detalla la estructura relacional del proyecto **Academia Roller Speed**.

## 🏗️ 1. Entidades del Sistema Académico

### `Estudiante`
- **id** (Long)
- **nombreCompleto** (String)
- **documentoIdentidad** (String)
- **correoElectronico** (String)
- **nivel** (String: Básico, Intermedio, Avanzado)
- **usuario** (1:1 con Usuario)

### `Instructor`
- **id** (Long)
- **nombre** (String)
- **especialidad** (String)
- **salario** (BigDecimal)
- **usuario** (1:1 con Usuario)

---

## 🏗️ 2. Entidades de Contenido Dinámico (NUEVAS)

Estas entidades han sido diseñadas para reemplazar el contenido estático del portal (Mocks) en la fase de producción.

### `Noticia`
- **id** (Long)
- **titulo** (String)
- **contenido** (TEXT)
- **fechaPublicacion** (LocalDate)
- **imagenUrl** (String)

### `Testimonio`
- **id** (Long)
- **autor** (String)
- **rolAutor** (String)
- **contenido** (TEXT)
- **estrellas** (Integer)

### `Evento`
- **id** (Long)
- **nombreEvento** (String)
- **fechaEvento** (LocalDate)
- **ubicacion** (String)
- **descripcion** (TEXT)

---

## 🛠️ 3. Configuración de Repositorios
Se han implementado Repositorios JPA para cada una de estas entidades, permitiendo operaciones CRUD (Crear, Leer, Actualizar, Borrar) directas sobre la base de datos sin necesidad de escribir SQL manual.
