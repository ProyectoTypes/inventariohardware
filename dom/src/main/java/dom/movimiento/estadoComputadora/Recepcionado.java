package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Recepcionado implements IEstado{

	@Override
	public String ManejoDelEstado(Movimiento movimiento) {
		// TODO Auto-generated method stub
//		movimiento.estado = new Reparando();
		this.container.informUser("EQUIPO RECEPCIONADO");
		return "retorno";
	}
	@javax.inject.Inject
	private DomainObjectContainer container;
	

}
