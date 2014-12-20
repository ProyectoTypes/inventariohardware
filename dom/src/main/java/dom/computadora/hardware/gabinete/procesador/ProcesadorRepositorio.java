package dom.computadora.hardware.gabinete.procesador;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

@DomainService
@Named("Procesador")
@Hidden
public class ProcesadorRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "procesador";
	}

	public String iconName() {
		return "procesador";
	}

	// //////////////////////////////////////
	// Agregar
	// //////////////////////////////////////
	
	@MemberOrder(sequence = "10")
	@Named("Agregar Procesador")
	public Procesador create(final @Named("Modelo") String modelo) {
		return nuevoProcesador(modelo);
	}

	private Procesador nuevoProcesador(final String modelo) {

		Procesador obj = this.container.newTransientInstance(Procesador.class);
		obj.setModelo(modelo);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	// //////////////////////////////////////
	// Listar
	// //////////////////////////////////////
	
	public List<Procesador> listAll() {
		return this.container.allMatches(new QueryDefault<Procesador>(
				Procesador.class, "listar"));
	}
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	
	@Inject
	private DomainObjectContainer container;
}
