# Diagramas de Flujo por Caso de Uso — PersonAPP

Este documento presenta los diagramas de flujo detallados para el enrutamiento hexagonal dinámico y cada uno de los cuatro Casos de Uso del núcleo de la aplicación (`PersonUseCase`, `ProfessionUseCase`, `PhoneUseCase` y `StudyUseCase`).

---

## 1. Flujo General de Selección Dinámica de Base de Datos (Adaptador de Entrada)

Aplica a **todos** los casos de uso antes de invocar cualquier operación del núcleo:

```mermaid
flowchart TD
    A(["Inicio: Petición HTTP / Opción CLI"]) --> B["Leer parámetro database (MARIA o MONGO)"]
    B --> C{"¿database == MARIA?"}
    C -- "Sí" --> D["Inyectar OutputAdapterMaria en UseCase"]
    C -- "No" --> E{"¿database == MONGO?"}
    E -- "Sí" --> F["Inyectar OutputAdapterMongo en UseCase"]
    E -- "No" --> G["Lanzar InvalidOptionException (400 Bad Request)"]
    D --> H["Ejecutar método del InputPort (UseCase)"]
    F --> H
    H --> I(["Retornar DTO / Respuesta al Cliente"])
    G --> I
```

---

## 2. Caso de Uso: Persona (`PersonUseCase`)

```mermaid
flowchart TD
    Start(["PersonUseCase"]) --> Op{"Operación solicitada"}

    %% CREATE
    Op -- "create(person)" --> C1["Validar DTO (@Valid) y regla de edad"]
    C1 --> C2["personOutputPort.save(person)"]
    C2 --> C3(["Retornar Person creada (201/200)"])

    %% EDIT
    Op -- "edit(cc, person)" --> E1["personOutputPort.findById(cc)"]
    E1 --> E2{"¿Existe la persona?"}
    E2 -- "No" --> E3["Lanzar NoexistException (404)"]
    E2 -- "Sí" --> E4["personOutputPort.save(person)"]
    E4 --> E5(["Retornar Person actualizada"])

    %% DROP
    Op -- "drop(cc)" --> D1["personOutputPort.findById(cc)"]
    D1 --> D2{"¿Existe la persona?"}
    D2 -- "No" --> D3["Lanzar NoexistException (404)"]
    D2 -- "Sí" --> D4["personOutputPort.delete(cc)"]
    D4 --> D5(["Retornar true (Eliminada)"])

    %% FIND ONE
    Op -- "findOne(cc)" --> F1["personOutputPort.findById(cc)"]
    F1 --> F2{"¿Encontrada?"}
    F2 -- "No" --> F3["Lanzar NoexistException (404)"]
    F2 -- "Sí" --> F4(["Retornar Person"])

    %% FIND ALL & COUNT
    Op -- "findAll()" --> FA1["personOutputPort.find()"]
    FA1 --> FA2(["Retornar List<Person>"])
    Op -- "count()" --> CT1["personOutputPort.find().size()"]
    CT1 --> CT2(["Retornar Integer"])
```

---

## 3. Caso de Uso: Profesión (`ProfessionUseCase`)

```mermaid
flowchart TD
    Start(["ProfessionUseCase"]) --> Op{"Operación solicitada"}

    Op -- "create(profession)" --> C1["professionOutputPort.save(profession)"]
    C1 --> C2(["Retornar Profession creada"])

    Op -- "edit(id, profession)" --> E1["professionOutputPort.findById(id)"]
    E1 --> E2{"¿Existe?"}
    E2 -- "No" --> E3["Lanzar NoexistException (404)"]
    E2 -- "Sí" --> E4["professionOutputPort.save(profession)"]
    E4 --> E5(["Retornar Profession actualizada"])

    Op -- "drop(id)" --> D1["professionOutputPort.findById(id)"]
    D1 --> D2{"¿Existe?"}
    D2 -- "No" --> D3["Lanzar NoexistException (404)"]
    D2 -- "Sí" --> D4["professionOutputPort.delete(id)"]
    D4 --> D5(["Retornar true"])

    Op -- "findOne(id)" --> F1["professionOutputPort.findById(id)"]
    F1 --> F2{"¿Encontrada?"}
    F2 -- "No" --> F3["Lanzar NoexistException (404)"]
    F2 -- "Sí" --> F4(["Retornar Profession"])

    Op -- "findAll() / count()" --> FA1["professionOutputPort.find()"]
    FA1 --> FA2(["Retornar Lista o Conteo"])
```

---

## 4. Caso de Uso: Teléfono (`PhoneUseCase`)

```mermaid
flowchart TD
    Start(["PhoneUseCase"]) --> Op{"Operación solicitada"}

    Op -- "create(phone)" --> C1["Validar número, operador y dueño (cc)"]
    C1 --> C2["phoneOutputPort.save(phone)"]
    C2 --> C3(["Retornar Phone creado"])

    Op -- "edit(num, phone)" --> E1["phoneOutputPort.findById(num)"]
    E1 --> E2{"¿Existe el número?"}
    E2 -- "No" --> E3["Lanzar NoexistException (404)"]
    E2 -- "Sí" --> E4["phoneOutputPort.save(phone)"]
    E4 --> E5(["Retornar Phone actualizado"])

    Op -- "drop(num)" --> D1["phoneOutputPort.findById(num)"]
    D1 --> D2{"¿Existe el número?"}
    D2 -- "No" --> D3["Lanzar NoexistException (404)"]
    D2 -- "Sí" --> D4["phoneOutputPort.delete(num)"]
    D4 --> D5(["Retornar true"])

    Op -- "findOne(num)" --> F1["phoneOutputPort.findById(num)"]
    F1 --> F2{"¿Encontrado?"}
    F2 -- "No" --> F3["Lanzar NoexistException (404)"]
    F2 -- "Sí" --> F4(["Retornar Phone"])

    Op -- "findAll() / count()" --> FA1["phoneOutputPort.find()"]
    FA1 --> FA2(["Retornar Lista o Conteo"])
```

---

## 5. Caso de Uso: Estudios (`StudyUseCase`)

```mermaid
flowchart TD
    Start(["StudyUseCase"]) --> Op{"Operación solicitada"}

    Op -- "create(study)" --> C1["Validar llave compuesta (id_prof + cc_per), fecha y universidad"]
    C1 --> C2["studyOutputPort.save(study)"]
    C2 --> C3(["Retornar Study creado"])

    Op -- "edit(idProf, ccPer, study)" --> E1["studyOutputPort.findById(idProf, ccPer)"]
    E1 --> E2{"¿Existe el registro?"}
    E2 -- "No" --> E3["Lanzar NoexistException (404)"]
    E2 -- "Sí" --> E4["studyOutputPort.save(study)"]
    E4 --> E5(["Retornar Study actualizado"])

    Op -- "drop(idProf, ccPer)" --> D1["studyOutputPort.findById(idProf, ccPer)"]
    D1 --> D2{"¿Existe el registro?"}
    D2 -- "No" --> D3["Lanzar NoexistException (404)"]
    D2 -- "Sí" --> D4["studyOutputPort.delete(idProf, ccPer)"]
    D4 --> D5(["Retornar true"])

    Op -- "findOne(idProf, ccPer)" --> F1["studyOutputPort.findById(idProf, ccPer)"]
    F1 --> F2{"¿Encontrado?"}
    F2 -- "No" --> F3["Lanzar NoexistException (404)"]
    F2 -- "Sí" --> F4(["Retornar Study"])

    Op -- "findAll() / count()" --> FA1["studyOutputPort.find()"]
    FA1 --> FA2(["Retornar Lista o Conteo"])
```
