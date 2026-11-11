package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
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
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterRest {

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
	private EstudiosMapperRest estudiosMapperRest;

	private StudyInputPort studyInputPort;
	private PersonInputPort personInputPort;
	private ProfessionInputPort professionInputPort;

	private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption != null && dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption != null && dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<EstudioResponse> historial(String database) {
		log.info("Into historial Estudios in Input Adapter");
		try {
			if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public EstudioResponse obtener(String database, Integer idProf, Integer ccPer) {
		log.info("Into obtener Estudios in Input Adapter");
		try {
			String db = setStudyOutputPortInjection(database);
			Study study = studyInputPort.findOne(idProf, ccPer);
			return estudiosMapperRest.fromDomainToAdapterRest(study, db);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public EstudioResponse crear(EstudioRequest request) {
		log.info("Into crear Estudios in Input Adapter");
		try {
			String db = setStudyOutputPortInjection(request.getDatabase());
			Study study = estudiosMapperRest.fromAdapterToDomain(request);
			if (study.getPerson() != null && study.getPerson().getIdentification() != null) {
				try {
					Person person = personInputPort.findOne(study.getPerson().getIdentification());
					study.setPerson(person);
				} catch (NoExistException e) {
					log.warn("Person not found: {}", e.getMessage());
				}
			}
			if (study.getProfession() != null && study.getProfession().getIdentification() != null) {
				try {
					Profession prof = professionInputPort.findOne(study.getProfession().getIdentification());
					study.setProfession(prof);
				} catch (NoExistException e) {
					log.warn("Profession not found: {}", e.getMessage());
				}
			}
			Study persisted = studyInputPort.create(study);
			return estudiosMapperRest.fromDomainToAdapterRest(persisted, db);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public EstudioResponse modificar(Integer idProf, Integer ccPer, EstudioRequest request) {
		log.info("Into modificar Estudios in Input Adapter");
		try {
			String db = setStudyOutputPortInjection(request.getDatabase());
			Study toEdit = estudiosMapperRest.fromAdapterToDomain(request);
			toEdit.getProfession().setIdentification(idProf);
			toEdit.getPerson().setIdentification(ccPer);
			if (toEdit.getPerson() != null && toEdit.getPerson().getIdentification() != null) {
				try {
					Person person = personInputPort.findOne(toEdit.getPerson().getIdentification());
					toEdit.setPerson(person);
				} catch (NoExistException ignored) {}
			}
			if (toEdit.getProfession() != null && toEdit.getProfession().getIdentification() != null) {
				try {
					Profession prof = professionInputPort.findOne(toEdit.getProfession().getIdentification());
					toEdit.setProfession(prof);
				} catch (NoExistException ignored) {}
			}
			Study edited = studyInputPort.edit(idProf, ccPer, toEdit);
			return estudiosMapperRest.fromDomainToAdapterRest(edited, db);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public Boolean eliminar(String database, Integer idProf, Integer ccPer) {
		log.info("Into eliminar Estudios in Input Adapter");
		try {
			setStudyOutputPortInjection(database);
			return studyInputPort.drop(idProf, ccPer);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	public Integer contar(String database) {
		log.info("Into contar Estudios in Input Adapter");
		try {
			setStudyOutputPortInjection(database);
			return studyInputPort.count();
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return 0;
		}
	}
}
