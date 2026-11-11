package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	private PhoneInputPort phoneInputPort;
	private PersonInputPort personInputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption != null && dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption != null && dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<TelefonoResponse> historial(String database) {
		log.info("Into historial Telefono in Input Adapter");
		try {
			if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneInputPort.findAll().stream().map(telefonoMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return phoneInputPort.findAll().stream().map(telefonoMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public TelefonoResponse obtener(String database, String number) {
		log.info("Into obtener Telefono in Input Adapter");
		try {
			String db = setPhoneOutputPortInjection(database);
			Phone phone = phoneInputPort.findOne(number);
			return telefonoMapperRest.fromDomainToAdapterRest(phone, db);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public TelefonoResponse crear(TelefonoRequest request) {
		log.info("Into crear Telefono in Input Adapter");
		try {
			String db = setPhoneOutputPortInjection(request.getDatabase());
			Phone phone = telefonoMapperRest.fromAdapterToDomain(request);
			if (phone.getOwner() != null && phone.getOwner().getIdentification() != null) {
				try {
					Person owner = personInputPort.findOne(phone.getOwner().getIdentification());
					phone.setOwner(owner);
				} catch (NoExistException e) {
					log.warn("Owner not found: {}", e.getMessage());
				}
			}
			Phone persisted = phoneInputPort.create(phone);
			return telefonoMapperRest.fromDomainToAdapterRest(persisted, db);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public TelefonoResponse modificar(String number, TelefonoRequest request) {
		log.info("Into modificar Telefono in Input Adapter");
		try {
			String db = setPhoneOutputPortInjection(request.getDatabase());
			Phone toEdit = telefonoMapperRest.fromAdapterToDomain(request);
			toEdit.setNumber(number);
			if (toEdit.getOwner() != null && toEdit.getOwner().getIdentification() != null) {
				try {
					Person owner = personInputPort.findOne(toEdit.getOwner().getIdentification());
					toEdit.setOwner(owner);
				} catch (NoExistException e) {
					log.warn("Owner not found: {}", e.getMessage());
				}
			}
			Phone edited = phoneInputPort.edit(number, toEdit);
			return telefonoMapperRest.fromDomainToAdapterRest(edited, db);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public Boolean eliminar(String database, String number) {
		log.info("Into eliminar Telefono in Input Adapter");
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.drop(number);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	public Integer contar(String database) {
		log.info("Into contar Telefono in Input Adapter");
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.count();
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return 0;
		}
	}
}
