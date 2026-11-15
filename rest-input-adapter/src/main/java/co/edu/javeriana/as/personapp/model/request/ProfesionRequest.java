package co.edu.javeriana.as.personapp.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionRequest {

	@NotNull(message = "El identificador (id) es obligatorio")
	@Pattern(regexp = "^[0-9]+$", message = "El identificador (id) debe ser numérico")
	private String id;

	@NotBlank(message = "El nombre (name) de la profesión es obligatorio")
	@Size(max = 90, message = "El nombre de la profesión no puede exceder los 90 caracteres")
	private String name;

	@Size(max = 65535, message = "La descripción no puede exceder el límite permitido")
	private String description;

	@Pattern(regexp = "^(?i)(MARIA|MONGO)?$", message = "La base de datos debe ser 'MARIA' o 'MONGO'")
	private String database;
}
