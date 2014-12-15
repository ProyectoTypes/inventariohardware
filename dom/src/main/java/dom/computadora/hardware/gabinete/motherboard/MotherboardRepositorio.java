package dom.computadora.hardware.gabinete.motherboard;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

/**
 * Clase MotherboardRepositorio.
 */
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

	/**
	 * Icon name.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "motherboard";
	}

	/**
	 * Adds the motherboard.
	 *
	 * @param modelo the modelo
	 * @return the motherboard
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Motherboard")
	public Motherboard addMotherboard(final @Named("Modelo") String modelo) {
		return nuevaMotherboard(modelo);
	}

	/**
	 * Nueva motherboard.
	 *
	 * @param modelo the modelo
	 * @return the motherboard
	 */
	private Motherboard nuevaMotherboard(final String modelo) {

		Motherboard obj = this.container
				.newTransientInstance(Motherboard.class);
		obj.setModelo(modelo);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/** The container. */
	@Inject
	private DomainObjectContainer container;

	/**
	 * Metodo para Listar.
	 *
	 * @return the list
	 */
	public List<Motherboard> listar() {
		return this.container.allMatches(new QueryDefault<Motherboard>(
				Motherboard.class, "listar"));
	}
}
