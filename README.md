# 📚 GestorNotas — Aplicación de Gestión de Notas

Aplicación de escritorio desarrollada en **Java + JavaFX** con base de datos **SQLite** para la gestión de notas académicas. Permite a profesores registrar calificaciones y a alumnos consultar sus notas por módulo.

---

## 🗂️ Estructura del proyecto

```
src/
└── app/
    ├── MainApp.java          # Punto de entrada de la aplicación
    ├── DB.java               # Conexión a la base de datos SQLite
    ├── LoginView.java        # Pantalla de inicio de sesión
    ├── ProfesorView.java     # Vista del profesor (editar notas)
    └── AlumnoView.java       # Vista del alumno (consultar notas)
resources/
├── notas.db                  # Base de datos SQLite
└── ue.png                    # Logotipo mostrado en el login
```

---

## 🗃️ Base de datos

El archivo `notas.db` contiene las siguientes tablas:

### `usuarios`
| Campo      | Tipo    | Descripción                        |
|------------|---------|------------------------------------|
| `id`       | INTEGER | Clave primaria (autoincrement)     |
| `nombre`   | TEXT    | Nombre de usuario para el login    |
| `password` | TEXT    | Contraseña del usuario             |
| `rol`      | TEXT    | `profesor` o `alumno`              |

### `modulos`
| Campo        | Tipo    | Descripción                      |
|--------------|---------|----------------------------------|
| `id`         | INTEGER | Clave primaria (autoincrement)   |
| `nombre`     | TEXT    | Nombre del módulo                |
| `profesor_id`| INTEGER | FK → `usuarios.id`               |

### `matriculas`
| Campo       | Tipo    | Descripción                  |
|-------------|---------|------------------------------|
| `alumno_id` | INTEGER | FK → `usuarios.id`           |
| `modulo_id` | INTEGER | FK → `modulos.id`            |
| `nota`      | REAL    | Calificación del alumno      |

**Datos de prueba incluidos:**

| Usuario   | Contraseña | Rol      |
|-----------|------------|----------|
| Nataliia  | uem        | profesor |
| alumno1   | uem        | alumno   |
| alumno2   | uem        | alumno   |
| alumno3   | uem        | alumno   |

Módulos disponibles: **Programación** y **Entornos de Desarrollo**.

---

## ▶️ Cómo ejecutar

### Requisitos previos

- **Java 11** o superior
- **JavaFX SDK** (si no está incluido en el JDK)
- **SQLite JDBC Driver** (`sqlite-jdbc-*.jar`)

### Compilación y ejecución

1. Clona o descarga el repositorio.
2. Asegúrate de que `notas.db` y `ue.png` están en el classpath (carpeta `resources/`).
3. Compila e incluye las dependencias:

```bash
javac --module-path /ruta/javafx-sdk/lib \
      --add-modules javafx.controls \
      -cp sqlite-jdbc.jar \
      src/app/*.java -d out/
```

4. Ejecuta la aplicación:

```bash
java --module-path /ruta/javafx-sdk/lib \
     --add-modules javafx.controls \
     -cp out/:sqlite-jdbc.jar \
     app.MainApp
```

> Si usas un IDE como **IntelliJ IDEA** o **Eclipse**, importa el proyecto, añade JavaFX y el driver JDBC a las dependencias del módulo y ejecuta `MainApp`.

---

## 🖥️ Uso de la aplicación

### Pantalla de Login

Al iniciar la aplicación aparece el formulario de acceso. Introduce:
- **Usuario** y **contraseña**
- **Cargo**: `alumno` o `profesor`

Pulsa **ENTRAR** para acceder. Con **LIMPIAR** se borran los campos y con **SALIR** se cierra la aplicación.

### Vista Profesor

1. Selecciona un **módulo** del desplegable (solo aparecen los módulos asignados al profesor).
2. La tabla muestra los alumnos matriculados y sus notas actuales.
3. Modifica las notas directamente en la tabla.
4. Pulsa **GUARDAR NOTAS** para persistir los cambios en la base de datos.
5. Pulsa **VOLVER** para regresar al login.

### Vista Alumno

1. Selecciona un **módulo** del desplegable (solo aparecen los módulos en los que está matriculado).
2. La tabla muestra la calificación obtenida en ese módulo.
3. Pulsa **VOLVER** para regresar al login.

---

## 🏗️ Descripción de clases

| Clase           | Descripción                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| `MainApp`       | Lanza la aplicación JavaFX y crea la ventana principal (760×380 px).        |
| `DB`            | Gestiona la conexión JDBC a `notas.db` mediante `DriverManager`.            |
| `LoginView`     | Formulario de login con validación de usuario, contraseña y rol.            |
| `ProfesorView`  | Carga módulos y alumnos del profesor; permite editar y guardar notas.       |
| `AlumnoView`    | Carga los módulos matriculados y muestra las notas del alumno.              |

---

## ⚠️ Notas importantes

- La contraseña se almacena en texto plano en la base de datos. Para un entorno de producción se recomienda usar **hashing** (p. ej. BCrypt).
- La tabla `matriculas` no tiene clave primaria definida explícitamente; considera añadir una restricción `UNIQUE (alumno_id, modulo_id)` para evitar duplicados.
- La URL de conexión `jdbc:sqlite::resource:notas.db` requiere que el archivo esté disponible en el classpath.

---

## 📄 Licencia

Proyecto académico de uso interno. Sin licencia de distribución.
