package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

	public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		if (phone == null) return null;
		TelefonoModelCli model = new TelefonoModelCli();
		model.setNum(phone.getNumber());
		model.setOper(phone.getCompany());
		model.setDuenioCc(phone.getOwner() != null ? phone.getOwner().getIdentification() : null);
		return model;
	}

	public Phone fromAdapterCliToDomain(TelefonoModelCli model) {
		if (model == null) return null;
		Person owner = new Person();
		owner.setIdentification(model.getDuenioCc());
		return new Phone(model.getNum(), model.getOper(), owner);
	}
}
