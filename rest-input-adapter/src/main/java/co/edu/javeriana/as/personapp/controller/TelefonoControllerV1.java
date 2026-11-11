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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.Response;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
@Tag(name = "Telefono", description = "Endpoints para la gestión de Teléfonos (MariaDB y MongoDB)")
public class TelefonoControllerV1 {

	@Autowired
	private TelefonoInputAdapterRest telefonoInputAdapterRest;

	@Operation(summary = "Obtener todos los teléfonos")
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TelefonoResponse>> telefonos(@PathVariable String database) {
		log.info("GET all telefonos for database: {}", database);
		return ResponseEntity.ok(telefonoInputAdapterRest.historial(database.toUpperCase()));
	}

	@Operation(summary = "Obtener un teléfono por número")
	@ResponseBody
	@GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> obtenerTelefono(@PathVariable String database, @PathVariable String number) {
		log.info("GET telefono {} for database: {}", number, database);
		TelefonoResponse response = telefonoInputAdapterRest.obtener(database.toUpperCase(), number);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("NOT_FOUND", "Teléfono no encontrado con número " + number, LocalDateTime.now()));
	}

	@Operation(summary = "Crear un nuevo teléfono")
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> crearTelefono(@RequestBody TelefonoRequest request) {
		log.info("POST crearTelefono");
		TelefonoResponse response = telefonoInputAdapterRest.crear(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("ERROR", "No se pudo crear el teléfono", LocalDateTime.now()));
	}

	@Operation(summary = "Actualizar un teléfono existente")
	@ResponseBody
	@PutMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificarTelefono(@PathVariable String database, @PathVariable String number,
			@RequestBody TelefonoRequest request) {
		log.info("PUT modificarTelefono {} in database {}", number, database);
		request.setDatabase(database.toUpperCase());
		TelefonoResponse response = telefonoInputAdapterRest.modificar(number, request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo actualizar el teléfono " + number, LocalDateTime.now()));
	}

	@Operation(summary = "Eliminar un teléfono")
	@ResponseBody
	@DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarTelefono(@PathVariable String database, @PathVariable String number) {
		log.info("DELETE eliminarTelefono {} in database {}", number, database);
		Boolean deleted = telefonoInputAdapterRest.eliminar(database.toUpperCase(), number);
		if (Boolean.TRUE.equals(deleted)) {
			return ResponseEntity.ok(new Response("DELETED", "Teléfono eliminado con éxito", LocalDateTime.now()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response("ERROR", "No se pudo eliminar el teléfono " + number, LocalDateTime.now()));
	}

	@Operation(summary = "Contar el total de teléfonos")
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> contarTelefonos(@PathVariable String database) {
		log.info("GET count telefonos for database: {}", database);
		return ResponseEntity.ok(telefonoInputAdapterRest.contar(database.toUpperCase()));
	}
}
