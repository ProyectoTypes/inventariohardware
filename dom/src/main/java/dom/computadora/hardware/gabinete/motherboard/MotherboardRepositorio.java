package dom.computadora.hardware.gabinete.motherboard;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

@DomainService
@Named("Placa De Red")
@Hidden()
public class MotherboardRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "motherboard";
	}

	public String iconName() {
		return "motherboard";
	}

	@MemberOrder(sequence = "10")
	@Named("Agregar Motherboard")
	public Motherboard addMotherboard(final @Named("Modelo") String modelo) {
		return nuevaMotherboard(modelo);
	}

	private Motherboard nuevaMotherboard(final String modelo) {

		Motherboard obj = this.container
				.newTransientInstance(Motherboard.class);
		obj.setModelo(modelo);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	@Inject
	private DomainObjectContainer container;
}
