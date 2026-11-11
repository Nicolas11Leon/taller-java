package co.edu.javeriana.as.personapp.terminal.mapper;

import java.util.ArrayList;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PersonaMapperCli {

	public PersonaModelCli fromDomainToAdapterCli(Person person) {
		if (person == null) return null;
		PersonaModelCli personaModelCli = new PersonaModelCli();
		personaModelCli.setCc(person.getIdentification());
		personaModelCli.setNombre(person.getFirstName());
		personaModelCli.setApellido(person.getLastName());
		personaModelCli.setGenero(person.getGender() != null ? person.getGender().toString() : "");
		personaModelCli.setEdad(person.getAge());
		return personaModelCli;
	}

	public Person fromAdapterCliToDomain(PersonaModelCli model) {
		if (model == null) return null;
		Gender gender = "F".equalsIgnoreCase(model.getGenero()) ? Gender.FEMALE
				: "M".equalsIgnoreCase(model.getGenero()) ? Gender.MALE : Gender.OTHER;
		return new Person(model.getCc(), model.getNombre(), model.getApellido(), gender, model.getEdad(),
				new ArrayList<>(), new ArrayList<>());
	}
}
