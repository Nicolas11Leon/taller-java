package co.edu.javeriana.as.personapp.mapper;

import java.util.ArrayList;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {

	public ProfesionResponse fromDomainToAdapterRestMaria(Profession profession) {
		return fromDomainToAdapterRest(profession, "MariaDB");
	}

	public ProfesionResponse fromDomainToAdapterRestMongo(Profession profession) {
		return fromDomainToAdapterRest(profession, "MongoDB");
	}

	public ProfesionResponse fromDomainToAdapterRest(Profession profession, String database) {
		if (profession == null) return null;
		return new ProfesionResponse(
				String.valueOf(profession.getIdentification()),
				profession.getName(),
				profession.getDescription(),
				database,
				"OK");
	}

	public Profession fromAdapterToDomain(ProfesionRequest request) {
		if (request == null) return null;
		Integer id = null;
		if (request.getId() != null && !request.getId().trim().isEmpty()) {
			try {
				id = Integer.parseInt(request.getId().trim());
			} catch (NumberFormatException ignored) {}
		}
		return new Profession(id, request.getName(), request.getDescription(), new ArrayList<>());
	}
}
