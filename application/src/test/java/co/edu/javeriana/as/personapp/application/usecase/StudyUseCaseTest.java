package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class StudyUseCaseTest {

	@Mock
	private StudyOutputPort studyPersistence;

	private StudyUseCase studyUseCase;

	private Study testStudy;
	private Person testPerson;
	private Profession testProfession;

	@BeforeEach
	void setUp() {
		studyUseCase = new StudyUseCase(studyPersistence);
		testPerson = new Person(123456789, "Pepe", "Perez", Gender.MALE);
		testProfession = new Profession(1, "Ingeniero de Sistemas", "Software", new java.util.ArrayList<>());
		testStudy = new Study(testPerson, testProfession, LocalDate.of(2020, 12, 15), "Pontificia Universidad Javeriana");
	}

	@Test
	@DisplayName("Debe crear un estudio exitosamente")
	void testCreateStudy() {
		when(studyPersistence.save(any(Study.class))).thenReturn(testStudy);

		Study created = studyUseCase.create(testStudy);

		assertNotNull(created);
		assertEquals("Pontificia Universidad Javeriana", created.getUniversityName());
		verify(studyPersistence).save(testStudy);
	}

	@Test
	@DisplayName("Debe editar un estudio existente exitosamente")
	void testEditStudySuccess() throws NoExistException {
		Study updated = new Study(testPerson, testProfession, LocalDate.of(2021, 6, 20), "Javeriana Cali");
		when(studyPersistence.findById(1, 123456789)).thenReturn(testStudy);
		when(studyPersistence.save(updated)).thenReturn(updated);

		Study result = studyUseCase.edit(1, 123456789, updated);

		assertNotNull(result);
		assertEquals("Javeriana Cali", result.getUniversityName());
		verify(studyPersistence).save(updated);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar editar un estudio inexistente")
	void testEditStudyNotFound() {
		when(studyPersistence.findById(99, 999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.edit(99, 999, testStudy));
	}

	@Test
	@DisplayName("Debe eliminar un estudio existente exitosamente")
	void testDropStudySuccess() throws NoExistException {
		when(studyPersistence.findById(1, 123456789)).thenReturn(testStudy);
		when(studyPersistence.delete(1, 123456789)).thenReturn(true);

		Boolean deleted = studyUseCase.drop(1, 123456789);

		assertTrue(deleted);
		verify(studyPersistence).delete(1, 123456789);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar eliminar un estudio inexistente")
	void testDropStudyNotFound() {
		when(studyPersistence.findById(99, 999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.drop(99, 999));
	}

	@Test
	@DisplayName("Debe listar todos los estudios")
	void testFindAll() {
		when(studyPersistence.find()).thenReturn(Arrays.asList(testStudy));

		List<Study> list = studyUseCase.findAll();

		assertEquals(1, list.size());
		assertEquals("Pontificia Universidad Javeriana", list.get(0).getUniversityName());
	}

	@Test
	@DisplayName("Debe buscar un estudio por su llave primaria compuesta")
	void testFindOneSuccess() throws NoExistException {
		when(studyPersistence.findById(1, 123456789)).thenReturn(testStudy);

		Study found = studyUseCase.findOne(1, 123456789);

		assertNotNull(found);
		assertEquals("Pontificia Universidad Javeriana", found.getUniversityName());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al buscar un estudio que no existe")
	void testFindOneNotFound() {
		when(studyPersistence.findById(99, 999)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.findOne(99, 999));
	}

	@Test
	@DisplayName("Debe contar el total de estudios")
	void testCount() {
		when(studyPersistence.find()).thenReturn(Arrays.asList(testStudy));

		Integer count = studyUseCase.count();

		assertEquals(1, count);
	}
}
