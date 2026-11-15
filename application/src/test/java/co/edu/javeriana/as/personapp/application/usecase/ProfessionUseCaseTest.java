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

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class ProfessionUseCaseTest {

	@Mock
	private ProfessionOutputPort professionPersistence;

	private ProfessionUseCase professionUseCase;

	private Profession testProfession;

	@BeforeEach
	void setUp() {
		professionUseCase = new ProfessionUseCase(professionPersistence);
		testProfession = new Profession(1, "Ingeniero de Sistemas", "Desarrollo de software", new ArrayList<>());
	}

	@Test
	@DisplayName("Debe crear una profesión exitosamente")
	void testCreateProfession() {
		when(professionPersistence.save(any(Profession.class))).thenReturn(testProfession);

		Profession created = professionUseCase.create(testProfession);

		assertNotNull(created);
		assertEquals(1, created.getIdentification());
		assertEquals("Ingeniero de Sistemas", created.getName());
		verify(professionPersistence).save(testProfession);
	}

	@Test
	@DisplayName("Debe editar una profesión existente exitosamente")
	void testEditProfessionSuccess() throws NoExistException {
		Profession updated = new Profession(1, "Ingeniero Cloud", "Infraestructura", new ArrayList<>());
		when(professionPersistence.findById(1)).thenReturn(testProfession);
		when(professionPersistence.save(updated)).thenReturn(updated);

		Profession result = professionUseCase.edit(1, updated);

		assertNotNull(result);
		assertEquals("Ingeniero Cloud", result.getName());
		verify(professionPersistence).save(updated);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar editar una profesión inexistente")
	void testEditProfessionNotFound() {
		when(professionPersistence.findById(99)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.edit(99, testProfession));
	}

	@Test
	@DisplayName("Debe eliminar una profesión existente")
	void testDropProfessionSuccess() throws NoExistException {
		when(professionPersistence.findById(1)).thenReturn(testProfession);
		when(professionPersistence.delete(1)).thenReturn(true);

		Boolean deleted = professionUseCase.drop(1);

		assertTrue(deleted);
		verify(professionPersistence).delete(1);
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al intentar eliminar una profesión inexistente")
	void testDropProfessionNotFound() {
		when(professionPersistence.findById(99)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.drop(99));
	}

	@Test
	@DisplayName("Debe listar todas las profesiones")
	void testFindAll() {
		when(professionPersistence.find()).thenReturn(Arrays.asList(testProfession));

		List<Profession> list = professionUseCase.findAll();

		assertEquals(1, list.size());
		assertEquals("Ingeniero de Sistemas", list.get(0).getName());
	}

	@Test
	@DisplayName("Debe buscar una profesión por su ID")
	void testFindOneSuccess() throws NoExistException {
		when(professionPersistence.findById(1)).thenReturn(testProfession);

		Profession found = professionUseCase.findOne(1);

		assertNotNull(found);
		assertEquals(1, found.getIdentification());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al buscar una profesión que no existe")
	void testFindOneNotFound() {
		when(professionPersistence.findById(99)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.findOne(99));
	}

	@Test
	@DisplayName("Debe contar el total de profesiones")
	void testCount() {
		when(professionPersistence.find()).thenReturn(Arrays.asList(testProfession, new Profession(2, "Médico", "Salud", new ArrayList<>())));

		Integer count = professionUseCase.count();

		assertEquals(2, count);
	}

	@Test
	@DisplayName("Debe obtener los estudios asociados a una profesión existente")
	void testGetStudiesSuccess() throws NoExistException {
		Study study = new Study();
		study.setUniversityName("Pontificia Universidad Javeriana");
		testProfession.setStudies(Arrays.asList(study));
		when(professionPersistence.findById(1)).thenReturn(testProfession);

		List<Study> studies = professionUseCase.getStudies(1);

		assertEquals(1, studies.size());
		assertEquals("Pontificia Universidad Javeriana", studies.get(0).getUniversityName());
	}

	@Test
	@DisplayName("Debe lanzar NoExistException al obtener estudios de una profesión inexistente")
	void testGetStudiesNotFound() {
		when(professionPersistence.findById(99)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.getStudies(99));
	}
}
