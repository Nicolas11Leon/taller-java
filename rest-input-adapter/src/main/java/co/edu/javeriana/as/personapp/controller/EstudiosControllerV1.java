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

import co.edu.javeriana.as.personapp.adapter.EstudiosInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
@Tag(name = "Estudio", description = "Endpoints para la gestión de Estudios (MariaDB y MongoDB)")
public class EstudiosControllerV1 {

	@Autowired
	private EstudiosInputAdapterRest estudiosInputAdapterRest;

	@Operation(summary = "Obtener todos los estudios")
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EstudioResponse>> estudios(@PathVariable String database) {
		log.info("GET all estudios for database: {}", database);
		return ResponseEntity.ok(estudiosInputAdapterRest.historial(database.toUpperCase()));
	}

	@Operation(summary = "Obtener un estudio por id_prof y cc_per")
	@ResponseBody
	@GetMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> obtenerEstudio(@PathVariable String database, @PathVariable Integer idProf,
			@PathVariable Integer ccPer) {
		log.info("GET estudio ({}, {}) for database: {}", idProf, ccPer, database);
		EstudioResponse response = estudiosInputAdapterRest.obtener(database.toUpperCase(), idProf, ccPer);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("NOT_FOUND",
				"Estudio no encontrado para profesión " + idProf + " y persona " + ccPer, LocalDateTime.now()));
	}

	@Operation(summary = "Crear un nuevo estudio")
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> crearEstudio(@RequestBody EstudioRequest request) {
		log.info("POST crearEstudio");
		EstudioResponse response = estudiosInputAdapterRest.crear(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("ERROR", "No se pudo crear el estudio", LocalDateTime.now()));
	}

	@Operation(summary = "Actualizar un estudio existente")
	@ResponseBody
	@PutMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificarEstudio(@PathVariable String database, @PathVariable Integer idProf,
			@PathVariable Integer ccPer, @RequestBody EstudioRequest request) {
		log.info("PUT modificarEstudio ({}, {}) in database {}", idProf, ccPer, database);
		request.setDatabase(database.toUpperCase());
		EstudioResponse response = estudiosInputAdapterRest.modificar(idProf, ccPer, request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("ERROR",
				"No se pudo actualizar el estudio para profesión " + idProf + " y persona " + ccPer, LocalDateTime.now()));
	}

	@Operation(summary = "Eliminar un estudio")
	@ResponseBody
	@DeleteMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarEstudio(@PathVariable String database, @PathVariable Integer idProf,
			@PathVariable Integer ccPer) {
		log.info("DELETE eliminarEstudio ({}, {}) in database {}", idProf, ccPer, database);
		Boolean deleted = estudiosInputAdapterRest.eliminar(database.toUpperCase(), idProf, ccPer);
		if (Boolean.TRUE.equals(deleted)) {
			return ResponseEntity.ok(new Response("DELETED", "Estudio eliminado con éxito", LocalDateTime.now()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("ERROR",
				"No se pudo eliminar el estudio para profesión " + idProf + " y persona " + ccPer, LocalDateTime.now()));
	}

	@Operation(summary = "Contar el total de estudios")
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> contarEstudios(@PathVariable String database) {
		log.info("GET count estudios for database: {}", database);
		return ResponseEntity.ok(estudiosInputAdapterRest.contar(database.toUpperCase()));
	}
}
