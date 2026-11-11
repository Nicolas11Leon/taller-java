package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudiosMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterCli {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private EstudiosMapperCli estudiosMapperCli;

	private StudyInputPort studyInputPort;
	private PersonInputPort personInputPort;
	private ProfessionInputPort professionInputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Estudios in Input Adapter CLI");
		List<EstudiosModelCli> estudios = studyInputPort.findAll().stream()
				.map(estudiosMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		if (estudios.isEmpty()) {
			System.out.println("No hay estudios registrados.");
		} else {
			estudios.forEach(System.out::println);
		}
	}

	public void obtenerEstudio(Integer idProf, Integer ccPer) {
		try {
			Study study = studyInputPort.findOne(idProf, ccPer);
			EstudiosModelCli model = estudiosMapperCli.fromDomainToAdapterCli(study);
			System.out.println("Estudio encontrado: " + model);
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void crearEstudio(EstudiosModelCli model) {
		Study study = estudiosMapperCli.fromAdapterCliToDomain(model);
		if (model.getCcPer() != null) {
			try {
				Person person = personInputPort.findOne(model.getCcPer());
				study.setPerson(person);
			} catch (NoExistException e) {
				System.out.println("Advertencia: La persona no existe en la base de datos.");
			}
		}
		if (model.getIdProf() != null) {
			try {
				Profession profession = professionInputPort.findOne(model.getIdProf());
				study.setProfession(profession);
			} catch (NoExistException e) {
				System.out.println("Advertencia: La profesión no existe en la base de datos.");
			}
		}
		Study created = studyInputPort.create(study);
		System.out.println("Estudio creado exitosamente: " + estudiosMapperCli.fromDomainToAdapterCli(created));
	}

	public void modificarEstudio(Integer idProf, Integer ccPer, EstudiosModelCli model) {
		try {
			Study study = estudiosMapperCli.fromAdapterCliToDomain(model);
			study.getProfession().setIdentification(idProf);
			study.getPerson().setIdentification(ccPer);
			if (model.getCcPer() != null) {
				try {
					Person person = personInputPort.findOne(model.getCcPer());
					study.setPerson(person);
				} catch (NoExistException ignored) {}
			}
			if (model.getIdProf() != null) {
				try {
					Profession profession = professionInputPort.findOne(model.getIdProf());
					study.setProfession(profession);
				} catch (NoExistException ignored) {}
			}
			Study edited = studyInputPort.edit(idProf, ccPer, study);
			System.out.println("Estudio modificado exitosamente: " + estudiosMapperCli.fromDomainToAdapterCli(edited));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminarEstudio(Integer idProf, Integer ccPer) {
		try {
			Boolean dropped = studyInputPort.drop(idProf, ccPer);
			if (Boolean.TRUE.equals(dropped)) {
				System.out.println("Estudio (" + idProf + ", " + ccPer + ") eliminado exitosamente.");
			} else {
				System.out.println("No se pudo eliminar el estudio (" + idProf + ", " + ccPer + ")");
			}
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de estudios registrados: " + studyInputPort.count());
	}
}
