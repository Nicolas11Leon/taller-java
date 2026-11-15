package co.edu.javeriana.as.personapp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.response.ErrorResponse;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler exceptionHandler;

	@BeforeEach
	void setUp() {
		exceptionHandler = new GlobalExceptionHandler();
	}

	@Test
	@DisplayName("Debe manejar NoExistException retornando 404 NOT_FOUND")
	void testHandleNoExistException() {
		NoExistException ex = new NoExistException("Persona no encontrada");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoExistException(ex);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("NOT_FOUND", response.getBody().getStatus());
		assertEquals("Persona no encontrada", response.getBody().getMessage());
	}

	@Test
	@DisplayName("Debe manejar InvalidOptionException retornando 400 BAD_REQUEST")
	void testHandleInvalidOptionException() {
		InvalidOptionException ex = new InvalidOptionException("Base de datos no soportada");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidOptionException(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("BAD_REQUEST", response.getBody().getStatus());
		assertEquals("Base de datos no soportada", response.getBody().getMessage());
	}

	@Test
	@DisplayName("Debe manejar DataIntegrityViolationException retornando 409 CONFLICT")
	void testHandleDataIntegrityViolationException() {
		DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate entry");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(ex);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("DATA_INTEGRITY_VIOLATION", response.getBody().getStatus());
	}

	@Test
	@DisplayName("Debe manejar Exception no controlada retornando 500 INTERNAL_SERVER_ERROR")
	void testHandleGeneralException() {
		Exception ex = new RuntimeException("Error inesperado");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneralException(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getStatus());
	}
}
