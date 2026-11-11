// DML para MongoDB
// Inserción de datos iniciales en la base de datos persona_db

var db = db.getSiblingDB("persona_db");

// 1. Personas
db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false });

// 2. Profesiones
db.profesion.insertMany([
	{
		"_id": NumberInt(1),
		"nom": "Ingeniero de Sistemas",
		"des": "Desarrollo de software y arquitectura",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(2),
		"nom": "Médico Cirujano",
		"des": "Medicina general y cirugía",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(3),
		"nom": "Abogado",
		"des": "Derecho corporativo y penal",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(4),
		"nom": "Diseñador Gráfico",
		"des": "Diseño visual y multimedia",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(5),
		"nom": "Contador Público",
		"des": "Finanzas y auditoría",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	}
], { ordered: false });

// 3. Teléfonos
db.telefono.insertMany([
	{
		"_id": "3001234567",
		"oper": "Claro",
		"primaryDuenio": NumberInt(123456789),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3109876543",
		"oper": "Movistar",
		"primaryDuenio": NumberInt(123456789),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3204567890",
		"oper": "Tigo",
		"primaryDuenio": NumberInt(321654987),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3156789012",
		"oper": "WOM",
		"primaryDuenio": NumberInt(963852741),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3012345678",
		"oper": "Claro",
		"primaryDuenio": NumberInt(987654321),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	}
], { ordered: false });

// 4. Estudios
db.estudios.insertMany([
	{
		"_id": "123456789-1",
		"primaryPersona": NumberInt(123456789),
		"primaryProfesion": NumberInt(1),
		"fecha": ISODate("2018-12-15T00:00:00Z"),
		"univer": "Pontificia Universidad Javeriana",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"_id": "321654987-2",
		"primaryPersona": NumberInt(321654987),
		"primaryProfesion": NumberInt(2),
		"fecha": ISODate("2015-06-20T00:00:00Z"),
		"univer": "Universidad Nacional de Colombia",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"_id": "963852741-4",
		"primaryPersona": NumberInt(963852741),
		"primaryProfesion": NumberInt(4),
		"fecha": ISODate("2022-11-30T00:00:00Z"),
		"univer": "Universidad de los Andes",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"_id": "987654321-3",
		"primaryPersona": NumberInt(987654321),
		"primaryProfesion": NumberInt(3),
		"fecha": ISODate("2010-07-15T00:00:00Z"),
		"univer": "Universidad del Rosario",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	}
], { ordered: false });
