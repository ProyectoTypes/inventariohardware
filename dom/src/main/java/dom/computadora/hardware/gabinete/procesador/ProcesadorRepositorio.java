package dom.computadora.hardware.gabinete.procesador;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

/**
 * clase ProcesadorRepositorio.
 */
@DomainService
@Named("Procesador")
@Hidden
public class ProcesadorRepositorio {

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "procesador";
	}

	/**
	 * Nombre de icono
	 * @return the string
	 */
	public String iconName() {
		return "procesador";
	}

	/**
	 * Agregar Procesador..
	 * @param modelo 
	 * @return
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Procesador")
	public Procesador create(final @Named("Modelo") String modelo) {
		return nuevoProcesador(modelo);
	}

	/**
	 * Nuevo procesador.
	 * @param modelo         
	 * @return obj
	 */
	private Procesador nuevoProcesador(final String modelo) {

		Procesador obj = this.container.newTransientInstance(Procesador.class);
		obj.setModelo(modelo);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/**
	 * Lista de procesador.
	 * @return the list
	 */	
	public List<Procesador> listAll() {
		return this.container.allMatches(new QueryDefault<Procesador>(
				Procesador.class, "listar"));
	}
	
	/**
 	* Inyección del Contenedor.
 	*/
	@Inject
	private DomainObjectContainer container;
}