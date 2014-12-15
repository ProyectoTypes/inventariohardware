package dom.computadora.hardware.gabinete.disco;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;

/**
 * Clase DiscoRepositorio.
 */
@DomainService
@Named("Hdd")
@Hidden
public class DiscoRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "hdd";
	}

	public String iconName() {
		return "Hdd";
	}

	@MemberOrder(sequence = "10")
	@Named("Agregar Disco")
	public Disco addDisco(final @Named("Marca") String marca,
			final @Named("Tipo") CategoriaDisco tipo,
			final @Named("Tamaño") int tamano) {
		return nuevoDisco(marca, tipo, tamano);
	}

	private Disco nuevoDisco(final String marca, final CategoriaDisco tipo,
			final int tamano) {

		Disco obj = this.container.newTransientInstance(Disco.class);
		obj.setMarca(marca);
		obj.setTipo(tipo);
		obj.setTamano(tamano);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	@Inject
	private DomainObjectContainer container;

	public List<Disco> listar() {
		return this.container.allMatches(new QueryDefault<Disco>(
				Disco.class, "listar"));

	}
}
