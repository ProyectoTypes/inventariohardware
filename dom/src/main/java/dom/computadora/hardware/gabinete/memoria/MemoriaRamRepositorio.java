package dom.computadora.hardware.gabinete.memoria;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

/**
 * Clase MemoriaRamRepositorio.
 */
@DomainService
@Named("Memoria Ram")
@Hidden
public class MemoriaRamRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "memoriaram";
	}

	/**
	 * Nombre del icono
	 *
	 * @return the string
	 */
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

	/**
	 * Nueva memoria ram.
	 *
	 * @param modelo the modelo
	 * @param tamano the tamano
	 * @param marca the marca
	 * @return the memoria ram
	 */
	private MemoriaRam nuevaMemoriaRam(final String modelo, final int tamano,
			final String marca) {

		MemoriaRam obj = this.container.newTransientInstance(MemoriaRam.class);
		obj.setModelo(modelo);
		obj.setMarca(marca);
		obj.setTamano(tamano);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/**
	 * The container.
	 */
	@Inject
	private DomainObjectContainer container;

	/**
	 * Metodo Listar.
	 *
	 * @return the list
	 */
	public List<MemoriaRam> listar() {
		return this.container.allMatches(new QueryDefault<MemoriaRam>(
				MemoriaRam.class, "listar"));
	}
}
