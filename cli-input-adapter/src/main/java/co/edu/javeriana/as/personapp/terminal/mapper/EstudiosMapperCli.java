package co.edu.javeriana.as.personapp.terminal.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;

@Mapper
public class EstudiosMapperCli {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public EstudiosModelCli fromDomainToAdapterCli(Study study) {
		if (study == null) return null;
		EstudiosModelCli model = new EstudiosModelCli();
		model.setIdProf(study.getProfession() != null ? study.getProfession().getIdentification() : null);
		model.setCcPer(study.getPerson() != null ? study.getPerson().getIdentification() : null);
		model.setFecha(study.getGraduationDate() != null ? study.getGraduationDate().format(FORMATTER) : null);
		model.setUniver(study.getUniversityName());
		return model;
	}

	public Study fromAdapterCliToDomain(EstudiosModelCli model) {
		if (model == null) return null;
		Person person = new Person();
		person.setIdentification(model.getCcPer());
		Profession profession = new Profession();
		profession.setIdentification(model.getIdProf());
		LocalDate fecha = null;
		if (model.getFecha() != null && !model.getFecha().trim().isEmpty()) {
			try {
				fecha = LocalDate.parse(model.getFecha().trim(), FORMATTER);
			} catch (Exception ignored) {}
		}
		return new Study(person, profession, fecha, model.getUniver());
	}
}
