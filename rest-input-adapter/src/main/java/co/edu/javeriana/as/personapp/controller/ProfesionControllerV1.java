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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
@Tag(name = "Profesion", description = "Endpoints para la gestión de Profesiones (MariaDB y MongoDB)")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@Operation(summary = "Obtener todas las profesiones")
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProfesionResponse>> profesiones(@PathVariable String database) {
		log.info("GET all profesiones for database: {}", database);
		return ResponseEntity.ok(profesionInputAdapterRest.historial(database.toUpperCase()));
	}

	@Operation(summary = "Obtener una profesión por ID")
	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> obtenerProfesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("GET profesion {} for database: {}", id, database);
		ProfesionResponse response = profesionInputAdapterRest.obtener(database.toUpperCase(), id);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("NOT_FOUND", "Profesión no encontrada con id " + id, LocalDateTime.now()));
	}

	@Operation(summary = "Crear una nueva profesión")
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("POST crearProfesion");
		ProfesionResponse response = profesionInputAdapterRest.crear(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("ERROR", "No se pudo crear la profesión", LocalDateTime.now()));
	}

	@Operation(summary = "Actualizar una profesión existente")
	@ResponseBody
	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificarProfesion(@PathVariable String database, @PathVariable Integer id,
			@RequestBody ProfesionRequest request) {
		log.info("PUT modificarProfesion {} in database {}", id, database);
		request.setDatabase(database.toUpperCase());
		ProfesionResponse response = profesionInputAdapterRest.modificar(id, request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo actualizar la profesión con id " + id, LocalDateTime.now()));
	}

	@Operation(summary = "Eliminar una profesión")
	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarProfesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("DELETE eliminarProfesion {} in database {}", id, database);
		Boolean deleted = profesionInputAdapterRest.eliminar(database.toUpperCase(), id);
		if (Boolean.TRUE.equals(deleted)) {
			return ResponseEntity.ok(new Response("DELETED", "Profesión eliminada con éxito", LocalDateTime.now()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo eliminar la profesión con id " + id, LocalDateTime.now()));
	}

	@Operation(summary = "Contar el total de profesiones")
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> contarProfesiones(@PathVariable String database) {
		log.info("GET count profesiones for database: {}", database);
		return ResponseEntity.ok(profesionInputAdapterRest.contar(database.toUpperCase()));
	}
}
