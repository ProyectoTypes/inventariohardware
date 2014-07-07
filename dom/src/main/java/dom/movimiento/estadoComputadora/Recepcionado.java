package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Recepcionado implements IEstado {
	Movimiento movimiento;
	public Recepcionado(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}

	
	@Override
	public void equipoRecibido() {
		// TODO Auto-generated method stub
		this.movimiento.setEstado(movimiento.getReparando());
	}
	
	@Override
	public void equipoReparado() {
		this.container.warnUser("Recepcionado.java - reparando(): El equipo NO ha sido recepcionado");
		
	}
	@Override
	public void equipoFinalizado() {
		this.container.warnUser("Recepcionado.java - finalizado(): El equipo NO ha sido Recepcionado/Reparado");
		
	}
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	public String toString() {
		return "RECIBIENDO EQUIPO";
	}

}
