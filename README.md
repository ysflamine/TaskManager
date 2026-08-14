# Task Manager

Aplicación de escritorio para la gestión de tareas personales o de equipo, desarrollada con **JavaFX** y **PostgreSQL**.

![Java](https://img.shields.io/badge/Java-17+-blue?logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-17+-green?logo=javafx)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue?logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?logo=apache-maven)

---

## Características

- **CRUD completo** de tareas (crear, leer, actualizar, eliminar).
- **Filtros inteligentes**: por estado (Pendiente, En progreso, Completada) y búsqueda por texto.
- **Panel de detalles** que muestra toda la información de la tarea seleccionada.
- **Alertas visuales** para tareas que vencen en los próximos 3 días.
- Interfaz limpia y responsiva con estilos CSS personalizados.

---

## Tecnologías utilizadas
- **Java 17+** – Lenguaje principal.
- **JavaFX 17** – Framework para la interfaz gráfica.
- **PostgreSQL 14+** – Base de datos relacional.
- **Maven** – Gestión de dependencias y construcción.
---

## Requisitos previos
- JDK 17 o superior (con JavaFX incluido o añadir módulos).
- PostgreSQL instalado y en ejecución.
- Maven (opcional, se puede usar el wrapper).

---

## Instalación y configuración
1. Clona el repositorio:
   ```bash
   git clone https://github.com/ysflamine/TaskManager
   ```
2. Crea la base de datos en PostgreSQL:
    ```bash
    CREATE DATABASE taskmanager_db;
    ```
3. Ejecutar lo siguiente para crear la tabla:
   ```sql
   CREATE TABLE tareas (
       id SERIAL PRIMARY KEY,
       titulo VARCHAR(100) NOT NULL,
       descripcion TEXT,
       prioridad INTEGER DEFAULT 1 CHECK (prioridad BETWEEN 1 AND 5),
       estado VARCHAR(20) NOT NULL CHECK (
           estado IN ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADA')
       ),
       responsable VARCHAR(100),
       fecha_limite DATE,
       fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

4. Copia config.properties.example a config.properties y modifica los datos de conexión.

   Edita config.properties con tus credenciales de PostgreSQL.

5. Compila y ejecuta con Maven.

### Capturas de pantalla

| | | | |
|:---:|:---:|:---:|:---:|
| <a href="https://ibb.co/RGg4Q99r"><img src="https://i.ibb.co/RGg4Q99r/3429-ED3-A-6143-48-DD-B3-B7-C667-E552-A5-A5.png" width="200" alt="Captura 1"></a> | <a href="https://ibb.co/QFByjZYH"><img src="https://i.ibb.co/QFByjZYH/FEF3344-A-91-DC-4-B84-89-DD-599-EAEFBBC12.png" width="200" alt="Captura 2"></a> | <a href="https://ibb.co/0ygPt7DG"><img src="https://i.ibb.co/0ygPt7DG/7-E228-B08-FACA-4-A0-B-A9-D9-BFF2-F0-D4970-A.png" width="200" alt="Captura 3"></a> | <a href="https://ibb.co/r2dsf9N7"><img src="https://i.ibb.co/r2dsf9N7/AE182-F59-E3-FF-435-E-A221-AE5-C61-D20672.png" width="200" alt="Captura 4"></a> |

### Uso básico
- **Añadir tarea:** Haz clic en **"Añadir"**, completa el formulario y guarda la tarea.
- **Editar tarea:** Selecciona una tarea en la tabla y haz clic en **"Editar"**.
- **Eliminar tarea:** Selecciona una tarea y haz clic en **"Eliminar"**. Se solicitará confirmación antes de eliminarla.
- **Buscar tareas:** Escribe en el campo de búsqueda para filtrar por título o descripción.
- **Filtrar por estado:** Utiliza el desplegable para mostrar únicamente las tareas de un estado concreto.
- **Ver detalles:** Al seleccionar una tarea, podrás consultar toda su información en el panel inferior.

