package co.edu.javeriana.as.personapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
@Tag(name = "Persona", description = "Endpoints para la gestión de Personas (MariaDB y MongoDB)")
public class PersonaControllerV1 {

	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;

	@Operation(summary = "Obtener todas las personas")
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonaResponse>> personas(@PathVariable String database) {
		log.info("GET all personas for database: {}", database);
		return ResponseEntity.ok(personaInputAdapterRest.historial(database.toUpperCase()));
	}

	@Operation(summary = "Obtener una persona por cédula")
	@ResponseBody
	@GetMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> obtenerPersona(@PathVariable String database, @PathVariable Integer cc) {
		log.info("GET persona {} for database: {}", cc, database);
		PersonaResponse response = personaInputAdapterRest.obtenerPersona(database.toUpperCase(), cc);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("NOT_FOUND", "Persona no encontrada con cc " + cc, LocalDateTime.now()));
	}

	@Operation(summary = "Crear una nueva persona")
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> crearPersona(@RequestBody PersonaRequest request) {
		log.info("POST crearPersona");
		PersonaResponse response = personaInputAdapterRest.crearPersona(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("ERROR", "No se pudo crear la persona", LocalDateTime.now()));
	}

	@Operation(summary = "Actualizar una persona existente")
	@ResponseBody
	@PutMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificarPersona(@PathVariable String database, @PathVariable Integer cc,
			@RequestBody PersonaRequest request) {
		log.info("PUT modificarPersona {} in database {}", cc, database);
		request.setDatabase(database.toUpperCase());
		PersonaResponse response = personaInputAdapterRest.modificarPersona(cc, request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo actualizar la persona con cc " + cc, LocalDateTime.now()));
	}

	@Operation(summary = "Eliminar una persona")
	@ResponseBody
	@DeleteMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarPersona(@PathVariable String database, @PathVariable Integer cc) {
		log.info("DELETE eliminarPersona {} in database {}", cc, database);
		Boolean deleted = personaInputAdapterRest.eliminarPersona(database.toUpperCase(), cc);
		if (Boolean.TRUE.equals(deleted)) {
			return ResponseEntity.ok(new Response("DELETED", "Persona eliminada con éxito", LocalDateTime.now()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo eliminar la persona con cc " + cc, LocalDateTime.now()));
	}

	@Operation(summary = "Contar el total de personas")
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> contarPersonas(@PathVariable String database) {
		log.info("GET count personas for database: {}", database);
		return ResponseEntity.ok(personaInputAdapterRest.contar(database.toUpperCase()));
	}
}
