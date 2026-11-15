package co.edu.javeriana.as.personapp.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

class PersonaMapperRestTest {

	private PersonaMapperRest personaMapperRest;

	@BeforeEach
	void setUp() {
		personaMapperRest = new PersonaMapperRest();
	}

	@Test
	@DisplayName("Debe convertir correctamente de Domain a Adapter REST MariaDB")
	void testFromDomainToAdapterRestMaria() {
		Person person = new Person(123456789, "Pepe", "Perez", Gender.MALE);
		person.setAge(30);

		PersonaResponse response = personaMapperRest.fromDomainToAdapterRestMaria(person);

		assertNotNull(response);
		assertEquals("123456789", response.getDni());
		assertEquals("Pepe", response.getFirstName());
		assertEquals("Perez", response.getLastName());
		assertEquals("MALE", response.getSex());
		assertEquals("30", response.getAge());
		assertEquals("MariaDB", response.getDatabase());
		assertEquals("OK", response.getStatus());
	}

	@Test
	@DisplayName("Debe convertir correctamente de Domain a Adapter REST MongoDB")
	void testFromDomainToAdapterRestMongo() {
		Person person = new Person(987654321, "Maria", "Lopez", Gender.FEMALE);
		person.setAge(28);

		PersonaResponse response = personaMapperRest.fromDomainToAdapterRestMongo(person);

		assertNotNull(response);
		assertEquals("987654321", response.getDni());
		assertEquals("MongoDB", response.getDatabase());
		assertEquals("FEMALE", response.getSex());
	}

	@Test
	@DisplayName("Debe convertir correctamente de PersonaRequest a Domain Person")
	void testFromAdapterToDomain() {
		PersonaRequest request = new PersonaRequest("555555", "Diana", "Rojas", "24", "F", "MARIA");

		Person person = personaMapperRest.fromAdapterToDomain(request);

		assertNotNull(person);
		assertEquals(555555, person.getIdentification());
		assertEquals("Diana", person.getFirstName());
		assertEquals("Rojas", person.getLastName());
		assertEquals(Gender.FEMALE, person.getGender());
		assertEquals(24, person.getAge());
	}

	@Test
	@DisplayName("Debe retornar null cuando se pase null")
	void testNullHandling() {
		assertNull(personaMapperRest.fromDomainToAdapterRest(null, "MARIA"));
		assertNull(personaMapperRest.fromAdapterToDomain(null));
	}
}
