# Credenciales, Conexiones y Datos Iniciales (MariaDB y MongoDB)

Este documento consolida todas las cuentas de acceso, puertos, cadenas de conexión, tablas/colecciones y registros precargados automáticamente en las bases de datos de **PersonAPP**.

---

## 1. MariaDB 10.6 (Motor Relacional SQL)

### Credenciales y Conexión
| Parámetro | Valor |
| :--- | :--- |
| **Contenedor Docker** | `mariadb_personapp` |
| **Puerto Host → Contenedor** | `3307` → `3306` |
| **Base de Datos (`MYSQL_DATABASE`)** | `persona_db` |
| **Usuario de Aplicación (`MYSQL_USER`)** | `persona_db` |
| **Contraseña Aplicación (`MYSQL_PASSWORD`)** | `persona_db` |
| **Contraseña Root (`MYSQL_ROOT_PASSWORD`)** | `root` |
| **JDBC URL (Local / Host)** | `jdbc:mariadb://localhost:3307/persona_db` |
| **JDBC URL (Red interna Docker)** | `jdbc:mariadb://mariadb:3306/persona_db` |

### Tablas Creadas (`scripts/persona_ddl_maria.sql`)
1. `persona` (`cc` PK, `nombre`, `apellido`, `genero`, `edad`)
2. `profesion` (`id` PK, `nom`, `des`)
3. `telefono` (`num` PK, `oper`, `duenio` FK → `persona.cc`)
4. `estudios` (`id_prof` PK/FK → `profesion.id`, `cc_per` PK/FK → `persona.cc`, `fecha`, `univer`)

---

## 2. MongoDB 7.0 (Motor Documental NoSQL)

### Credenciales y Conexión
| Parámetro | Valor |
| :--- | :--- |
| **Contenedor Docker** | `mongo_personapp` |
| **Puerto Host → Contenedor** | `27018` → `27017` |
| **Base de Datos** | `persona_db` |
| **Base de Autenticación** | `admin` |
| **Usuario de Aplicación** | `persona_db` (Roles: `readWrite`, `dbAdmin` sobre `persona_db`) |
| **Contraseña Aplicación** | `persona_db` |
| **Usuario Root (`MONGO_INITDB_ROOT_USERNAME`)** | `root` |
| **Contraseña Root (`MONGO_INITDB_ROOT_PASSWORD`)** | `root` |
| **URI (Local / Host)** | `mongodb://persona_db:persona_db@localhost:27018/persona_db?authSource=admin` |
| **URI (Red interna Docker)** | `mongodb://persona_db:persona_db@mongodb:27017/persona_db?authSource=admin` |

### Colecciones Creadas (`scripts/persona_ddl_mongo.js`)
1. `persona` (Índice en `cc`)
2. `profesion` (Índice en `id`)
3. `telefono` (Índice en `num`)
4. `estudios` (Índices en `primaryPersona.$id`, `primaryProfesion.$id`)

---

## 3. Registros Precargados (Datos Iniciales Idénticos en Ambas BDs)

### Personas (5 registros)
| `cc` | `nombre` | `apellido` | `genero` | `edad` |
| :--- | :--- | :--- | :--- | :--- |
| `123456789` | Pepe | Perez | M | `30` |
| `987654321` | Pepito | Perez | M | `null` |
| `321654987` | Pepa | Juarez | F | `30` |
| `147258369` | Pepita | Juarez | F | `10` |
| `963852741` | Fede | Perez | M | `18` |

### Profesiones (5 registros)
| `id` | `nom` | `des` |
| :--- | :--- | :--- |
| `1` | Ingeniero de Sistemas | Desarrollo de software y arquitectura |
| `2` | Médico Cirujano | Medicina general y cirugía |
| `3` | Abogado | Derecho corporativo y penal |
| `4` | Diseñador Gráfico | Diseño visual y multimedia |
| `5` | Contador Público | Finanzas y auditoría |

### Teléfonos (5 registros)
| `num` | `oper` | `duenio` (`cc`) |
| :--- | :--- | :--- |
| `3001234567` | Claro | `123456789` |
| `3109876543` | Movistar | `123456789` |
| `3204567890` | Tigo | `321654987` |
| `3156789012` | WOM | `963852741` |
| `3012345678` | Claro | `987654321` |

### Estudios (4 registros)
| `id_prof` | `cc_per` | `fecha` | `univer` |
| :--- | :--- | :--- | :--- |
| `1` | `123456789` | `2018-12-15` | Pontificia Universidad Javeriana |
| `2` | `321654987` | `2015-06-20` | Universidad Nacional de Colombia |
| `4` | `963852741` | `2022-11-30` | Universidad de los Andes |
| `3` | `987654321` | `2010-07-15` | Universidad del Rosario |
