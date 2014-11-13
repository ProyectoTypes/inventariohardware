package dom.computadora.hardware.gabinete.memoria;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

@DomainService
@Named("Placa De Red")
@Hidden()
public class MemoriaRamRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "memoriaram";
	}

	public String iconName() {
		return "memoriaram";
	}

	@MemberOrder(sequence = "10")
	@Named("Agregar Placa de Red")
	public MemoriaRam addMemoriaRam(final @Named("Modelo") String modelo,
			final @Named("Tamaño") int tamano,
			final @Named("Marca") String marca) {
		return nuevaMemoriaRam(modelo, tamano, marca);
	}

	private MemoriaRam nuevaMemoriaRam(final String modelo,
			final int tamano, final String marca) {

		MemoriaRam obj = this.container
				.newTransientInstance(MemoriaRam.class);
		obj.setModelo(modelo);
		obj.setMarca(marca);
		obj.setTamano(tamano);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	@Inject
	private DomainObjectContainer container;
}
