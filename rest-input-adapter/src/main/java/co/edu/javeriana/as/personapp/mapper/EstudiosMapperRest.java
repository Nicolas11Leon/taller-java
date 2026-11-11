package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudiosMapperRest {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, "MariaDB");
	}

	public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, "MongoDB");
	}

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		if (study == null) return null;
		String idProf = (study.getProfession() != null && study.getProfession().getIdentification() != null)
				? String.valueOf(study.getProfession().getIdentification()) : "";
		String ccPer = (study.getPerson() != null && study.getPerson().getIdentification() != null)
				? String.valueOf(study.getPerson().getIdentification()) : "";
		String fecha = study.getGraduationDate() != null ? study.getGraduationDate().format(FORMATTER) : "";
		return new EstudioResponse(
				idProf,
				ccPer,
				fecha,
				study.getUniversityName(),
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		if (request == null) return null;
		Person person = new Person();
		if (request.getCcPer() != null && !request.getCcPer().trim().isEmpty()) {
			try {
				person.setIdentification(Integer.parseInt(request.getCcPer().trim()));
			} catch (NumberFormatException ignored) {}
		}
		Profession profession = new Profession();
		if (request.getIdProf() != null && !request.getIdProf().trim().isEmpty()) {
			try {
				profession.setIdentification(Integer.parseInt(request.getIdProf().trim()));
			} catch (NumberFormatException ignored) {}
		}
		LocalDate graduationDate = null;
		if (request.getFecha() != null && !request.getFecha().trim().isEmpty()) {
			try {
				graduationDate = LocalDate.parse(request.getFecha().trim(), FORMATTER);
			} catch (Exception ignored) {}
		}
		return new Study(person, profession, graduationDate, request.getUniver());
	}
}
