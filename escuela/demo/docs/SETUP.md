# Guía de Instalación y Despliegue 🚀

Este documento proporciona los pasos necesarios para configurar el entorno de ejecución del proyecto **Academia Roller Speed**.

## 📋 Requisitos Previos

Asegúrate de contar con las siguientes herramientas configuradas en tu sistema:
-   **Java JDK 17** o superior.
-   **Maven 3.6+** (puedes usar el script `./mvnw` incluido).
-   **PostgreSQL 10+** (instancia local o en la nube).
-   **Terminal de comandos** compatible con Bash o PowerShell.

---

## 🛠️ Configuración para Desarrollo

### 1. Clonación del Repositorio
```bash
git clone <url-del-repositorio>
cd <nombre-del-proyecto>
```

### 2. Configuración de Base de Datos
Crea una base de datos local en PostgreSQL llamada `escuela_8axw`.
Asegúrate de ajustar las credenciales en `src/main/resources/application.properties` o definir las siguientes variables de entorno:
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/escuela_8axw
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
```

### 3. Ejecución en Modo Desarrollo
Utiliza el Maven Wrapper para iniciar la aplicación:
```bash
./mvnw spring-boot:run
```
La aplicación se servirá en `http://localhost:8080` (configuración por defecto).

---

## 🏗️ Construcción y Empaquetado

Para generar un archivo JAR autoejecutable listo para producción:
```bash
./mvnw clean package -DskipTests
```
El archivo resultante se encontrará en la carpeta `target/` con el nombre `demo-0.0.1-SNAPSHOT.jar`.

---

## ☁️ Despliegue en la Nube (Modelo Render)

Este proyecto está diseñado para desplegarse fácilmente en **Render** (o servicios similares).

### Variables de Entorno Requeridas:
| Variable | Propósito |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | Endpoint de la base de datos PostgreSQL remota. |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos remoto. |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos remoto. |
| `PORT` | Puerto de escucha (usualmente 8080 o el asignado por el servidor). |
| `APP_SEEDING_ENABLED` | `true` para precargar usuarios y registros de prueba iniciales. |

---

## 🏗️ Solución de Problemas Comunes

1.  **Error de Dialecto PostgreSQL:**
    Si usas una versión muy antigua de PostgreSQL, asegúrate de que el dialecto en `application.properties` esté configurado como `org.hibernate.dialect.PostgreSQLDialect`.
2.  **Permisos del script mvnw:**
    En sistemas Unix/macOS, otorga permisos de ejecución: `chmod +x mvnw`.
3.  **Conflictos con SSL:**
    Para bases de datos en la nube (como Render), asegúrate de que el parámetro `sslmode=require` esté presente al final de la URL si el servidor lo requiere.

---

## 🏛️ Soporte Operativo
Para dudas técnicas adicionales, contacta al administrador del sistema institucional.
