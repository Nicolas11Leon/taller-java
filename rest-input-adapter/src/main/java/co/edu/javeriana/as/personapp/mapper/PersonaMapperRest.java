package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		if (request == null) return null;
		co.edu.javeriana.as.personapp.domain.Gender gender;
		String sex = request.getSex() != null ? request.getSex().trim().toUpperCase() : "";
		if ("F".equals(sex) || "FEMALE".equals(sex)) {
			gender = co.edu.javeriana.as.personapp.domain.Gender.FEMALE;
		} else if ("M".equals(sex) || "MALE".equals(sex)) {
			gender = co.edu.javeriana.as.personapp.domain.Gender.MALE;
		} else {
			gender = co.edu.javeriana.as.personapp.domain.Gender.OTHER;
		}
		Integer dni = null;
		if (request.getDni() != null && !request.getDni().trim().isEmpty()) {
			try {
				dni = Integer.parseInt(request.getDni().trim());
			} catch (NumberFormatException ignored) {}
		}
		Integer age = null;
		if (request.getAge() != null && !request.getAge().trim().isEmpty()) {
			try {
				age = Integer.parseInt(request.getAge().trim());
			} catch (NumberFormatException ignored) {}
		}
		return new Person(dni, request.getFirstName(), request.getLastName(), gender, age, new java.util.ArrayList<>(), new java.util.ArrayList<>());
	}
		
}
