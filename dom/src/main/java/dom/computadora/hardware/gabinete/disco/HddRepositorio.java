package dom.computadora.hardware.gabinete.disco;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.disco.Hdd.CategoriaDisco;

@DomainService
@Named("Hdd")
@Hidden()
public class HddRepositorio {

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
	@Named("Agregar Placa de Red")
	public Hdd addHdd(final @Named("Marca") String marca,
			final @Named("Categoria") CategoriaDisco tipo,
			final @Named("Tipo") int tamano) {
		return nuevoHdd(marca, tipo, tamano);
	}

	private Hdd nuevoHdd(final String marca, final CategoriaDisco tipo,
			final int tamano) {

		Hdd obj = this.container.newTransientInstance(Hdd.class);
		obj.setMarca(marca);
		obj.setTipo(tipo);
		obj.setTamano(tamano);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	@Inject
	private DomainObjectContainer container;
}
