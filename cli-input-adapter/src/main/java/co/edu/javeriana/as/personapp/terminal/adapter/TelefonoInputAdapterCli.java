package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

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
	private TelefonoMapperCli telefonoMapperCli;

	private PhoneInputPort phoneInputPort;
	private PersonInputPort personInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Telefono in Input Adapter CLI");
		List<TelefonoModelCli> telefonos = phoneInputPort.findAll().stream()
				.map(telefonoMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		if (telefonos.isEmpty()) {
			System.out.println("No hay teléfonos registrados.");
		} else {
			telefonos.forEach(System.out::println);
		}
	}

	public void obtenerTelefono(String num) {
		try {
			Phone phone = phoneInputPort.findOne(num);
			TelefonoModelCli model = telefonoMapperCli.fromDomainToAdapterCli(phone);
			System.out.println("Teléfono encontrado: " + model);
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void crearTelefono(TelefonoModelCli model) {
		Phone phone = telefonoMapperCli.fromAdapterCliToDomain(model);
		if (model.getDuenioCc() != null) {
			try {
				Person owner = personInputPort.findOne(model.getDuenioCc());
				phone.setOwner(owner);
			} catch (NoExistException e) {
				System.out.println("Advertencia: El dueño no existe en la base de datos.");
			}
		}
		Phone created = phoneInputPort.create(phone);
		System.out.println("Teléfono creado exitosamente: " + telefonoMapperCli.fromDomainToAdapterCli(created));
	}

	public void modificarTelefono(String num, TelefonoModelCli model) {
		try {
			Phone phone = telefonoMapperCli.fromAdapterCliToDomain(model);
			phone.setNumber(num);
			if (model.getDuenioCc() != null) {
				try {
					Person owner = personInputPort.findOne(model.getDuenioCc());
					phone.setOwner(owner);
				} catch (NoExistException ignored) {}
			}
			Phone edited = phoneInputPort.edit(num, phone);
			System.out.println("Teléfono modificado exitosamente: " + telefonoMapperCli.fromDomainToAdapterCli(edited));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminarTelefono(String num) {
		try {
			Boolean dropped = phoneInputPort.drop(num);
			if (Boolean.TRUE.equals(dropped)) {
				System.out.println("Teléfono " + num + " eliminado exitosamente.");
			} else {
				System.out.println("No se pudo eliminar el teléfono " + num);
			}
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de teléfonos registrados: " + phoneInputPort.count());
	}
}
