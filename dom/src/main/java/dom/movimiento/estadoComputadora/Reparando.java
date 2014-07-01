package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Reparando implements IEstado{
	
	@Override
	public String ManejoDelEstado(Movimiento movimiento) {
		// TODO Auto-generated method stub
//		movimiento.estado = new Recepcionado();
		this.container.informUser("ESTADO B -> A");
		this.container.warnUser("ESTADO B -> A");
		return "ESTADO B -> A";
	}
	@javax.inject.Inject
	private DomainObjectContainer container;
}
