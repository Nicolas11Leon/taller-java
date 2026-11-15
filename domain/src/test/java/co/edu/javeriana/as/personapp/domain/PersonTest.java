package co.edu.javeriana.as.personapp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PersonTest {

	@Test
	@DisplayName("Debe validar correctamente si la edad es válida (positiva o cero)")
	void testIsValidAge() {
		Person person = new Person(123, "Carlos", "Gomez", Gender.MALE);
		person.setAge(25);
		assertTrue(person.isValidAge());

		person.setAge(0);
		assertTrue(person.isValidAge());

		person.setAge(-1);
		assertFalse(person.isValidAge());
	}

	@Test
	@DisplayName("Debe crear instancia con datos completos")
	void testPersonCreation() {
		Person person = new Person(12345, "Laura", "Martinez", Gender.FEMALE);
		person.setAge(22);

		assertNotNull(person);
		assertEquals(12345, person.getIdentification());
		assertEquals("Laura", person.getFirstName());
		assertEquals("Martinez", person.getLastName());
		assertEquals(Gender.FEMALE, person.getGender());
		assertEquals(22, person.getAge());
	}
}
