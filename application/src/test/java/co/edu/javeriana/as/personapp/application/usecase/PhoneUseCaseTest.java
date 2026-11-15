package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;

@ExtendWith(MockitoExtension.class)
class PhoneUseCaseTest {

	@Mock
	private PhoneOutputPort phonePersistence;

	private PhoneUseCase phoneUseCase;

	private Phone testPhone;
	private Person testOwner;

	@BeforeEach
	void setUp() {
		phoneUseCase = new PhoneUseCase(phonePersistence);
		testOwner = new Person(123456789, "Pepe", "Perez", Gender.MALE);
		testPhone = new Phone("3001234567", "Claro", testOwner);
	}

	@Test
	@DisplayName("Debe crear un teléfono exitosamente")
	void testCreatePhone() {
		when(phonePersistence.save(any(Phone.class))).thenReturn(testPhone);

		Phone created = phoneUseCase.create(testPhone);

		assertNotNull(created);
		assertEquals("3001234567", created.getNumber());
		assertEquals("Claro", created.getCompany());
		verify(phonePersistence).save(testPhone);
	}

	@Test
	@DisplayName("Debe editar un teléfono existente exitosamente")
	void testEditPhoneSuccess() throws NoExistException {
		Phone updated = new Phone("3001234567", "Movistar", testOwner);
		when(phonePersistence.findById("3001234567")).thenReturn(testPhone);
		when(phonePersistence.save(updated)).thenReturn(updated);

		Phone result = phoneUseCase.edit("3001234567", updated);

		assertNotNull(result);
		assertEquals("Movistar", result.getCompany());
		verify(phonePersistence).save(updated);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar editar un teléfono inexistente")
	void testEditPhoneNotFound() {
		when(phonePersistence.findById("3999999999")).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.edit("3999999999", testPhone));
	}

	@Test
	@DisplayName("Debe eliminar un teléfono existente exitosamente")
	void testDropPhoneSuccess() throws NoExistException {
		when(phonePersistence.findById("3001234567")).thenReturn(testPhone);
		when(phonePersistence.delete("3001234567")).thenReturn(true);

		Boolean deleted = phoneUseCase.drop("3001234567");

		assertTrue(deleted);
		verify(phonePersistence).delete("3001234567");
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar eliminar un teléfono inexistente")
	void testDropPhoneNotFound() {
		when(phonePersistence.findById("3999999999")).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.drop("3999999999"));
	}

	@Test
	@DisplayName("Debe listar todos los teléfonos")
	void testFindAll() {
		when(phonePersistence.find()).thenReturn(Arrays.asList(testPhone));

		List<Phone> list = phoneUseCase.findAll();

		assertEquals(1, list.size());
		assertEquals("3001234567", list.get(0).getNumber());
	}

	@Test
	@DisplayName("Debe buscar un teléfono por su número")
	void testFindOneSuccess() throws NoExistException {
		when(phonePersistence.findById("3001234567")).thenReturn(testPhone);

		Phone found = phoneUseCase.findOne("3001234567");

		assertNotNull(found);
		assertEquals("3001234567", found.getNumber());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al buscar un teléfono que no existe")
	void testFindOneNotFound() {
		when(phonePersistence.findById("3999999999")).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.findOne("3999999999"));
	}

	@Test
	@DisplayName("Debe contar el total de teléfonos")
	void testCount() {
		when(phonePersistence.find()).thenReturn(Arrays.asList(testPhone, new Phone("3100000000", "Tigo", testOwner)));

		Integer count = phoneUseCase.count();

		assertEquals(2, count);
	}

	@Test
	@DisplayName("Debe obtener el dueño de un teléfono existente")
	void testGetOwnerSuccess() throws NoExistException {
		when(phonePersistence.findById("3001234567")).thenReturn(testPhone);

		Person owner = phoneUseCase.getOwner("3001234567");

		assertNotNull(owner);
		assertEquals(123456789, owner.getIdentification());
		assertEquals("Pepe", owner.getFirstName());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al obtener dueño de un teléfono inexistente")
	void testGetOwnerNotFound() {
		when(phonePersistence.findById("3999999999")).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.getOwner("3999999999"));
	}
}
