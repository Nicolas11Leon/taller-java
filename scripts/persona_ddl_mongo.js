// DDL para MongoDB
// Creación de usuario administrador y colecciones con índices para persona_db

use admin;

db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "read", db: "persona_db" },
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin", db: "persona_db" }
  ],
  mechanisms: ["SCRAM-SHA-1", "SCRAM-SHA-256"]
});

use persona_db;

// Crear colecciones
db.createCollection("persona");
db.createCollection("profesion");
db.createCollection("telefono");
db.createCollection("estudios");

// Índices
db.persona.createIndex({ "cc": 1 });
db.profesion.createIndex({ "id": 1 });
db.telefono.createIndex({ "num": 1 });
db.estudios.createIndex({ "primaryPersona.$id": 1 });
db.estudios.createIndex({ "primaryProfesion.$id": 1 });
