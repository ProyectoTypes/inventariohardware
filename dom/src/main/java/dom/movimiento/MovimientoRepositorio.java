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
}
