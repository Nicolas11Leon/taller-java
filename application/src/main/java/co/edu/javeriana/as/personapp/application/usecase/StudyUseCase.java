package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

	private StudyOutputPort studyPersistence;

	public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
		this.studyPersistence = studyPersistence;
	}

	@Override
	public void setPersistence(StudyOutputPort studyPersistence) {
		this.studyPersistence = studyPersistence;
	}

	@Override
	public Study create(Study study) {
		log.debug("Into create Study on Application Domain");
		return studyPersistence.save(study);
	}

	@Override
	public Study edit(Integer identificationProfession, Integer identificationPerson, Study study)
			throws NoExistException {
		Study oldStudy = studyPersistence.findById(identificationProfession, identificationPerson);
		if (oldStudy != null) {
			return studyPersistence.save(study);
		}
		throw new NoExistException("The study with id_prof " + identificationProfession + " and cc_per "
				+ identificationPerson + " does not exist into db, cannot be edited");
	}

	@Override
	public Boolean drop(Integer identificationProfession, Integer identificationPerson) throws NoExistException {
		Study oldStudy = studyPersistence.findById(identificationProfession, identificationPerson);
		if (oldStudy != null) {
			return studyPersistence.delete(identificationProfession, identificationPerson);
		}
		throw new NoExistException("The study with id_prof " + identificationProfession + " and cc_per "
				+ identificationPerson + " does not exist into db, cannot be dropped");
	}

	@Override
	public List<Study> findAll() {
		return studyPersistence.find();
	}

	@Override
	public Study findOne(Integer identificationProfession, Integer identificationPerson) throws NoExistException {
		Study oldStudy = studyPersistence.findById(identificationProfession, identificationPerson);
		if (oldStudy != null) {
			return oldStudy;
		}
		throw new NoExistException("The study with id_prof " + identificationProfession + " and cc_per "
				+ identificationPerson + " does not exist into db, cannot be found");
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
