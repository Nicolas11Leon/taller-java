package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private EstudiosMapperMongo estudiosMapperMongo;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MongoDB for Study");
		try {
			EstudiosDocument persisted = estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
			return estudiosMapperMongo.fromAdapterToDomain(persisted);
		} catch (MongoWriteException e) {
			log.warn(e.getMessage());
			return study;
		}
	}

	@Override
	public Boolean delete(Integer identificationProfession, Integer identificationPerson) {
		log.debug("Into delete on Adapter MongoDB for Study");
		String id1 = identificationPerson + "-" + identificationProfession;
		String id2 = identificationProfession + "-" + identificationPerson;
		estudiosRepositoryMongo.deleteById(id1);
		estudiosRepositoryMongo.deleteById(id2);
		return estudiosRepositoryMongo.findById(id1).isEmpty() && estudiosRepositoryMongo.findById(id2).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MongoDB for Study");
		return estudiosRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer identificationProfession, Integer identificationPerson) {
		log.debug("Into findById on Adapter MongoDB for Study");
		String id1 = identificationPerson + "-" + identificationProfession;
		Optional<EstudiosDocument> doc = estudiosRepositoryMongo.findById(id1);
		if (doc.isEmpty()) {
			String id2 = identificationProfession + "-" + identificationPerson;
			doc = estudiosRepositoryMongo.findById(id2);
		}
		return doc.map(estudiosMapperMongo::fromAdapterToDomain).orElse(null);
	}
}
