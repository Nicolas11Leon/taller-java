package co.edu.javeriana.as.personapp.terminal.mapper;

import java.util.ArrayList;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;

@Mapper
public class ProfesionMapperCli {

	public ProfesionModelCli fromDomainToAdapterCli(Profession profession) {
		if (profession == null) return null;
		ProfesionModelCli model = new ProfesionModelCli();
		model.setId(profession.getIdentification());
		model.setNom(profession.getName());
		model.setDes(profession.getDescription());
		return model;
	}

	public Profession fromAdapterCliToDomain(ProfesionModelCli model) {
		if (model == null) return null;
		return new Profession(model.getId(), model.getNom(), model.getDes(), new ArrayList<>());
	}
}
