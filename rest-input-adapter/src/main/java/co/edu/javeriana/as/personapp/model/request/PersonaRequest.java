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
public class PersonaRequest {

	@NotNull(message = "La cédula (dni) es obligatoria")
	@Pattern(regexp = "^[0-9]+$", message = "La cédula (dni) debe ser un número entero positivo")
	private String dni;

	@NotBlank(message = "El nombre (firstName) es obligatorio")
	@Size(max = 45, message = "El nombre no puede exceder los 45 caracteres")
	private String firstName;

	@NotBlank(message = "El apellido (lastName) es obligatorio")
	@Size(max = 45, message = "El apellido no puede exceder los 45 caracteres")
	private String lastName;

	@Pattern(regexp = "^[0-9]*$", message = "La edad (age) debe ser un número entero positivo")
	private String age;

	@NotBlank(message = "El género (sex) es obligatorio")
	@Pattern(regexp = "^(?i)[MF]$", message = "El género (sex) debe ser 'M' o 'F'")
	private String sex;

	@Pattern(regexp = "^(?i)(MARIA|MONGO)?$", message = "La base de datos debe ser 'MARIA' o 'MONGO'")
	private String database;
}
