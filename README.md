# PersonAPP - Arquitectura Hexagonal con Spring Boot

Proyecto de laboratorio que implementa una arquitectura limpia/hexagonal (Puertos y Adaptadores) para la gestión del modelo de datos `Persona`, `Profesión`, `Teléfono` y `Estudios`, soportando persistencia dual en **MariaDB** y **MongoDB**, y dos adaptadores de entrada independientes: **API REST** (con Swagger 3 / OpenAPI) y **Línea de Comandos (CLI)** interactiva.

---

## 📋 Modelo de Datos

El sistema implementa el CRUD completo para las siguientes entidades relacionadas:

* **`persona`**: `cc` (PK), `nombre`, `apellido`, `genero` (ENUM 'M', 'F'), `edad`
* **`profesion`**: `id` (PK), `nom`, `des`
* **`telefono`**: `num` (PK), `oper`, `duenio` (FK hacia `persona.cc`)
* **`estudios`**: `id_prof` (PK, FK hacia `profesion.id`), `cc_per` (PK, FK hacia `persona.cc`), `fecha`, `univer`

---

## 🏛️ Arquitectura del Sistema (Hexagonal / Clean Architecture)

El proyecto está modularizado utilizando Maven multi-módulo:

* **`domain`**: Entidades del modelo de negocio puras (`Person`, `Profession`, `Phone`, `Study`, `Gender`) desacopladas de cualquier framework.
* **`application`**: Casos de uso y puertos:
  * *Puertos de entrada (`port.in`)*: Interfaces que definen las operaciones ofrecidas por el sistema (`PersonInputPort`, `ProfessionInputPort`, `PhoneInputPort`, `StudyInputPort`).
  * *Puertos de salida (`port.out`)*: Interfaces que definen la comunicación hacia el exterior (`PersonOutputPort`, `ProfessionOutputPort`, `PhoneOutputPort`, `StudyOutputPort`).
  * *Casos de uso (`usecase`)*: Implementaciones que orquestan las reglas de negocio.
* **`maria-output-adapter`**: Adaptador de salida para persistencia en MariaDB / MySQL utilizando JPA / Hibernate.
* **`mongo-output-adapter`**: Adaptador de salida para persistencia en MongoDB utilizando Spring Data MongoDB.
* **`rest-input-adapter`**: Adaptador de entrada REST (`@RestController`) que expone los endpoints HTTP con Swagger 3 (OpenAPI). Corre en el puerto `3000`.
* **`cli-input-adapter`**: Adaptador de entrada por terminal (`CommandLineRunner`) con menús interactivos por consola.
* **`common`**: Anotaciones personalizadas (`@Adapter`, `@Port`, `@UseCase`, `@Mapper`), excepciones y enums de configuración.
* **`scripts`**: Scripts DDL y DML para MariaDB y MongoDB.

---

## 🚀 Requisitos del Ambiente

* **Java JDK**: 11 o 17 (probado y compatible con JDK 17 y JDK 21).
* **Maven**: 3.8+ (se incluye script `./mvnw`).
* **MariaDB**: Puerto `3307` (Base de datos: `persona_db`, Usuario: `persona_db`, Contraseña: `persona_db`).
* **MongoDB**: Puerto `27017` (Base de datos: `persona_db`, Usuario: `persona_db`, Contraseña: `persona_db`, Auth DB: `admin`).
* **Docker & Docker Compose** (opcional, para levantar las bases de datos de inmediato).

---

## 🐳 Despliegue de Bases de Datos con Docker Compose

Para levantar automáticamente MariaDB (puerto 3307) y MongoDB (puerto 27017) con los scripts DDL y DML ejecutados:

```bash
docker compose up -d
```

### Configuración manual de bases de datos:
Si no utilizas Docker, ejecuta los scripts ubicados en la carpeta `scripts/`:
1. **MariaDB**:
   * Ejecutar `scripts/persona_ddl_maria.sql`
   * Ejecutar `scripts/persona_dml_maria.sql`
2. **MongoDB**:
   * Ejecutar `scripts/persona_ddl_mongo.js`
   * Ejecutar `scripts/persona_dml_mongo.js`

---

## 🛠️ Compilación del Proyecto

Para compilar todos los módulos del proyecto:

```bash
./mvnw clean compile
```

O empaquetar los artefactos `.jar`:

```bash
./mvnw clean package -DskipTests
```

---

## 🌐 Ejecución del Adaptador REST (API & Swagger)

El adaptador REST es una aplicación Spring Boot independiente que expone la API en el puerto `3000`.

### Iniciar la API REST:
```bash
./mvnw spring-boot:run -pl rest-input-adapter
```

### Documentación Swagger / OpenAPI 3:
Una vez iniciada la aplicación, accede desde el navegador web a:
👉 **[http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)**

### Endpoints Principales:

Todos los endpoints admiten la selección del motor de base de datos (`MARIA` o `MONGO`):

| Módulo | Método | Endpoint | Descripción |
| :--- | :--- | :--- | :--- |
| **Persona** | `GET` | `/api/v1/persona/{database}` | Listar todas las personas |
| | `GET` | `/api/v1/persona/{database}/{cc}` | Obtener persona por cédula |
| | `POST` | `/api/v1/persona` | Crear nueva persona |
| | `PUT` | `/api/v1/persona/{database}/{cc}` | Actualizar persona |
| | `DELETE` | `/api/v1/persona/{database}/{cc}` | Eliminar persona |
| | `GET` | `/api/v1/persona/{database}/count` | Conteo total de personas |
| **Profesión** | `GET` | `/api/v1/profesion/{database}` | Listar todas las profesiones |
| | `GET` | `/api/v1/profesion/{database}/{id}` | Obtener profesión por ID |
| | `POST` | `/api/v1/profesion` | Crear nueva profesión |
| | `PUT` | `/api/v1/profesion/{database}/{id}` | Actualizar profesión |
| | `DELETE` | `/api/v1/profesion/{database}/{id}` | Eliminar profesión |
| | `GET` | `/api/v1/profesion/{database}/count`| Conteo total de profesiones |
| **Teléfono** | `GET` | `/api/v1/telefono/{database}` | Listar todos los teléfonos |
| | `GET` | `/api/v1/telefono/{database}/{num}`| Obtener teléfono por número |
| | `POST` | `/api/v1/telefono` | Crear nuevo teléfono |
| | `PUT` | `/api/v1/telefono/{database}/{num}`| Actualizar teléfono |
| | `DELETE` | `/api/v1/telefono/{database}/{num}`| Eliminar teléfono |
| | `GET` | `/api/v1/telefono/{database}/count` | Conteo total de teléfonos |
| **Estudios** | `GET` | `/api/v1/estudio/{database}` | Listar todos los estudios |
| | `GET` | `/api/v1/estudio/{database}/{idProf}/{ccPer}` | Obtener estudio específico |
| | `POST` | `/api/v1/estudio` | Crear nuevo estudio |
| | `PUT` | `/api/v1/estudio/{database}/{idProf}/{ccPer}` | Actualizar estudio |
| | `DELETE` | `/api/v1/estudio/{database}/{idProf}/{ccPer}` | Eliminar estudio |
| | `GET` | `/api/v1/estudio/{database}/count` | Conteo total de estudios |

---

## 💻 Ejecución del Adaptador CLI (Consola)

El adaptador CLI es una aplicación Spring Boot de consola interactiva.

### Iniciar la Consola interactiva:
```bash
./mvnw spring-boot:run -pl cli-input-adapter
```

El menú interactivo permite elegir:
1. Módulo (`Persona`, `Profesión`, `Teléfono`, `Estudio`)
2. Motor de persistencia (`1: MariaDB`, `2: MongoDB`)
3. Operación CRUD (`Ver todo`, `Buscar`, `Crear`, `Editar`, `Eliminar`, `Contar`)

---

## 👥 Colaboradores

* **Nicolás León Parra** - [`Nicolas11Leon`](https://github.com/Nicolas11Leon)
* **Warrior baby** - [`Warriorr89`](https://github.com/Warriorr89)
* **Savagee 22** - [`savaged-22`](https://github.com/savaged-22)
