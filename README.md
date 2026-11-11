# PersonAPP - Arquitectura Hexagonal con Spring Boot (100% Contenerizado con Docker)

Proyecto de laboratorio que implementa una arquitectura limpia/hexagonal (Puertos y Adaptadores) para la gestión del modelo de datos `Persona`, `Profesión`, `Teléfono` y `Estudios`. 

**Todo el sistema está completamente contenerizado**, por lo que **no se requiere instalar Java, Maven, MariaDB ni MongoDB en la máquina host**. Solo se necesita tener **Docker** y **Docker Compose**.

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
  * *Casos de uso (`usecase`)*: Implementaciones que orquestan las reglas de negocio (`PersonUseCase`, `ProfessionUseCase`, `PhoneUseCase`, `StudyUseCase`).
* **`maria-output-adapter`**: Adaptador de salida para persistencia en MariaDB / MySQL utilizando JPA / Hibernate.
* **`mongo-output-adapter`**: Adaptador de salida para persistencia en MongoDB utilizando Spring Data MongoDB.
* **`rest-input-adapter`**: Adaptador de entrada REST (`@RestController`) que expone los endpoints HTTP con Swagger 3 (OpenAPI). Corre en el puerto `3000`.
* **`cli-input-adapter`**: Adaptador de entrada por terminal (`CommandLineRunner`) con menús interactivos por consola.
* **`common`**: Anotaciones semánticas de arquitectura (`@Adapter`, `@Port`, `@UseCase`, `@Mapper`), excepciones y enums de configuración.
* **`scripts`**: Scripts DDL y DML para MariaDB y MongoDB.

---

## 🐳 Despliegue 100% Contenerizado (Docker & Docker Compose)

### 1. Requisitos
* **Docker Engine** (versión 20.10+)
* **Docker Compose** (versión 2.0+)

> No es necesario instalar JDK, Maven, MariaDB ni MongoDB en el PC. Los Dockerfiles multi-etapa se encargan de compilar el código fuente y empaquetar los artefactos dentro de contenedores aislados.

### 2. Levantar la Infraestructura y la API REST

Para compilar las imágenes e iniciar **MariaDB** (puerto `3307`), **MongoDB** (puerto `27017`) y la **API REST** (puerto `3000`):

```bash
docker compose up -d --build
```

El orden de inicio y disponibilidad está garantizado mediante *Healthchecks* configurados en `docker-compose.yml`:
1. `mariadb` ejecuta automáticamente los scripts DDL y DML en `/docker-entrypoint-initdb.d/`.
2. `mongodb` ejecuta automáticamente los scripts DDL y DML en `/docker-entrypoint-initdb.d/`.
3. `rest-api` espera a que ambas bases de datos estén en estado saludable (*healthy*) antes de iniciar.

Para verificar el estado de los contenedores:
```bash
docker compose ps
```

Para ver los logs en tiempo real:
```bash
docker compose logs -f rest-api
```

---

## 🌐 Documentación Interactiva Swagger / OpenAPI 3

Una vez levantado el entorno con Docker, accede directamente desde el navegador:

👉 **[http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)**

### Endpoints Disponibles:

Todos los endpoints admiten la selección del motor de persistencia (`MARIA` o `MONGO`):

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

## 💻 Ejecución del Adaptador CLI (Consola Interactiva Contenerizada)

Para ejecutar la aplicación de consola interactiva dentro de su propio contenedor Docker conectado a la red interna:

```bash
docker compose run --rm cli
```

Esto abrirá la consola interactiva (`MenuPrincipal`) en tu terminal:
```text
==========================================
  SISTEMA PERSONAPP - ARQUITECTURA HEXAGONAL
==========================================
1 para trabajar con el Módulo de Personas
2 para trabajar con el Módulo de Profesiones
3 para trabajar con el Módulo de Teléfonos
4 para trabajar con el Módulo de Estudios
0 para Salir
==========================================
```

Cada submódulo permite seleccionar el motor de persistencia (`1: MariaDB`, `2: MongoDB`) y ejecutar operaciones de consulta, búsqueda, creación, edición, eliminación y conteo.

---

## 🛑 Detener los Contenedores

Para apagar todos los servicios:
```bash
docker compose down
```

Para apagar y eliminar también los volúmenes de datos:
```bash
docker compose down -v
```

---

## 👥 Colaboradores

* **Nicolás León Parra** - [`Nicolas11Leon`](https://github.com/Nicolas11Leon)
* **Warrior baby** - [`Warriorr89`](https://github.com/Warriorr89)
* **Savagee 22** - [`savaged-22`](https://github.com/savaged-22)
