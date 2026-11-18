# Diagramas de Arquitectura — Modelo C4 (PersonAPP)

Este documento presenta la arquitectura de **PersonAPP** siguiendo los tres primeros niveles del **Modelo C4** (*Context, Containers, Components*).

---

## Nivel 1: Diagrama de Contexto (System Context)

Muestra a los usuarios que interactúan con el sistema PersonAPP y las bases de datos que lo soportan.

```mermaid
flowchart TB
    U1["Usuario / Cliente API\n(Navegador, Swagger UI, Postman)"]
    U2["Operador de Sistema\n(Terminal / Consola)"]

    SYS["PersonAPP\n(Sistema de Gestión de Personas,\nProfesiones, Teléfonos y Estudios)"]

    DB1[("MariaDB 10.6\n(Base de Datos Relacional SQL)")]
    DB2[("MongoDB 7.0\n(Base de Datos Documental NoSQL)")]

    U1 -- "Consume API REST\n[HTTP / JSON]" --> SYS
    U2 -- "Opera menú interactivo\n[stdin / stdout]" --> SYS
    SYS -- "Lee y escribe registros\n[JDBC / JPA]" --> DB1
    SYS -- "Lee y escribe documentos\n[Mongo Driver / BSON]" --> DB2
```

---

## Nivel 2: Diagrama de Contenedores (Containers)

Muestra los contenedores Docker ejecutables dentro de la red privada `personapp-net` definida en `docker-compose.yml`.

```mermaid
flowchart TB
    U1["Cliente Web / Swagger UI"]
    U2["Operador CLI"]

    subgraph NET ["Red Docker: personapp-net"]
        REST["Contenedor: rest_personapp\n[Spring Boot 2.7.18 / Java 17]\nPuertos: 3000 y 3005"]
        CLI["Contenedor: cli_personapp\n[Spring Boot CLI / Java 17]\nPerfil interactivo"]
        MARIA[("Contenedor: mariadb_personapp\n[MariaDB 10.6]\nPuerto: 3307 -> 3306")]
        MONGO[("Contenedor: mongo_personapp\n[MongoDB 7.0]\nPuerto: 27018 -> 27017")]
    end

    U1 -- "HTTP/JSON\n(Puerto 3000 / 3005)" --> REST
    U2 -- "docker compose run --rm cli" --> CLI

    REST -- "JPA / JDBC" --> MARIA
    REST -- "Spring Data Mongo" --> MONGO
    CLI -- "JPA / JDBC" --> MARIA
    CLI -- "Spring Data Mongo" --> MONGO
```

---

## Nivel 3: Diagrama de Componentes (Components — Arquitectura Hexagonal)

Muestra cómo se organizan los módulos Maven dentro de la aplicación y cómo se cumple el Principio de Inversión de Dependencias (DIP).

```mermaid
flowchart LR
    subgraph IN ["Adaptadores de Entrada (Driving)"]
        C1["rest-input-adapter\n(Controllers, DTOs, GlobalExceptionHandler)"]
        C2["cli-input-adapter\n(MenuPrincipal, Submenús)"]
    end

    subgraph CORE ["Núcleo Hexagonal (Core)"]
        PI["Puertos de Entrada\n(PersonInputPort, ProfessionInputPort,\nPhoneInputPort, StudyInputPort)"]
        UC["Casos de Uso\n(PersonUseCase, ProfessionUseCase,\nPhoneUseCase, StudyUseCase)"]
        DOM["Dominio Puro\n(Person, Profession, Phone, Study, Gender)"]
        PO["Puertos de Salida\n(PersonOutputPort, ProfessionOutputPort,\nPhoneOutputPort, StudyOutputPort)"]
    end

    subgraph OUT ["Adaptadores de Salida (Driven)"]
        M_AD["maria-output-adapter\n(Adapters, Entities, Repositories JPA)"]
        NO_AD["mongo-output-adapter\n(Adapters, Documents, Repositories Mongo)"]
    end

    C1 -- "invoca" --> PI
    C2 -- "invoca" --> PI
    UC -. "implementa" .-> PI
    UC -- "opera sobre" --> DOM
    UC -- "usa" --> PO
    M_AD -. "implementa" .-> PO
    NO_AD -. "implementa" .-> PO
```
