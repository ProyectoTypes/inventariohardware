package dom.computadora.hardware.gabinete.placadered;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

// TODO: Auto-generated Javadoc
/**
 * Clase PlacaDeRedRepositorio.
 */
@DomainService
@Named("Placa De Red")
@Hidden
public class PlacaDeRedRepositorio {

	/**
	 * Instantiates a new placa de red repositorio.
	 */
	public PlacaDeRedRepositorio() {

	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "placaDeRed";
	}

	/**
	 * Nombre del Icono
	 * @return the string
	 */
	public String iconName() {
		return "red";
	}

	/**
	 * Adds the placa de red.
	 *
	 * @param ip the ip
	 * @param mac the mac
	 * @return the placa de red
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Placa de Red")
	public PlacaDeRed addPlacaDeRed(final @Named("IP") String ip,
			final @Named("MAC") String mac) {
		return nuevaPlacaDeRed(ip, mac);
	}

	/**
	 * Nueva placa de red.
	 *
	 * @param ip the ip
	 * @param mac the mac
	 * @return the placa de red
	 */
	private PlacaDeRed nuevaPlacaDeRed(final String ip, final String mac) {

		PlacaDeRed obj = this.container.newTransientInstance(PlacaDeRed.class);
		obj.setIp(ip);
		obj.setMac(mac);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/** The container. */
	@Inject
	private DomainObjectContainer container;

	/**
	 * Metodo para listar
	 *
	 * @return the list
	 */
	public List<PlacaDeRed> listar() {
		return this.container.allMatches(new QueryDefault<PlacaDeRed>(
				PlacaDeRed.class, "listar"));

	}
}
