package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {

	public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
		return fromDomainToAdapterRest(phone, "MariaDB");
	}

	public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
		return fromDomainToAdapterRest(phone, "MongoDB");
	}

	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		if (phone == null) return null;
		String ownerId = (phone.getOwner() != null && phone.getOwner().getIdentification() != null)
				? String.valueOf(phone.getOwner().getIdentification()) : "";
		return new TelefonoResponse(
				phone.getNumber(),
				phone.getCompany(),
				ownerId,
				database,
				"OK");
	}

	public Phone fromAdapterToDomain(TelefonoRequest request) {
		if (request == null) return null;
		Person owner = new Person();
		if (request.getOwnerDni() != null && !request.getOwnerDni().trim().isEmpty()) {
			try {
				owner.setIdentification(Integer.parseInt(request.getOwnerDni().trim()));
			} catch (NumberFormatException ignored) {}
		}
		return new Phone(request.getNumber(), request.getOper(), owner);
	}
}
