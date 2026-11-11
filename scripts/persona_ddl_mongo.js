// DDL para MongoDB
// Creación de usuario y colecciones con índices para persona_db

var adminDB = db.getSiblingDB("admin");

adminDB.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin", db: "persona_db" }
  ]
});

var personaDB = db.getSiblingDB("persona_db");

// Crear colecciones
personaDB.createCollection("persona");
personaDB.createCollection("profesion");
personaDB.createCollection("telefono");
personaDB.createCollection("estudios");

// Índices
personaDB.persona.createIndex({ "cc": 1 });
personaDB.profesion.createIndex({ "id": 1 });
personaDB.telefono.createIndex({ "num": 1 });
personaDB.estudios.createIndex({ "primaryPersona.$id": 1 });
personaDB.estudios.createIndex({ "primaryProfesion.$id": 1 });
