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

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "motherboard";
	}

	/**
	 * Nombre del Icono.
	 * @return
	 */
	public String iconName() {
		return "motherboard";
	}

	/**
	 * Agregar Motherboard.
	 * @param modelo
	 * @return
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Motherboard")
	public Motherboard create(final @Named("Modelo") String modelo) {
		return nuevaMotherboard(modelo);
	}

	/**
	 * Nueva Motherboard.
	 * @param modelo
	 * @return
	 */
	private Motherboard nuevaMotherboard(final String modelo) {

		Motherboard obj = this.container
				.newTransientInstance(Motherboard.class);
		obj.setModelo(modelo);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/**
	 * Método para Listar.
	 * @return the list
	 */	
	public List<Motherboard> listAll() {
		return this.container.allMatches(new QueryDefault<Motherboard>(
				Motherboard.class, "listar"));
	}

	/**
	 * Inyección del Contenedor.
	 */
	@Inject
	private DomainObjectContainer container;
}
