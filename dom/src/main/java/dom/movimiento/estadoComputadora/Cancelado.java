package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Cancelado implements IEstado{
	Movimiento movimiento;
	public Cancelado(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}
	@Override
	public void equipoRecibido() {
		this.container.warnUser("Recepcionado.java - equipoRecibido(): El equipo NO ha sido Reparado/Entregado");
		
	}

	@Override
	public void equipoReparado() {
		this.container.warnUser("Recepcionado.java - equipoReparado(): El equipo NO ha sido Recepcionado/Finalizado");
		
	}

	@Override
	public void equipoFinalizado() {
		this.container.warnUser("Cancelado.java - equipoFinalizado(): FIN :) ");
		
	}
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	public String toString() {
		return "EQUIPO CANCELADO";
	}

}
