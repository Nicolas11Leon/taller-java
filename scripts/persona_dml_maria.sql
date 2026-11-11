-- DML para MariaDB / MySQL
-- Inserción de datos iniciales para la base de datos persona_db

USE `persona_db`;

-- Personas
INSERT INTO `persona_db`.`persona` (`cc`, `nombre`, `apellido`, `genero`, `edad`) VALUES
	(123456789, 'Pepe', 'Perez', 'M', 30),
	(987654321, 'Pepito', 'Perez', 'M', null),
	(321654987, 'Pepa', 'Juarez', 'F', 30),
	(147258369, 'Pepita', 'Juarez', 'F', 10),
	(963852741, 'Fede', 'Perez', 'M', 18)
ON DUPLICATE KEY UPDATE `nombre` = VALUES(`nombre`), `apellido` = VALUES(`apellido`), `genero` = VALUES(`genero`), `edad` = VALUES(`edad`);

-- Profesiones
INSERT INTO `persona_db`.`profesion` (`id`, `nom`, `des`) VALUES
	(1, 'Ingeniero de Sistemas', 'Desarrollo de software y arquitectura'),
	(2, 'Médico Cirujano', 'Medicina general y cirugía'),
	(3, 'Abogado', 'Derecho corporativo y penal'),
	(4, 'Diseñador Gráfico', 'Diseño visual y multimedia'),
	(5, 'Contador Público', 'Finanzas y auditoría')
ON DUPLICATE KEY UPDATE `nom` = VALUES(`nom`), `des` = VALUES(`des`);

-- Telefonos
INSERT INTO `persona_db`.`telefono` (`num`, `oper`, `duenio`) VALUES
	('3001234567', 'Claro', 123456789),
	('3109876543', 'Movistar', 123456789),
	('3204567890', 'Tigo', 321654987),
	('3156789012', 'WOM', 963852741),
	('3012345678', 'Claro', 987654321)
ON DUPLICATE KEY UPDATE `oper` = VALUES(`oper`), `duenio` = VALUES(`duenio`);

-- Estudios
INSERT INTO `persona_db`.`estudios` (`id_prof`, `cc_per`, `fecha`, `univer`) VALUES
	(1, 123456789, '2018-12-15', 'Pontificia Universidad Javeriana'),
	(2, 321654987, '2015-06-20', 'Universidad Nacional de Colombia'),
	(4, 963852741, '2022-11-30', 'Universidad de los Andes'),
	(3, 987654321, '2010-07-15', 'Universidad del Rosario')
ON DUPLICATE KEY UPDATE `fecha` = VALUES(`fecha`), `univer` = VALUES(`univer`);

COMMIT;
