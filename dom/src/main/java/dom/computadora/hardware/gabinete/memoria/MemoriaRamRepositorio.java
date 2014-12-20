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
	
	/**
	 * ID
	 * @return
	 */
	public String getId() {
		return "memoriaram";
	}

	/**
	 * Nombre del Icono
	 * @return 
	 */
	public String iconName() {
		return "memoriaram";
	}
	
	/**
	 * Agregar Placa de Red.
	 * @param modelo
	 * @param tamano
	 * @param marca
	 * @return
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Placa de Red")
	public MemoriaRam create(final @Named("Modelo") String modelo,
			final @Named("Tamaño") int tamano,
			final @Named("Marca") String marca) {
		return nuevaMemoriaRam(modelo, tamano, marca);
	}

	/**
	 * Nueva memoria ram.
	 * @param modelo 
	 * @param tamano
	 * @param marca
	 * @return 
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
	 * Método Listar.
	 * @return
	 */
	public List<MemoriaRam> listAll() {
		return this.container.allMatches(new QueryDefault<MemoriaRam>(
				MemoriaRam.class, "listar"));
	}
	
	/**
	 * Inyección del Contenedor.
	 */
	@Inject
	private DomainObjectContainer container;
}