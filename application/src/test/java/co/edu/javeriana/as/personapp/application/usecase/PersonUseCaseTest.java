package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

	@Mock
	private PersonOutputPort personPersistence;

	private PersonUseCase personUseCase;

	private Person testPerson;

	@BeforeEach
	void setUp() {
		personUseCase = new PersonUseCase(personPersistence);
		testPerson = new Person(123456789, "Pepe", "Perez", Gender.MALE, 30, new ArrayList<>(), new ArrayList<>());
	}

	@Test
	@DisplayName("Debe crear una persona exitosamente")
	void testCreatePerson() {
		when(personPersistence.save(any(Person.class))).thenReturn(testPerson);

		Person created = personUseCase.create(testPerson);

		assertNotNull(created);
		assertEquals(123456789, created.getIdentification());
		assertEquals("Pepe", created.getFirstName());
		verify(personPersistence).save(testPerson);
	}

	@Test
	@DisplayName("Debe editar una persona existente exitosamente")
	void testEditPersonSuccess() throws NoExistException {
		Person updated = new Person(123456789, "Pepe Modificado", "Perez", Gender.MALE, 31, new ArrayList<>(), new ArrayList<>());
		when(personPersistence.findById(123456789)).thenReturn(testPerson);
		when(personPersistence.save(updated)).thenReturn(updated);

		Person result = personUseCase.edit(123456789, updated);

		assertNotNull(result);
		assertEquals("Pepe Modificado", result.getFirstName());
		verify(personPersistence).save(updated);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar editar una persona que no existe")
	void testEditPersonNotFound() {
		when(personPersistence.findById(999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.edit(999, testPerson));
	}

	@Test
	@DisplayName("Debe eliminar una persona existente exitosamente")
	void testDropPersonSuccess() throws NoExistException {
		when(personPersistence.findById(123456789)).thenReturn(testPerson);
		when(personPersistence.delete(123456789)).thenReturn(true);

		Boolean deleted = personUseCase.drop(123456789);

		assertTrue(deleted);
		verify(personPersistence).delete(123456789);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar eliminar una persona que no existe")
	void testDropPersonNotFound() {
		when(personPersistence.findById(999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.drop(999));
	}

	@Test
	@DisplayName("Debe listar todas las personas")
	void testFindAll() {
		when(personPersistence.find()).thenReturn(Arrays.asList(testPerson));

		List<Person> list = personUseCase.findAll();

		assertEquals(1, list.size());
		assertEquals("Pepe", list.get(0).getFirstName());
	}

	@Test
	@DisplayName("Debe buscar una persona por su cédula")
	void testFindOneSuccess() throws NoExistException {
		when(personPersistence.findById(123456789)).thenReturn(testPerson);

		Person found = personUseCase.findOne(123456789);

		assertNotNull(found);
		assertEquals(123456789, found.getIdentification());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al buscar una persona inexistente")
	void testFindOneNotFound() {
		when(personPersistence.findById(999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.findOne(999));
	}

	@Test
	@DisplayName("Debe contar el total de personas")
	void testCount() {
		when(personPersistence.find()).thenReturn(Arrays.asList(testPerson, new Person(2, "Ana", "G", Gender.FEMALE)));

		Integer count = personUseCase.count();

		assertEquals(2, count);
	}

	@Test
	@DisplayName("Debe obtener la lista de teléfonos de una persona existente")
	void testGetPhonesSuccess() throws NoExistException {
		Phone phone = new Phone("3001234567", "Claro", testPerson);
		testPerson.setPhoneNumbers(Arrays.asList(phone));
		when(personPersistence.findById(123456789)).thenReturn(testPerson);

		List<Phone> phones = personUseCase.getPhones(123456789);

		assertEquals(1, phones.size());
		assertEquals("3001234567", phones.get(0).getNumber());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al obtener teléfonos de una persona inexistente")
	void testGetPhonesNotFound() {
		when(personPersistence.findById(999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.getPhones(999));
	}

	@Test
	@DisplayName("Debe obtener la lista de estudios de una persona existente")
	void testGetStudiesSuccess() throws NoExistException {
		Study study = new Study();
		study.setUniversityName("Pontificia Universidad Javeriana");
		testPerson.setStudies(Arrays.asList(study));
		when(personPersistence.findById(123456789)).thenReturn(testPerson);

		List<Study> studies = personUseCase.getStudies(123456789);

		assertEquals(1, studies.size());
		assertEquals("Pontificia Universidad Javeriana", studies.get(0).getUniversityName());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al obtener estudios de una persona inexistente")
	void testGetStudiesNotFound() {
		when(personPersistence.findById(999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.getStudies(999));
	}
}
