# Auditoría y Plan de Modernización de Versiones (PersonAPP)

Este documento detalla el diagnóstico de obsolescencia de componentes, la matriz de actualización de versiones aplicada bajo la **Estrategia In-Place (Opción A)**, su justificación técnica, el análisis de impacto y los resultados de verificación en el proyecto `taller-java`.

---

## 1. Justificación y Diagnóstico Inicial

El proyecto inicial contaba con diversas librerías y componentes en estado **End-of-Life (EOL)** o desactualizados respecto al entorno de ejecución en contenedores Docker:

1. **Java Version Mismatch:**
   * Las imágenes Docker (`Dockerfile.rest` y `Dockerfile.cli`) ya utilizaban `eclipse-temurin:17-jre-alpine` y `maven:3.9.6-eclipse-temurin-17`.
   * Sin embargo, el archivo raíz `pom.xml` fijaba `<global.java.version>11</global.java.version>`, obligando al compilador a restringir el bytecode a la especificación de Java 11 y perdiendo las optimizaciones del runtime y JIT de Java 17.
2. **Spring Boot y Spring Framework EOL:**
   * La versión `2.7.11` de Spring Boot y `5.3.27` de Spring Framework alcanzaron el fin de soporte abierto (noviembre de 2023 y diciembre de 2024 respectivamente), existiendo versiones de mantenimiento posteriores en la misma serie (`2.7.18` y `5.3.39`) con resolución de fallos y parches de seguridad.
3. **Lombok y Herramientas de Compilación:**
   * La versión `1.18.26` de Lombok presentaba advertencias de procesamiento de anotaciones ante compiladores de JDK 17 y JDK 21.
4. **Conector JDBC MariaDB y Documentación OpenAPI:**
   * El cliente `mariadb-java-client` en `3.1.4` y `springdoc-openapi-ui` en `1.7.0` contaban con versiones de parche finales (`3.3.3` y `1.8.0`) con mayor estabilidad en pooling de conexiones y visualización de esquemas OpenAPI 3.
5. **Motor NoSQL MongoDB en Docker:**
   * La imagen oficial `mongo:5.0` alcanzó su fin de vida en octubre de 2024. Su actualización a `mongo:7.0` garantiza soporte activo y seguridad a nivel de motor.

---

## 2. Decisión de Arquitectura: Elección de la Opción A

Se optó por la **Opción A (Modernización In-Place a las Últimas Versiones Estables)** por las siguientes razones:

* **Estabilidad y Continuidad Operativa:** Una migración mayor a Spring Boot 3.x (Opción B) requiere la refactorización obligatoria de paquetes de `javax.*` a `jakarta.*` (`javax.persistence.*`, `javax.validation.*`, `javax.xml.bind.*`), lo cual introduce un alto riesgo de regresión en entidades JPA, DTOs y validadores en un sistema en producción o evaluación.
* **Alineación con Java 17 LTS:** Al subir el target de compilación a Java 17 nativo, el código aprovecha plenamente el compilador y JRE moderno sin necesidad de romper contratos de API ni firmas de librerías.
* **Cero Rompimiento de Contratos:** Todas las interfaces de puertos, casos de uso, adaptadores de persistencia (MariaDB / MongoDB) y controladores REST mantienen su compatibilidad al 100%.

---

## 3. Matriz Comparativa de Actualizaciones

| Componente / Dependencia | Ubicación | Versión Anterior | Versión Actualizada | Tipo de Cambio | Justificación |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **Java Target / Source** | `pom.xml` | `11` | **`17`** | Minor (LTS) | Alineación nativa con el runtime Docker Temurin 17 y soporte LTS extendido. |
| **Spring Boot** | `pom.xml` | `2.7.11` | **`2.7.18`** | Patch (Final 2.x) | Última versión de la rama 2.x con acumulado de correcciones y parches de seguridad. |
| **Spring Framework** | `pom.xml` | `5.3.27` | **`5.3.39`** | Patch (Final 5.x) | Último parche oficial de la serie 5.3 para compatibilidad y estabilidad del núcleo. |
| **Project Lombok** | `pom.xml` | `1.18.26` | **`1.18.34`** | Minor | Compatibilidad completa con procesadores de anotaciones en Java 17 y 21. |
| **Log4j Bridge (`log4j-to-slf4j`)** | `pom.xml` | `2.20.0` | **`2.24.1`** | Minor | Resolución de avisos de vulnerabilidad y mejoras de rendimiento en logging. |
| **MariaDB Java Client** | `maria-output-adapter/pom.xml` | `3.1.4` | **`3.3.3`** | Minor | Mayor compatibilidad de pipeline TLS y gestión eficiente de conexiones JDBC. |
| **SpringDoc OpenAPI UI** | `rest-input-adapter/pom.xml` | `1.7.0` | **`1.8.0`** | Patch (Final 1.x) | Última versión de la rama 1.x (OpenAPI 3) compatible con Spring Boot 2.x / `javax`. |
| **Imagen Docker MongoDB** | `docker-compose.yml` | `mongo:5.0` | **`mongo:7.0`** | Major (Motor BD) | Reemplazo del motor EOL (5.0) por el actual LTS (7.0), actualizando healthcheck a `mongosh`. |

---

## 4. Análisis de Impacto Técnico

### 4.1. Persistencia Relacional (MariaDB)
* El driver `3.3.3` es totalmente compatible hacia atrás con Hibernate 5.6 (manejado por Spring Boot `2.7.18`).
* Las entidades JPA (`PersonaEntity`, `TelefonoEntity`, `ProfesionEntity`, `EstudiosEntity`) continúan utilizando `javax.persistence.*` sin alteraciones.
* La integridad referencial y las claves foráneas en MariaDB (`3307:3306`) operan sin modificaciones en los scripts DDL y DML.

### 4.2. Persistencia NoSQL (MongoDB 7.0)
* En MongoDB 7.0, el ejecutable tradicional `mongo` fue reemplazado por la moderna consola `mongosh`.
* Se actualizó la prueba de salud (*healthcheck*) en `docker-compose.yml`:
  ```yaml
  healthcheck:
    test: ["CMD", "mongosh", "-u", "root", "-p", "root", "--authenticationDatabase", "admin", "--eval", "db.adminCommand('ping')"]
  ```
* Las colecciones, identificadores compuestos BSON y referencias cruzadas continúan respondiendo correctamente.

### 4.3. Validación de DTOs y Manejo Global de Excepciones
* Las anotaciones de `spring-boot-starter-validation` (`@NotNull`, `@NotBlank`, `@Pattern`, `@Size`) en los objetos Request operan bajo el estándar Bean Validation 2.0 (`javax.validation`).
* El componente `GlobalExceptionHandler` (`@RestControllerAdvice`) mantiene sus intercepciones de `MethodArgumentNotValidException`, `NoExistException`, `InvalidOptionException`, `DataIntegrityViolationException` y `MethodArgumentTypeMismatchException`.

---

## 5. Verificación y Resultados

### 5.1. Pruebas Unitarias Automatizadas (JUnit 5 + Mockito)
Se ejecutó la suite completa mediante `./run_unit_tests.sh` en el contenedor `maven:3.9.6-eclipse-temurin-17` con Java 17:
* **Módulos evaluados:** `common`, `domain`, `application`, `maria-output-adapter`, `mongo-output-adapter`, `rest-input-adapter`, `cli-input-adapter`.
* **Total de pruebas ejecutadas:** 54 pruebas unitarias.
* **Fallos / Errores:** 0.
* **Resultado:** **`BUILD SUCCESS`** en el reactor de Maven.

### 5.2. Pruebas de Integración y Endpoints REST
Se ejecutó la suite de integración desatendida `./test_endpoints.sh`:
* **Total de pruebas ejecutadas:** 34 pruebas HTTP/curl.
* **Endpoints CRUD:** 24/24 aprobados (6 operaciones x 4 entidades).
* **Endpoints de Subrecursos:** 8/8 aprobados (navegación de relaciones persona-teléfono, persona-estudios, teléfono-dueño, profesión-estudios para MariaDB y MongoDB).
* **Validación de DTOs y Errores:** Aprobado (código 400 con detalle de campos requeridos y código 404 para entidades inexistentes).
* **Tasa de éxito:** **100% de efectividad**.

---

## 6. Hoja de Ruta para Futura Migración (Opción B: Spring Boot 3.x)

Para cuando se determine viable una refactorización de código fuente a gran escala, la ruta de actualización a seguir consistirá en:
1. Reemplazar todas las dependencias `javax.persistence.*` por `jakarta.persistence.*`.
2. Reemplazar todas las dependencias `javax.validation.*` por `jakarta.validation.*`.
3. Migrar `springdoc-openapi-ui:1.8.0` a `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0+`.
4. En `cli-input-adapter`, reemplazar las dependencias JAXB legacy de `javax.xml.bind` por `jakarta.xml.bind:jakarta.xml.bind-api:4.x`.
5. Actualizar el property de Spring Boot en `pom.xml` a `3.3.x`.
