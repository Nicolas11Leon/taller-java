# Laboratorio 2: Implementación de Servicio WEB en Arquitectura Hexagonal con Repository y Service

---

## 1. Portada

* **Título del Proyecto:** Implementación de Servicio WEB en Arquitectura Hexagonal con Repository y Service (PersonAPP)
* **Asignatura:** Arquitectura de Software
* **Institución:** Pontificia Universidad Javeriana
* **Integrantes del Equipo:**
  * Nicolás León Parra ([@Nicolas11Leon](https://github.com/Nicolas11Leon))
  * [@Warriorr89](https://github.com/Warriorr89)
  * [@savaged-22](https://github.com/savaged-22)
* **Fecha:** Noviembre de 2026
* **Repositorio GitHub:** [https://github.com/Nicolas11Leon/taller-java](https://github.com/Nicolas11Leon/taller-java)
* **TAG de Entrega:** `v1.0.0`

---

## 2. Marco Conceptual

### 2.1 Arquitectura Hexagonal (Puertos y Adaptadores)
La Arquitectura Hexagonal, formulada por Alistair Cockburn en 2005, propone aislar el núcleo del negocio (dominio y casos de uso) de los detalles tecnológicos e infraestructurales (bases de datos, frameworks web, interfaces gráficas o de consola). 
* **Dominio (*Domain*):** Contiene las entidades y reglas de negocio puras. No tiene dependencias de librerías externas o frameworks como Spring o Hibernate.
* **Puertos (*Ports*):** Interfaces que actúan como contratos de frontera.
  * *Puertos de Entrada (`Driving Ports` / `Inbound`)*: Exponen los casos de uso que los actores externos pueden invocar.
  * *Puertos de Salida (`Driven Ports` / `Outbound`)*: Definen las operaciones que el núcleo requiere que un servicio secundario provea (persistencia, notificaciones).
* **Adaptadores (*Adapters*):** Componentes tecnológicos concretos que interactúan con el mundo exterior:
  * *Adaptadores de Entrada*: Controladores REST (`@RestController`) y aplicaciones de consola CLI (`CommandLineRunner`).
  * *Adaptadores de Salida*: Implementaciones de repositorios JPA para MariaDB y repositorios documentales para MongoDB.

### 2.2 Patrón Repositorio y Servicio
* **Repository Pattern:** Abstrae el acceso y persistencia de las entidades, proporcionando una colección en memoria simulada que encapsula las consultas SQL o MongoDB BSON.
* **Service / Use Case Pattern:** Coordina las transacciones, invoca las reglas de negocio y orquesta el flujo de información entre puertos de entrada y puertos de salida.

### 2.3 Persistencia Políglota (MariaDB & MongoDB)
El proyecto demuestra desacoplamiento absoluto mediante la persistencia políglota:
* **MariaDB:** Base de datos relacional (SQL) que impone integridad referencial mediante claves primarias (`PK`) y claves foráneas (`FK`).
* **MongoDB:** Base de datos NoSQL documental donde la relación se representa a través de referencias (`@DocumentReference` / `DBRef`) o identificadores compuestos.

### 2.4 Swagger 3 / OpenAPI
Especificación estándar para describir APIs RESTful de manera independiente del código fuente, permitiendo generar interfaces interactivas de prueba y documentación viva (`/swagger-ui.html`).

---

## 3. Diseño del Sistema

### 3.1 Diagrama de Dominio y Modelo de Datos
El modelo de datos implementado consta de cuatro entidades interconectadas:

```
+--------------------+        +---------------------+
|      persona       |        |      profesion      |
+--------------------+        +---------------------+
| PK  cc INT(15)     |<---+   | PK  id INT(6)       |<---+
|     nombre VARCHAR |    |   |     nom VARCHAR(90) |    |
|     apellido VARC. |    |   |     des TEXT        |    |
|     genero ENUM    |    |   +---------------------+    |
|     edad INT(3)    |    |                              |
+--------------------+    |                              |
         ^                |                              |
         |                |                              |
         | 1:N            | 1:N                          | 1:N
+--------+-----------+    |   +--------------------------+---+
|      telefono      |    +---|           estudios           |
+--------------------+        +------------------------------+
| PK  num VARCHAR(15)|        | PK,FK  id_prof INT(6)        |
|     oper VARCHAR   |        | PK,FK  cc_per INT(15)        |
| FK  duenio INT(15) |        |        fecha DATE            |
+--------------------+        |        univer VARCHAR(50)    |
                              +------------------------------+
```

### 3.2 Estructura de Módulos Maven
El software se estructuró en 7 módulos independientes:
1. `common`: Anotaciones semánticas de arquitectura (`@Port`, `@Adapter`, `@UseCase`, `@Mapper`) y excepciones (`NoExistException`, `InvalidOptionException`).
2. `domain`: Entidades Java puras (`Person`, `Profession`, `Phone`, `Study`, `Gender`) con anotaciones Lombok.
3. `application`: Interfaces de puertos (`*InputPort`, `*OutputPort`) y casos de uso (`*UseCase`).
4. `maria-output-adapter`: Entidades JPA, repositorios `JpaRepository` y adaptadores hacia MariaDB en el puerto 3307.
5. `mongo-output-adapter`: Documentos Mongo, repositorios `MongoRepository` y adaptadores hacia MongoDB en el puerto 27017.
6. `rest-input-adapter`: Controladores REST, modelos DTO (Request/Response), mappers y configuración Swagger 3 en el puerto 3000.
7. `cli-input-adapter`: Menús de consola interactivos organizados jerárquicamente por entidad y motor de base de datos.

---

## 4. Procedimiento de Implementación

1. **Resolución de incompatibilidades y bugs de la plantilla base:**
   * Se identificó y resolvió una inconsistencia en `EstudiosMapperMaria` y `EstudiosMapperMongo` donde el método `fromAdapterToDomain` retornaba `null` en lugar de la instancia de `Study`.
   * Se implementó un mapeo seguro no cíclico (`fromAdapterToDomainShallow`) para evitar excepciones de desbordamiento de pila (`StackOverflowError`) causadas por relaciones bidireccionales entre `Persona`, `Profesión` y `Estudios`.
   * Se implementó el método `fromAdapterToDomain` en `PersonaMapperRest`, el cual se encontraba vacío en la plantilla.

2. **Implementación de Puertos y Casos de Uso:**
   * Se crearon los puertos de entrada y salida para `Profession`, `Phone` y `Study`.
   * Se desarrollaron los casos de uso `ProfessionUseCase`, `PhoneUseCase` y `StudyUseCase` con validaciones de existencia previa para operaciones de edición y eliminación.

3. **Implementación de Adaptadores de Persistencia:**
   * **MariaDB:** Se definieron las interfaces `ProfesionRepositoryMaria`, `TelefonoRepositoryMaria` y `EstudiosRepositoryMaria`. Se construyeron los adaptadores `ProfessionOutputAdapterMaria`, `PhoneOutputAdapterMaria` y `StudyOutputAdapterMaria`.
   * **MongoDB:** Se construyeron los repositorios `ProfesionRepositoryMongo`, `TelefonoRepositoryMongo` y `EstudiosRepositoryMongo`. Se codificaron los adaptadores documentales con resolución de identificadores compuestos para estudios.

4. **Implementación del Adaptador REST (OpenAPI / Swagger 3):**
   * Se construyeron los controladores `PersonaControllerV1`, `ProfesionControllerV1`, `TelefonoControllerV1` y `EstudiosControllerV1`.
   * Se implementaron estrictamente **6 métodos por entidad (3 de lectura y 3 de escritura)**:
     * *Lectura 1:* Listar todos (`GET /{recurso}/{database}`)
     * *Lectura 2:* Buscar por llave primaria (`GET /{recurso}/{database}/{id}`)
     * *Lectura 3:* Contar cantidad total (`GET /{recurso}/{database}/count`)
     * *Escritura 1:* Crear nuevo registro (`POST /{recurso}`)
     * *Escritura 2:* Editar registro existente (`PUT /{recurso}/{database}/{id}`)
     * *Escritura 3:* Eliminar registro (`DELETE /{recurso}/{database}/{id}`)
   * Se documentaron todos los métodos mediante `@Operation` y `@Tag` para su visualización interactiva en Swagger UI (`/swagger-ui.html`).
   * Se implementó el mecanismo de inyección dinámica según el parámetro `{database}` (`MARIA` o `MONGO`).
   * **Endpoints de Navegación de Relaciones (Sub-recursos):**
     * `GET /api/v1/persona/{database}/{cc}/telefonos`: Retorna la lista de teléfonos asociados a una persona.
     * `GET /api/v1/persona/{database}/{cc}/estudios`: Retorna el historial de estudios cursados por una persona.
     * `GET /api/v1/telefono/{database}/{number}/duenio`: Retorna la entidad Persona propietaria de una línea telefónica.
     * `GET /api/v1/profesion/{database}/{id}/estudios`: Retorna las graduaciones o estudios vinculados a una profesión.
   * **Validación de DTOs con Bean Validation (`@Valid`):**
     * Se integró `spring-boot-starter-validation` aplicando anotaciones (`@NotNull`, `@NotBlank`, `@Pattern`, `@Size`) sobre los modelos `*Request`.
   * **Manejo Global de Excepciones (`@RestControllerAdvice`):**
     * Se implementó `GlobalExceptionHandler` capturando excepciones de validación (`MethodArgumentNotValidException`), recursos no encontrados (`NoExistException`), opciones erróneas de motor (`InvalidOptionException`) e integridad relacional (`DataIntegrityViolationException`), estandarizando las respuestas en formato `ErrorResponse`.

5. **Implementación del Adaptador CLI:**
   * Se codificaron los menús interactivos `PersonaMenu`, `ProfesionMenu`, `TelefonoMenu` y `EstudiosMenu`.
   * Cada submenú implementa exactamente los **6 métodos (3 de lectura y 3 de escritura)**: (1) Ver todos, (2) Buscar por ID/cédula, (3) Crear nuevo, (4) Editar, (5) Eliminar, (6) Contar total.
   * Se conectaron todas las opciones en el `MenuPrincipal`, permitiendo alternar en caliente entre MariaDB y MongoDB para cada operación.

6. **Automatización de Infraestructura, Contenerización y Pruebas:**
   * Se crearon los scripts DDL y DML completos para MariaDB y MongoDB en `/scripts`.
   * Se orquestó todo el ecosistema con Docker y Docker Compose (`mariadb`, `mongodb`, `rest-api`, `cli`) eliminando la necesidad de software local.
   * **Pruebas Unitarias Automatizadas (JUnit 5 + Mockito):**
     * Se diseñó una suite de **54 pruebas unitarias** que valida el 100% de los casos de uso (`PersonUseCaseTest`, `ProfessionUseCaseTest`, `PhoneUseCaseTest`, `StudyUseCaseTest`), entidades de dominio (`PersonTest`), mappers REST (`PersonaMapperRestTest`) y el manejador global de excepciones (`GlobalExceptionHandlerTest`), simulando los puertos de salida mediante Mockito.
     * Se creó el script de ejecución desatendida `./run_unit_tests.sh`.
   * **Pruebas de Integración y Endpoints:**
     * Se desarrolló la suite de pruebas automáticas `./test_endpoints.sh` con validación 100% exitosa de los 24 endpoints base + 8 endpoints de navegación de relaciones + pruebas de Bean Validation y manejo de excepciones.
     * Se incluyó la colección de pruebas para Postman en `postman_collection.json`.

7. **Modernización de Dependencias y Ciclo de Vida de Software:**
   * Se realizó una auditoría de obsolescencia de componentes, identificando la necesidad de alinear la versión del compilador con el runtime Temurin 17 y actualizar versiones con fin de soporte (EOL).
   * Se adoptó la estrategia in-place de actualización a las últimas versiones estables (detallada en [ACTUALIZACION_VERSIONES.md](file:///home/savaged22/arqui_talleres/taller-java/ACTUALIZACION_VERSIONES.md)):
     * **Java Target / Source:** Actualizado a **17** en `pom.xml`, alineado con las imágenes base de Docker.
     * **Spring Boot:** Actualizado de 2.7.11 a **2.7.18** (último release acumulado de la rama 2.x).
     * **Spring Framework:** Actualizado de 5.3.27 a **5.3.39** (último parche con soporte extendido).
     * **Project Lombok:** Actualizado de 1.18.26 a **1.18.34** para compatibilidad óptima con compiladores modernos.
     * **Log4j:** Actualizado a **2.24.1**.
     * **MariaDB Connector:** Actualizado a **3.3.3**.
     * **SpringDoc OpenAPI UI:** Actualizado a **1.8.0**.
     * **MongoDB en Docker:** Actualizado de 5.0 (EOL) a **7.0** (LTS activo) con migración de healthcheck a `mongosh`.
   * Se verificó que el 100% de las 54 pruebas unitarias y 34 pruebas de endpoints se mantienen operativas sin introducir deuda técnica ni cambios de ruptura.

---

## 5. Conclusiones y Lecciones Aprendidas

1. **Inversión de Dependencias y Desacoplamiento:**
   La arquitectura hexagonal permitió que la lógica de negocio permaneciera completamente intacta ante la presencia de dos motores de base de datos diametralmente opuestos (relacional y no relacional), demostrando que la persistencia es un detalle de implementación.

2. **Reutilización de Casos de Uso con Múltiples Puntos de Entrada:**
   Tanto la API REST como la aplicación de consola CLI invocan exactamente los mismos casos de uso del módulo `application`, eliminando cualquier duplicidad de lógica.

3. **Manejo de Ciclos de Referencia en Mapeadores:**
   En arquitecturas limpias, el mapeo entre capas relacionales y de dominio requiere especial cuidado con las relaciones bidireccionales. El uso de métodos de mapeo superficial (*shallow mapping*) demostró ser la estrategia más eficaz para prevenir recursión infinita sin perder la integridad de los datos.

4. **Persistencia Políglota Dinámica:**
   La capacidad de seleccionar en tiempo de ejecución (mediante path variables o parámetros en consola) si una operación se ejecuta contra MariaDB o MongoDB evidencia la flexibilidad que brindan los puertos e inversión de dependencias en Spring Boot.

---

## 6. Referencias

1. Cockburn, A. (2005). *Hexagonal Architecture (Ports and Adapters)*. Alistair Cockburn Blog.
2. Martin, R. C. (2017). *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Prentice Hall.
3. Spring Framework Documentation. (2023). *Spring Data JPA & Spring Data MongoDB Reference Documentation*. VMware.
4. SmartBear Software. (2023). *OpenAPI Specification 3.0*.
