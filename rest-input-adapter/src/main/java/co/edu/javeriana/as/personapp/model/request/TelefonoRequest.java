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
public class TelefonoRequest {

	@NotBlank(message = "El número telefónico (number) es obligatorio")
	@Size(max = 15, message = "El número telefónico no puede exceder los 15 caracteres")
	private String number;

	@NotBlank(message = "El operador telefónico (oper) es obligatorio")
	@Size(max = 45, message = "El operador no puede exceder los 45 caracteres")
	private String oper;

	@NotNull(message = "La cédula del dueño (ownerDni) es obligatoria")
	@Pattern(regexp = "^[0-9]+$", message = "La cédula del dueño (ownerDni) debe ser numérica")
	private String ownerDni;

	@Pattern(regexp = "^(?i)(MARIA|MONGO)?$", message = "La base de datos debe ser 'MARIA' o 'MONGO'")
	private String database;
}
