package dom.movimiento;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.Computadora;
import dom.tecnico.Tecnico;

@Named("MOVIMIENTO")
public class MovimientoRepositorio {
	
	public MovimientoRepositorio() {

	}
	
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "movimiento";
	}

	public String iconName() {
		return "Movimiento";
	}
	
	// //////////////////////////////////////
	// Agregar Movimiento
	// //////////////////////////////////////
	
	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Movimiento addMovimiento(
						final @Named("Computadora") Computadora computadora,
						final @Optional @Named("Tecnico") Tecnico tecnico) {
		return nuevoMovimiento(computadora, tecnico, this.currentUserName());
	}
	
	@Programmatic
	public Movimiento nuevoMovimiento(
						final Computadora computadora,
						final Tecnico tecnico,
						final String ingresadoPor){
		final Movimiento unMovimiento = container.newTransientInstance(Movimiento.class);
		unMovimiento.setComputadora(computadora);
		unMovimiento.setHabilitado(true);
		unMovimiento.setIngresadoPor(ingresadoPor);
		container.persistIfNotAlready(unMovimiento);
		container.flush();
		return unMovimiento;
	}
	
	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Movimiento> listar() {
		final List<Movimiento> listaMovimientos = this.container
				.allMatches(new QueryDefault<Movimiento>(Movimiento.class,
						"eliminarMovimientoTrue", "ingresadoPor", this
								.currentUserName()));
		if (listaMovimientos.isEmpty()) {
			this.container.warnUser("No hay Movimiento cargados en el sistema.");
		}
		return listaMovimientos;
	}
	
	@Programmatic
	public List<Movimiento> autoComplete(final String ip) {
		return container.allMatches(new QueryDefault<Movimiento>(Movimiento.class,
				"autoCompletePorMovimiento", "ingresadoPor", this.currentUserName(),
				"ip", ip.toUpperCase().trim()));
	}
	
	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
	
	@javax.inject.Inject
	private MovimientoRepositorio movimientoRepositorio;
}
