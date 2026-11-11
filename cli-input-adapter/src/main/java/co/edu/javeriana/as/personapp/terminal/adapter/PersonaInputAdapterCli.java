package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	private PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Persona in Input Adapter CLI");
		List<PersonaModelCli> personas = personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		if (personas.isEmpty()) {
			System.out.println("No hay personas registradas.");
		} else {
			personas.forEach(System.out::println);
		}
	}

	public void obtenerPersona(Integer cc) {
		try {
			Person person = personInputPort.findOne(cc);
			PersonaModelCli model = personaMapperCli.fromDomainToAdapterCli(person);
			System.out.println("Persona encontrada: " + model);
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void crearPersona(PersonaModelCli model) {
		Person person = personaMapperCli.fromAdapterCliToDomain(model);
		Person created = personInputPort.create(person);
		System.out.println("Persona creada exitosamente: " + personaMapperCli.fromDomainToAdapterCli(created));
	}

	public void modificarPersona(Integer cc, PersonaModelCli model) {
		try {
			Person person = personaMapperCli.fromAdapterCliToDomain(model);
			person.setIdentification(cc);
			Person edited = personInputPort.edit(cc, person);
			System.out.println("Persona modificada exitosamente: " + personaMapperCli.fromDomainToAdapterCli(edited));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminarPersona(Integer cc) {
		try {
			Boolean dropped = personInputPort.drop(cc);
			if (Boolean.TRUE.equals(dropped)) {
				System.out.println("Persona con cc " + cc + " eliminada exitosamente.");
			} else {
				System.out.println("No se pudo eliminar la persona con cc " + cc);
			}
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de personas registradas: " + personInputPort.count());
	}
}
