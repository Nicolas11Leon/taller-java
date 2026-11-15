package co.edu.javeriana.as.personapp.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {

	@NotNull(message = "El ID de la profesión (idProf) es obligatorio")
	@Pattern(regexp = "^[0-9]+$", message = "El ID de la profesión (idProf) debe ser numérico")
	private String idProf;

	@NotNull(message = "La cédula de la persona (ccPer) es obligatoria")
	@Pattern(regexp = "^[0-9]+$", message = "La cédula de la persona (ccPer) debe ser numérica")
	private String ccPer;

	@Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "La fecha (fecha) debe tener formato yyyy-MM-dd")
	private String fecha;

	@Size(max = 50, message = "El nombre de la universidad (univer) no puede exceder los 50 caracteres")
	private String univer;

	@Pattern(regexp = "^(?i)(MARIA|MONGO)?$", message = "La base de datos debe ser 'MARIA' o 'MONGO'")
	private String database;
}
