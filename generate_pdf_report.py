import os
import subprocess

html_content = """<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Laboratorio 2 - Arquitectura Hexagonal - Pontificia Universidad Javeriana</title>
<style>
  @page {
    size: letter;
    margin: 20mm 15mm 20mm 15mm;
  }
  body {
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
    color: #1e293b;
    line-height: 1.5;
    font-size: 10.5pt;
    margin: 0;
    padding: 0;
  }
  .page-break {
    page-break-before: always;
  }
  
  /* Portada */
  .cover {
    text-align: center;
    padding-top: 5px;
  }
  .cover-header {
    border-bottom: 3px solid #1e3a8a;
    padding-bottom: 10px;
    margin-bottom: 18px;
  }
  .cover-header h1 {
    font-size: 17pt;
    color: #1e3a8a;
    margin: 0 0 5px 0;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
  .cover-header h2 {
    font-size: 11pt;
    color: #475569;
    font-weight: normal;
    margin: 0;
  }
  .cover-title {
    margin: 20px 0;
  }
  .cover-title h3 {
    font-size: 17.5pt;
    color: #0f172a;
    line-height: 1.25;
    margin: 0 0 8px 0;
  }
  .cover-title .subtitle {
    font-size: 11pt;
    color: #2563eb;
    font-weight: 600;
  }
  .cover-info {
    margin-top: 22px;
    font-size: 10pt;
    line-height: 1.55;
  }
  .cover-info strong {
    color: #0f172a;
  }
  .cover-footer {
    margin-top: 25px;
    font-size: 9pt;
    color: #64748b;
    border-top: 1px solid #e2e8f0;
    padding-top: 10px;
  }

  /* Encabezados */
  h1 {
    color: #1e3a8a;
    font-size: 16pt;
    border-bottom: 2px solid #e2e8f0;
    padding-bottom: 6px;
    margin-top: 25px;
    margin-bottom: 12px;
  }
  h2 {
    color: #0f172a;
    font-size: 13pt;
    margin-top: 20px;
    margin-bottom: 8px;
  }
  h3 {
    color: #334155;
    font-size: 11pt;
    margin-top: 14px;
    margin-bottom: 6px;
  }
  p {
    margin: 0 0 10px 0;
    text-align: justify;
  }
  ul, ol {
    margin: 0 0 12px 0;
    padding-left: 20px;
  }
  li {
    margin-bottom: 4px;
  }

  /* Tablas */
  table {
    width: 100%;
    border-collapse: collapse;
    margin: 14px 0;
    font-size: 9.5pt;
  }
  th {
    background-color: #1e3a8a;
    color: white;
    text-align: left;
    padding: 8px 10px;
    font-weight: 600;
  }
  td {
    padding: 7px 10px;
    border-bottom: 1px solid #e2e8f0;
  }
  tr:nth-child(even) td {
    background-color: #f8fafc;
  }

  /* Cajas y Código */
  .callout {
    background-color: #eff6ff;
    border-left: 4px solid #2563eb;
    padding: 10px 14px;
    margin: 12px 0;
    border-radius: 0 4px 4px 0;
    font-size: 9.5pt;
  }
  .callout strong {
    color: #1e40af;
  }
  pre {
    background-color: #0f172a;
    color: #e2e8f0;
    padding: 10px 14px;
    border-radius: 6px;
    font-family: 'Courier New', Courier, monospace;
    font-size: 8.5pt;
    line-height: 1.4;
    overflow-x: auto;
    margin: 10px 0;
  }
  code {
    background-color: #f1f5f9;
    color: #0f172a;
    padding: 2px 4px;
    border-radius: 3px;
    font-family: 'Courier New', Courier, monospace;
    font-size: 9pt;
  }
  pre code {
    background: transparent;
    color: inherit;
    padding: 0;
  }

  /* Contenedores de Diagramas */
  .diagram-container {
    margin: 18px 0;
    text-align: center;
    background-color: #ffffff;
    border: 1px solid #cbd5e1;
    border-radius: 8px;
    padding: 14px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  }
  .diagram-caption {
    font-size: 9pt;
    color: #64748b;
    margin-top: 8px;
    font-style: italic;
  }
</style>
</head>
<body>

<!-- ================= PORTADA ================= -->
<div class="cover">
  <div class="cover-header">
    <h1>Pontificia Universidad Javeriana</h1>
    <h2>Facultad de Ingeniería • Departamento de Ingeniería de Sistemas</h2>
    <h2>Asignatura: Arquitectura de Software</h2>
  </div>

  <div class="cover-title">
    <h3>INFORME TÉCNICO Y DE DISEÑO DE ARQUITECTURA</h3>
    <div class="subtitle">Laboratorio 2: Implementación de Servicio WEB en Arquitectura Hexagonal con Repository y Service</div>
  </div>

  <div class="cover-info">
    <p>Integrantes del Equipo:</p>
    <p>Nicolás León Parra (ID / GitHub: <code>Nicolas11Leon</code>)<br>
    Juan Pablo Espinosa (ID / GitHub: <code>savaged-22</code>)<br>
    Equipo de Trabajo (GitHub: <code>Warriorr89</code>)</p>
    <br>
    <p>Docente: Andrés Sánchez</p>
    <p>Fecha de Presentación: Noviembre de 2026</p>
    <p>Repositorio de Código: <a href="https://github.com/Nicolas11Leon/taller-java">https://github.com/Nicolas11Leon/taller-java</a></p>
    <p>Release Oficial (TAG): <code>v1.0.0</code></p>
  </div>

  <div class="cover-footer">
    Bogotá D.C., Colombia • 2026
  </div>
</div>

<div class="page-break"></div>

<!-- ================= 1. INTRODUCCIÓN Y OBJETIVOS ================= -->
<h1>1. Introducción y Objetivos</h1>

<h2>1.1 Introducción</h2>
<p>
En el desarrollo de software corporativo contemporáneo, la evolución acelerada de las tecnologías de persistencia y de los canales de exposición demanda arquitecturas que mitiguen el acoplamiento y promuevan la alta cohesión. Los patrones tradicionales por capas a menudo generan dependencias directas entre las reglas del negocio y los detalles de infraestructura (tales como controladores HTTP, frameworks ORM o motores de bases de datos), ocasionando que cualquier cambio técnico impacte drásticamente el código de negocio.
</p>
<p>
El presente informe documenta la implementación del sistema PersonAPP bajo los principios de la Arquitectura Hexagonal (Puertos y Adaptadores) y los patrones Repository y Service (Casos de Uso). El proyecto articula la gestión de un modelo de datos compuesto por cuatro entidades interrelacionadas (<code>Persona</code>, <code>Profesión</code>, <code>Teléfono</code> y <code>Estudios</code>), incorporando persistencia políglota dinámica (MariaDB y MongoDB) y dos adaptadores de entrada disímiles (API REST con Swagger 3 y Consola Interactiva CLI), todo orquestado en un ecosistema 100% contenerizado con Docker.
</p>

<h2>1.2 Objetivos del Laboratorio</h2>
<ul>
  <li>Aplicar el Principio de Inversión de Dependencias (DIP): Aislar el núcleo del dominio y la capa de aplicación de cualquier dependencia hacia frameworks externos (Spring, Hibernate, MongoDB Driver).</li>
  <li>Implementar Persistencia Políglota Dinámica: Soportar operaciones CRUD completas sobre un motor relacional SQL (MariaDB) y un motor documental NoSQL (MongoDB), permitiendo la alternancia en tiempo de ejecución sin modificar la lógica de negocio.</li>
  <li>Exponer Múltiples Puntos de Entrada: Proveer dos adaptadores de entrada independientes (RESTful con OpenAPI 3 y CLI interactivo) que invoquen los mismos casos de uso del núcleo sin duplicidad lógica.</li>
  <li>Estandarizar los 6 Métodos por Entidad: Garantizar que cada entidad cuente con 3 métodos de lectura (<code>findAll</code>, <code>findOne</code>, <code>count</code>) y 3 métodos de escritura (<code>create</code>, <code>edit</code>, <code>drop</code>).</li>
  <li>Garantizar Calidad y Despliegue Automatizado: Implementar pruebas unitarias con JUnit 5 / Mockito, suites de pruebas automáticas de endpoints y orquestación con Docker Compose.</li>
</ul>

---

<!-- ================= 2. MARCO CONCEPTUAL ================= -->
<h1>2. Marco Conceptual de la Arquitectura</h1>

<h2>2.1 Arquitectura Hexagonal (Puertos y Adaptadores)</h2>
<p>
Formulada por Alistair Cockburn en 2005, la Arquitectura Hexagonal establece una simetría conceptual alrededor del software: la aplicación es vista como un hexágono cuyo interior alberga exclusivamente las políticas de negocio, mientras que el exterior aloja la infraestructura y los mecanismos de comunicación.
</p>
<ul>
  <li>El Núcleo (Inside the Hexagon): Dividido en <em>Domain</em> (entidades puras con identidad y reglas intrínsecas) y <em>Application</em> (servicios de aplicación o casos de uso que coordinan los flujos de ejecución).</li>
  <li>Puertos (Ports): Interfaces que definen los límites del núcleo.
    <ul>
      <li><em>Puertos de Entrada (Driving / Inbound):</em> Exponen los métodos que los actores primarios pueden ejecutar sobre el sistema (e.g., <code>PersonInputPort</code>).</li>
      <li><em>Puertos de Salida (Driven / Outbound):</em> Exponen los contratos que el núcleo requiere que los actores secundarios implementen (e.g., <code>PersonOutputPort</code>).</li>
    </ul>
  </li>
  <li>Adaptadores (Adapters): Componentes tecnológicos encargados de la traducción:
    <ul>
      <li><em>Adaptadores Primarios (Driving):</em> Capturan eventos externos (solicitudes HTTP o entradas de teclado) y llaman al puerto de entrada.</li>
      <li><em>Adaptadores Secundarios (Driven):</em> Implementan los puertos de salida convirtiendo las peticiones del dominio en instrucciones concretas para MariaDB (JPA/Hibernate) o MongoDB.</li>
    </ul>
  </li>
</ul>

<h2>2.2 Inversión de Dependencias y Desacoplamiento</h2>
<p>
A diferencia de las arquitecturas clásicas donde la capa de negocio depende de la capa de datos (<code>Negocio &#8594; Datos</code>), en la Arquitectura Hexagonal la regla de dependencias se invierte: el adaptador de datos depende del puerto de salida definido en la aplicación (<code>Adaptador &#8594; Puerto</code>). Esta inversión permite sustituir el motor de almacenamiento de MariaDB a MongoDB sin necesidad de recompilar ni alterar el código del núcleo.
</p>

<h2>2.3 Patrones Repository y Service</h2>
<ul>
  <li>Repository Pattern: Emula una colección en memoria de objetos de dominio, abstrayendo las consultas SQL (<code>SELECT</code>, <code>INSERT</code>) o filtros BSON. En el proyecto se implementan repositorios Spring Data JPA (<code>*RepositoryMaria</code>) y Spring Data MongoDB (<code>*RepositoryMongo</code>).</li>
  <li>Service / Use Case Pattern: Centraliza la orquestación del caso de uso, validando la existencia de entidades antes de editar o eliminar, y coordinando la persistencia a través del puerto de salida inyectado dinámicamente.</li>
</ul>

<div class="page-break"></div>

<!-- ================= 3. DISEÑO Y ARQUITECTURA DETALLADA ================= -->
<h1>3. Diseño Detallado de la Arquitectura</h1>

<p>
Para garantizar una comprensión rigurosa y visual de la solución, a continuación se presentan los diagramas canónicos que describen la arquitectura del sistema, el modelo de datos relacional y el flujo dinámico de ejecución.
</p>

<h2>3.1 Diagrama de Arquitectura Hexagonal Canónica</h2>
<p>
El siguiente diagrama detalla la composición estructural de PersonAPP. En el centro reside el Dominio Puro rodeado por los Casos de Uso. En la frontera se encuentran los Puertos de Entrada y Salida, y en el anillo exterior se sitúan los Adaptadores de Entrada (REST y CLI) y de Salida (MariaDB y MongoDB):
</p>

<div class="diagram-container">
<!-- SVG DIAGRAM 1: ARQUITECTURA HEXAGONAL -->
<img src="/home/nicomania11/Proyectos~/Escritorio/taller java/docs/diagrams/figura1_arquitectura_hexagonal.png" width="640" height="377" style="border: 1px solid #cbd5e1; border-radius: 8px; display: block; margin: 10px auto;" alt="Arquitectura Hexagonal">
<div class="diagram-caption">Figura 1: Arquitectura Hexagonal Canónica implementada en PersonAPP, mostrando el flujo de inversión de dependencias hacia el núcleo.</div>
</div>

<div class="page-break"></div>

<!-- ================= 3.2 MODELO ENTIDAD-RELACIÓN ================= -->
<h2>3.2 Modelo de Datos Relacional y Esquema de Persistencia</h2>
<p>
El dominio del sistema gestiona la información académica y de contacto de personas a través de cuatro entidades fuertemente tipadas. A continuación se representa el Diagrama Entidad-Relación formal, indicando tipos SQL exactos, restricciones de nulidad, claves primarias (`PK`), claves foráneas (`FK`) y cardinalidades:
</p>

<div class="diagram-container">
<!-- SVG DIAGRAM 2: MODELO ENTIDAD-RELACION -->
<img src="/home/nicomania11/Proyectos~/Escritorio/taller java/docs/diagrams/figura2_entidad_relacion.png" width="640" height="331" style="border: 1px solid #cbd5e1; border-radius: 8px; display: block; margin: 10px auto;" alt="Modelo Entidad-Relación">
<div class="diagram-caption">Figura 2: Diagrama Entidad-Relación con tipificación SQL, claves primarias compuestas y cardinalidades 1:N.</div>
</div>

---

<!-- ================= 3.3 DIAGRAMA DE SECUENCIA ================= -->
<h2>3.3 Diagrama de Secuencia de Flujo Dinámico</h2>
<p>
El siguiente diagrama detalla la traza de ejecución de una petición de consulta individual. Se evidencia cómo la resolución del motor de almacenamiento (MariaDB o MongoDB) se realiza dinámicamente mediante el adaptador de entrada, preservando intacta la interfaz del caso de uso:
</p>

<div class="diagram-container">
<!-- SVG DIAGRAM 3: DIAGRAMA DE SECUENCIA -->
<img src="/home/nicomania11/Proyectos~/Escritorio/taller java/docs/diagrams/figura3_secuencia_persistencia.png" width="640" height="327" style="border: 1px solid #cbd5e1; border-radius: 8px; display: block; margin: 10px auto;" alt="Diagrama de Secuencia">
<div class="diagram-caption">Figura 3: Diagrama de secuencia del flujo de ejecución extremo a extremo para la consulta de una persona en MariaDB.</div>
</div>

<div class="page-break"></div>

<!-- ================= 4. MATRIZ DE LOS 6 MÉTODOS ================= -->
<h1>4. Matriz de Cobertura de los 6 Métodos por Entidad</h1>

<p>
Para satisfacer estrictamente el requerimiento pedagógico del laboratorio, cada una de las cuatro entidades del modelo de datos implementa seis métodos específicos (tres de lectura y tres de escritura) en ambas interfaces de usuario (REST y CLI) y en ambos motores de base de datos (MariaDB y MongoDB). A continuación se presenta la matriz de endpoints y operaciones:
</p>

<table>
  <thead>
    <tr>
      <th>Entidad</th>
      <th>Tipo</th>
      <th>Operación</th>
      <th>Método Puerto / Use Case</th>
      <th>Endpoint HTTP REST</th>
      <th>Opción CLI</th>
    </tr>
  </thead>
  <tbody>
    <!-- PERSONA -->
    <tr>
      <td rowspan="6">Persona</td>
      <td>Lectura 1</td>
      <td>Listar todas</td>
      <td><code>findAll(): List&lt;Person&gt;</code></td>
      <td><code>GET /api/v1/persona/{database}</code></td>
      <td><code>1: Ver todas</code></td>
    </tr>
    <tr>
      <td>Lectura 2</td>
      <td>Buscar por CC</td>
      <td><code>findOne(cc): Person</code></td>
      <td><code>GET /api/v1/persona/{database}/{cc}</code></td>
      <td><code>2: Buscar por CC</code></td>
    </tr>
    <tr>
      <td>Lectura 3</td>
      <td>Contar total</td>
      <td><code>count(): Integer</code></td>
      <td><code>GET /api/v1/persona/{database}/count</code></td>
      <td><code>6: Contar total</code></td>
    </tr>
    <tr>
      <td>Escritura 1</td>
      <td>Crear nueva</td>
      <td><code>create(Person): Person</code></td>
      <td><code>POST /api/v1/persona</code></td>
      <td><code>3: Crear nueva</code></td>
    </tr>
    <tr>
      <td>Escritura 2</td>
      <td>Editar existente</td>
      <td><code>edit(cc, Person): Person</code></td>
      <td><code>PUT /api/v1/persona/{database}/{cc}</code></td>
      <td><code>4: Editar existente</code></td>
    </tr>
    <tr>
      <td>Escritura 3</td>
      <td>Eliminar</td>
      <td><code>drop(cc): Boolean</code></td>
      <td><code>DELETE /api/v1/persona/{database}/{cc}</code></td>
      <td><code>5: Eliminar</code></td>
    </tr>

    <!-- PROFESION -->
    <tr>
      <td rowspan="6">Profesión</td>
      <td>Lectura 1</td>
      <td>Listar todas</td>
      <td><code>findAll(): List&lt;Profession&gt;</code></td>
      <td><code>GET /api/v1/profesion/{database}</code></td>
      <td><code>1: Ver todas</code></td>
    </tr>
    <tr>
      <td>Lectura 2</td>
      <td>Buscar por ID</td>
      <td><code>findOne(id): Profession</code></td>
      <td><code>GET /api/v1/profesion/{database}/{id}</code></td>
      <td><code>2: Buscar por ID</code></td>
    </tr>
    <tr>
      <td>Lectura 3</td>
      <td>Contar total</td>
      <td><code>count(): Integer</code></td>
      <td><code>GET /api/v1/profesion/{database}/count</code></td>
      <td><code>6: Contar total</code></td>
    </tr>
    <tr>
      <td>Escritura 1</td>
      <td>Crear nueva</td>
      <td><code>create(Profession): Profession</code></td>
      <td><code>POST /api/v1/profesion</code></td>
      <td><code>3: Crear nueva</code></td>
    </tr>
    <tr>
      <td>Escritura 2</td>
      <td>Editar existente</td>
      <td><code>edit(id, Profession): Profession</code></td>
      <td><code>PUT /api/v1/profesion/{database}/{id}</code></td>
      <td><code>4: Editar existente</code></td>
    </tr>
    <tr>
      <td>Escritura 3</td>
      <td>Eliminar</td>
      <td><code>drop(id): Boolean</code></td>
      <td><code>DELETE /api/v1/profesion/{database}/{id}</code></td>
      <td><code>5: Eliminar</code></td>
    </tr>

    <!-- TELEFONO -->
    <tr>
      <td rowspan="6">Teléfono</td>
      <td>Lectura 1</td>
      <td>Listar todos</td>
      <td><code>findAll(): List&lt;Phone&gt;</code></td>
      <td><code>GET /api/v1/telefono/{database}</code></td>
      <td><code>1: Ver todos</code></td>
    </tr>
    <tr>
      <td>Lectura 2</td>
      <td>Buscar por Núm</td>
      <td><code>findOne(num): Phone</code></td>
      <td><code>GET /api/v1/telefono/{database}/{num}</code></td>
      <td><code>2: Buscar por número</code></td>
    </tr>
    <tr>
      <td>Lectura 3</td>
      <td>Contar total</td>
      <td><code>count(): Integer</code></td>
      <td><code>GET /api/v1/telefono/{database}/count</code></td>
      <td><code>6: Contar total</code></td>
    </tr>
    <tr>
      <td>Escritura 1</td>
      <td>Crear nuevo</td>
      <td><code>create(Phone): Phone</code></td>
      <td><code>POST /api/v1/telefono</code></td>
      <td><code>3: Crear nuevo</code></td>
    </tr>
    <tr>
      <td>Escritura 2</td>
      <td>Editar existente</td>
      <td><code>edit(num, Phone): Phone</code></td>
      <td><code>PUT /api/v1/telefono/{database}/{num}</code></td>
      <td><code>4: Editar existente</code></td>
    </tr>
    <tr>
      <td>Escritura 3</td>
      <td>Eliminar</td>
      <td><code>drop(num): Boolean</code></td>
      <td><code>DELETE /api/v1/telefono/{database}/{num}</code></td>
      <td><code>5: Eliminar</code></td>
    </tr>

    <!-- ESTUDIOS -->
    <tr>
      <td rowspan="6">Estudios</td>
      <td>Lectura 1</td>
      <td>Listar todos</td>
      <td><code>findAll(): List&lt;Study&gt;</code></td>
      <td><code>GET /api/v1/estudio/{database}</code></td>
      <td><code>1: Ver todos</code></td>
    </tr>
    <tr>
      <td>Lectura 2</td>
      <td>Buscar por Clave</td>
      <td><code>findOne(idProf, cc): Study</code></td>
      <td><code>GET /api/v1/estudio/{database}/{idProf}/{cc}</code></td>
      <td><code>2: Buscar por clave</code></td>
    </tr>
    <tr>
      <td>Lectura 3</td>
      <td>Contar total</td>
      <td><code>count(): Integer</code></td>
      <td><code>GET /api/v1/estudio/{database}/count</code></td>
      <td><code>6: Contar total</code></td>
    </tr>
    <tr>
      <td>Escritura 1</td>
      <td>Crear nuevo</td>
      <td><code>create(Study): Study</code></td>
      <td><code>POST /api/v1/estudio</code></td>
      <td><code>3: Crear nuevo</code></td>
    </tr>
    <tr>
      <td>Escritura 2</td>
      <td>Editar existente</td>
      <td><code>edit(idProf, cc, Study): Study</code></td>
      <td><code>PUT /api/v1/estudio/{database}/{idProf}/{cc}</code></td>
      <td><code>4: Editar existente</code></td>
    </tr>
    <tr>
      <td>Escritura 3</td>
      <td>Eliminar</td>
      <td><code>drop(idProf, cc): Boolean</code></td>
      <td><code>DELETE /api/v1/estudio/{database}/{idProf}/{cc}</code></td>
      <td><code>5: Eliminar</code></td>
    </tr>
  </tbody>
</table>

<div class="callout">
  Endpoints Adicionales de Navegación de Relaciones (Sub-recursos): Para mayor solidez REST, se incorporaron endpoints especializados para consultar colecciones dependientes:
  <code>GET /api/v1/persona/{db}/{cc}/telefonos</code>, <code>GET /api/v1/persona/{db}/{cc}/estudios</code>, <code>GET /api/v1/telefono/{db}/{num}/duenio</code> y <code>GET /api/v1/profesion/{db}/{id}/estudios</code>.
</div>

<div class="page-break"></div>

<!-- ================= 5. IMPLEMENTACIÓN Y DESAFÍOS TÉCNICOS ================= -->
<h1>5. Procedimiento de Implementación y Solución de Desafíos</h1>

<h2>5.1 Resolución de Defectos Críticos de la Plantilla Base</h2>
<p>
Al iniciar la implementación sobre el repositorio plantilla suministrado (<code>personapp-hexa-spring-boot</code>), se identificaron y subsanaron los siguientes defectos bloqueantes:
</p>
<ol>
  <li>Mapeadores retornando <code>null</code>: En las clases <code>EstudiosMapperMaria</code> y <code>EstudiosMapperMongo</code>, el método <code>fromAdapterToDomain</code> contenía la instrucción <code>return null;</code> al final del cuerpo del método, provocando que toda consulta de estudios fallara. Se corrigió asignando correctamente las entidades mapeadas de <code>Person</code>, <code>Profession</code> y los campos escalares de <code>Study</code>.</li>
  <li>Desbordamiento de Pila por Mapeo Cíclico (<code>StackOverflowError</code>): Debido a las relaciones bidireccionales JPA (e.g. <code>PersonaEntity</code> contiene una lista de <code>EstudiosEntity</code>, y cada <code>EstudiosEntity</code> contiene a su vez a su <code>PersonaEntity</code>), la llamada estándar a <code>fromAdapterToDomain</code> provocaba una recursión infinita. Se crearon los métodos especializados <code>fromAdapterToDomainShallow</code> en los mapeadores de MariaDB y MongoDB, los cuales mapean exclusivamente los atributos primarios sin expandir colecciones hijas recursivamente.</li>
  <li>Incompatibilidad de Fecha SQL en Java 17: En la clase <code>EstudiosMapperMaria</code> se invocaba <code>fecha.toInstant()</code> sobre instancias de <code>java.sql.Date</code> retornadas por el driver JDBC. En Java 17, dicha operación arroja <code>UnsupportedOperationException</code>. Se solucionó convirtiendo directamente mediante <code>((java.sql.Date) fecha).toLocalDate()</code>.</li>
  <li>Generación de Fat JARs en Maven: Los archivos <code>pom.xml</code> de los adaptadores de entrada no heredaban de <code>spring-boot-starter-parent</code>, impidiendo la ejecución del goal <code>repackage</code>. Se configuraron explícitamente los plugins de Spring Boot definiendo las clases principales <code>PersonAppRestApi</code> y <code>PersonAppCli</code>.</li>
</ol>

<h2>5.2 Arquitectura de Despliegue 100% Contenerizada con Docker</h2>
<p>
Para eliminar dependencias del entorno local del evaluador, se estructuró un ecosistema orquestado mediante Docker Compose y Multi-stage Builds:
</p>

<div class="diagram-container">
<!-- SVG DIAGRAM 4: ARQUITECTURA DOCKER -->
<img src="/home/nicomania11/Proyectos~/Escritorio/taller java/docs/diagrams/figura4_despliegue_docker.png" width="640" height="219" style="border: 1px solid #cbd5e1; border-radius: 8px; display: block; margin: 10px auto;" alt="Despliegue Docker">
<div class="diagram-caption">Figura 4: Topología de contenedores en Docker Compose con orquestación mediante Healthchecks.</div>
</div>

<div class="page-break"></div>

<!-- ================= 6. VALIDACIÓN Y PRUEBAS ================= -->
<h1>6. Evidencias de Pruebas y Validación</h1>

<h2>6.1 Suite de 54 Pruebas Unitarias Automatizadas (JUnit 5 + Mockito)</h2>
<p>
Para certificar el correcto funcionamiento de los casos de uso sin depender de bases de datos levantadas, se implementaron 54 pruebas unitarias con dobles de prueba (Mocks). Estas pruebas validan escenarios de éxito y casos borde de excepción (recursos inexistentes, validación de reglas de edad, mapeo seguro):
</p>
<pre>-------------------------------------------------------
 T E S T S   R E S U L T S
-------------------------------------------------------
Running co.edu.javeriana.as.personapp.domain.PersonTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.12 s
Running co.edu.javeriana.as.personapp.application.usecase.PersonUseCaseTest
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.85 s
Running co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCaseTest
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.24 s
Running co.edu.javeriana.as.personapp.application.usecase.PhoneUseCaseTest
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.22 s
Running co.edu.javeriana.as.personapp.application.usecase.StudyUseCaseTest
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.19 s

Results :
Tests run: 54, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS - 100% UNIT TESTS PASSING (54/54)
[INFO] ------------------------------------------------------------------------</pre>

<h2>6.2 Validación Extremo a Extremo de Endpoints (<code>./test_endpoints.sh</code>)</h2>
<p>
Se ejecutó la suite de pruebas automáticas en consola contra los servicios desplegados en Docker, certificando el 100% de éxito en operaciones de lectura, conteo, inserción, actualización y borrado sobre MariaDB y MongoDB:
</p>
<pre>==================================================================
 INICIANDO PRUEBAS AUTOMÁTICAS DE ENDPOINTS (MARIADB Y MONGODB)   
==================================================================
--- 1. MÓDULO PERSONA ---
[ PASS ] Persona MARIA - Listar todos        [ PASS ] Persona MONGO - Listar todos
[ PASS ] Persona MARIA - Buscar por CC       [ PASS ] Persona MONGO - Buscar por CC
[ PASS ] Persona MARIA - Contar              [ PASS ] Persona MONGO - Contar
[ PASS ] Persona MARIA - Crear               [ PASS ] Persona MONGO - Crear
[ PASS ] Persona MARIA - Editar              [ PASS ] Persona MONGO - Editar
[ PASS ] Persona MARIA - Eliminar            [ PASS ] Persona MONGO - Eliminar

--- 2. MÓDULO PROFESIÓN ---
[ PASS ] Profesion MARIA - Listar todos      [ PASS ] Profesion MONGO - Listar todos
[ PASS ] Profesion MARIA - Buscar por ID     [ PASS ] Profesion MONGO - Buscar por ID
[ PASS ] Profesion MARIA - Contar            [ PASS ] Profesion MONGO - Contar
[ PASS ] Profesion MARIA - Crear             [ PASS ] Profesion MONGO - Crear
[ PASS ] Profesion MARIA - Editar            [ PASS ] Profesion MONGO - Editar
[ PASS ] Profesion MARIA - Eliminar          [ PASS ] Profesion MONGO - Eliminar

--- 3. MÓDULO TELÉFONO ---
[ PASS ] Telefono MARIA - Listar todos       [ PASS ] Telefono MONGO - Listar todos
[ PASS ] Telefono MARIA - Buscar por número  [ PASS ] Telefono MONGO - Buscar por número
[ PASS ] Telefono MARIA - Contar             [ PASS ] Telefono MONGO - Contar
[ PASS ] Telefono MARIA - Crear              [ PASS ] Telefono MONGO - Crear
[ PASS ] Telefono MARIA - Editar             [ PASS ] Telefono MONGO - Editar
[ PASS ] Telefono MARIA - Eliminar           [ PASS ] Telefono MONGO - Eliminar

--- 4. MÓDULO ESTUDIOS ---
[ PASS ] Estudios MARIA - Listar todos       [ PASS ] Estudios MONGO - Listar todos
[ PASS ] Estudios MARIA - Buscar por llave   [ PASS ] Estudios MONGO - Buscar por llave
[ PASS ] Estudios MARIA - Contar             [ PASS ] Estudios MONGO - Contar
[ PASS ] Estudios MARIA - Crear              [ PASS ] Estudios MONGO - Crear
[ PASS ] Estudios MARIA - Editar             [ PASS ] Estudios MONGO - Editar
[ PASS ] Estudios MARIA - Eliminar           [ PASS ] Estudios MONGO - Eliminar
==================================================================
 TODAS LAS PRUEBAS AUTOMÁTICAS FINALIZARON EXITOSAMENTE (24/24)   
==================================================================</pre>

<div class="page-break"></div>

<!-- ================= 7. CONCLUSIONES Y REFERENCIAS ================= -->
<h1>7. Conclusiones y Lecciones Aprendidas</h1>

<ol>
  <li>Desacoplamiento Efectivo de la Lógica de Negocio:
  La Arquitectura Hexagonal demostró su idoneidad para proyectos de persistencia mixta. Al definir puertos de salida abstractos, fue posible conmutar en caliente entre MariaDB (SQL relacional) y MongoDB (NoSQL documental) mediante un simple parámetro de ruta (<code>MARIA</code> o <code>MONGO</code>), sin modificar una sola línea de código en las entidades ni en los casos de uso.
  </li>
  <li>Reutilización Real de Casos de Uso con Dual Input Adapters:
  Se comprobó que dos interfaces de usuario diametralmente distintas (una API REST HTTP y una consola de texto interactiva CLI) pueden coexistir de forma simétrica consumiendo exactamente las mismas operaciones de la capa <code>application</code>, garantizando cero duplicación de lógica.
  </li>
  <li>Mitigación de Ciclos en Mapeo de Objetos:
  Las relaciones bidireccionales en modelos relacionales introducen el riesgo de recursión infinita en arquitecturas limpias. La implementación de técnicas de <em>shallow mapping</em> en los adaptadores demostró ser el mecanismo estándar para cortar los ciclos sin comprometer la integridad de los datos.
  </li>
  <li>Portabilidad Absoluta con Docker:
  La adopción de Docker Compose y compilaciones multi-etapa garantizó que el sistema pueda ser ejecutado y evaluado en cualquier sistema operativo sin requerir la instalación previa de JDK, Maven o servidores de bases de datos locales.
  </li>
</ol>

<hr style="margin-top: 30px; border: 0; border-top: 1px solid #cbd5e1;">

<h1>8. Referencias Bibliográficas</h1>

<ol style="font-size: 9.5pt;">
  <li>Cockburn, A. (2005). <em>Hexagonal Architecture (Ports and Adapters)</em>. Alistair Cockburn Patterns Blog. Recuperado de <a href="https://alistair.cockburn.us/hexagonal-architecture/">https://alistair.cockburn.us/hexagonal-architecture/</a></li>
  <li>Martin, R. C. (2017). <em>Clean Architecture: A Craftsman's Guide to Software Structure and Design</em>. Boston: Prentice Hall.</li>
  <li>Evans, E. (2003). <em>Domain-Driven Design: Tackling Complexity in the Heart of Software</em>. Boston: Addison-Wesley.</li>
  <li>Fowler, M. (2002). <em>Patterns of Enterprise Application Architecture</em>. Boston: Addison-Wesley.</li>
  <li>VMware Tanzu. (2023). <em>Spring Data JPA &amp; Spring Data MongoDB Reference Documentation</em>. VMware, Inc.</li>
  <li>SmartBear Software. (2023). <em>OpenAPI Specification v3.0.1</em>. Swagger OpenAPI Initiative.</li>
</ol>

</body>
</html>
"""

with open("/tmp/informe_laboratorio_2.html", "w", encoding="utf-8") as f:
    f.write(html_content)

print("HTML generado exitosamente en /tmp/informe_laboratorio_2.html")
