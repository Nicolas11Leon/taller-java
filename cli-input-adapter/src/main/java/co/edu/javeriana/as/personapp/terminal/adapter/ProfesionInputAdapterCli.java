package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	private ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Profesion in Input Adapter CLI");
		List<ProfesionModelCli> profesiones = professionInputPort.findAll().stream()
				.map(profesionMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		if (profesiones.isEmpty()) {
			System.out.println("No hay profesiones registradas.");
		} else {
			profesiones.forEach(System.out::println);
		}
	}

	public void obtenerProfesion(Integer id) {
		try {
			Profession profession = professionInputPort.findOne(id);
			ProfesionModelCli model = profesionMapperCli.fromDomainToAdapterCli(profession);
			System.out.println("Profesión encontrada: " + model);
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void crearProfesion(ProfesionModelCli model) {
		Profession profession = profesionMapperCli.fromAdapterCliToDomain(model);
		Profession created = professionInputPort.create(profession);
		System.out.println("Profesión creada exitosamente: " + profesionMapperCli.fromDomainToAdapterCli(created));
	}

	public void modificarProfesion(Integer id, ProfesionModelCli model) {
		try {
			Profession profession = profesionMapperCli.fromAdapterCliToDomain(model);
			profession.setIdentification(id);
			Profession edited = professionInputPort.edit(id, profession);
			System.out.println("Profesión modificada exitosamente: " + profesionMapperCli.fromDomainToAdapterCli(edited));
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminarProfesion(Integer id) {
		try {
			Boolean dropped = professionInputPort.drop(id);
			if (Boolean.TRUE.equals(dropped)) {
				System.out.println("Profesión con id " + id + " eliminada exitosamente.");
			} else {
				System.out.println("No se pudo eliminar la profesión con id " + id);
			}
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de profesiones registradas: " + professionInputPort.count());
	}
}
