package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Reparando implements IEstado{
	Movimiento movimiento;
	public Reparando(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}

	@Override
	public void equipoRecibido() {
		this.container.warnUser("Reparando.java - recepcionando(): El equipo ya ha sido recepcionado");
		
	}
	@Override
	public void equipoReparado() {
		this.movimiento.setEstado(this.movimiento.getCancelado());
		
	}
	@Override
	public void equipoFinalizado() {
		this.container.warnUser("Recepcionado.java - finalizado(): El equipo NO ha sido Recepcionado/Reparado");
		
	}
	
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	public String toString() {
		return "EQUIPO EN REPARACION";
	}
}
