package dom.computadora.hardware.gabinete.motherboard;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

@DomainService
@Named("Motherboard")
@Hidden
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
	
	// //////////////////////////////////////
	// Agregar 
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar Motherboard")
	public Motherboard create(final @Named("Modelo") String modelo) {
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

	// //////////////////////////////////////
	// Listar 
	// //////////////////////////////////////
	
	public List<Motherboard> listAll() {
		return this.container.allMatches(new QueryDefault<Motherboard>(
				Motherboard.class, "listar"));
	}
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	
	@Inject
	private DomainObjectContainer container;
}
