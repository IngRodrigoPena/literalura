# 📚 Literalura

Aplicación de consola desarrollada en Java con Spring Boot que consume la API pública de Gutendex para buscar, registrar y consultar libros y autores, almacenando los datos en una base de datos PostgreSQL.

---

## 🖼️ Vista previa del proyecto

![Literalura Banner](assets/bibliotecaDigital.jpg)

---

## 🚀 Funcionalidades principales

1. **Buscar libro por título**

    * Consulta la API de Gutendex.
    * Registra libro y autor si no existen en la base.
    * Guarda solo el primer autor e idioma.
    * Evita duplicados con el `idGutendex`.

2. **Listar libros registrados**

    * Muestra libros con: título, autor, idioma y número de descargas.

3. **Listar autores registrados**

    * Lista los autores sin repetir.

4. **Consultar autores vivos en un año específico**

    * Filtra autores vivos en un año ingresado.

5. **Listar libros por idioma**

    * Muestra libros filtrando por código de idioma (`EN`, `ES`, etc.).

6. **Buscar autor por nombre**

    * Consulta autores por nombre y muestra sus libros registrados.

7. **Estadísticas**

    * Submenú con:

        * Promedio, mínimo, máximo y total de descargas.
        * Cantidad de libros por idioma.
        * Total de libros registrados.

---

## 🛠️ Tecnologías utilizadas

* Java 21
* Spring Boot
* Maven
* Spring Data JPA
* PostgreSQL
* Jackson (para deserialización JSON)
* API [Gutendex](https://gutendex.com/)

---

## 🧩 Estructura general del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com.aluracursos.literalura/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       ├── dto/
│   │       ├── ui/
│   │       └── util/
│   └── resources/
│       └── application.properties
```

---

## 🐘 Configuración de PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## ▶️ Ejecución

1. Clona el repositorio.
2. Configura `application.properties`.
3. Ejecuta `LiteraluraApplication.java`.
4. Interactúa con el menú por consola.

---

## 🗂️ Diagrama de la Base de Datos

```mermaid
erDiagram
    LIBRO {
        Long id PK
        Long idGutendex UNIQUE
        String titulo
        String idioma
        Integer numeroDescargas
    }

    AUTOR {
        Long id PK
        String nombre
        Integer fechaNacimiento
        Integer fechaMuerte
    }

    LIBRO ||--o{ LIBRO_AUTOR : contiene
    AUTOR ||--o{ LIBRO_AUTOR : escribe

    LIBRO_AUTOR {
        Long libro_id FK
        Long autor_id FK
    }
```

---

## 📝 Ejemplos de uso

**Buscar libro:**

```
> 1
Ingrese el título: don quijote
📚 Libro registrado exitosamente: Don Quijote
```

**Autores vivos en 1900:**

```
> 4
Ingrese el año: 1900
Autores vivos en 1900:
- Jules Verne (1828 - 1905)
- Mark Twain (1835 - 1910)
```

**Estadísticas:**

```
> 7
1. Estadísticas de descargas
📊 Total de libros: 5
📈 Promedio: 1863.20
📉 Mínimo: 120
📈 Máximo: 4720
```

---

## 📦 Extras implementados

* 📈 Estadísticas con `DoubleSummaryStatistics`
* 🔍 Buscar autor por nombre
* 🌍 Cantidad de libros por idioma
* 📊 Total de libros registrados

---

## 🧠 Notas importantes

* Solo se guarda el primer autor e idioma de cada libro.
* Se utiliza `idGutendex` para evitar duplicados.
* Valores nulos se manejan de forma segura (idioma desconocido → `N/A`).

---

## 💡 Futuras mejoras

* Mostrar libros por autor al listar autores.
* Top 10 libros más descargados.
* Consultas avanzadas por año o nombre parcial de autor.
* Exportar reportes en CSV o PDF.
