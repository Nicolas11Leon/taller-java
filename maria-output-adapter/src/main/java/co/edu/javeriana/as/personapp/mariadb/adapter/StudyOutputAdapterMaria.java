package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMaria estudiosRepositoryMaria;

	@Autowired
	private EstudiosMapperMaria estudiosMapperMaria;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MariaDB for Study");
		EstudiosEntity entity = estudiosMapperMaria.fromDomainToAdapter(study);
		EstudiosEntity persisted = estudiosRepositoryMaria.save(entity);
		return estudiosMapperMaria.fromAdapterToDomain(persisted);
	}

	@Override
	public Boolean delete(Integer identificationProfession, Integer identificationPerson) {
		log.debug("Into delete on Adapter MariaDB for Study");
		EstudiosEntityPK pk = new EstudiosEntityPK(identificationProfession, identificationPerson);
		estudiosRepositoryMaria.deleteById(pk);
		return estudiosRepositoryMaria.findById(pk).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MariaDB for Study");
		return estudiosRepositoryMaria.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer identificationProfession, Integer identificationPerson) {
		log.debug("Into findById on Adapter MariaDB for Study");
		EstudiosEntityPK pk = new EstudiosEntityPK(identificationProfession, identificationPerson);
		return estudiosRepositoryMaria.findById(pk)
				.map(estudiosMapperMaria::fromAdapterToDomain)
				.orElse(null);
	}
}
