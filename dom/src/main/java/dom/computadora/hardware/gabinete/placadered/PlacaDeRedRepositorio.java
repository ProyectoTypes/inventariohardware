package dom.computadora.hardware.gabinete.placadered;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

@DomainService
@Named("Placa De Red")
@Hidden
public class PlacaDeRedRepositorio {

	public PlacaDeRedRepositorio() {

	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "placaDeRed";
	}

	public String iconName() {
		return "red";
	}

	@MemberOrder(sequence = "10")
	@Named("Agregar Placa de Red")
	public PlacaDeRed addPlacaDeRed(final @Named("IP") String ip,
			final @Named("MAC") String mac) {
		return nuevaPlacaDeRed(ip, mac);
	}

	private PlacaDeRed nuevaPlacaDeRed(final String ip, final String mac) {

		PlacaDeRed obj = this.container.newTransientInstance(PlacaDeRed.class);
		obj.setIp(ip);
		obj.setMac(mac);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	@Inject
	private DomainObjectContainer container;

	public List<PlacaDeRed> listar() {
		return this.container.allMatches(new QueryDefault<PlacaDeRed>(
				PlacaDeRed.class, "listar"));

	}
}
