package dom.movimiento.estadoComputadora;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;

import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idRecibido")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "recibidoUnique", members = { "idRecibido" }) })
@ObjectType("RECIBIDO")
@Audited
@Bookmarkable
public class Recepcionado implements IEstado {

	public String title() {
		return "RECIBIDO ";
	}

	public String iconName() {
		return "sector";
	}

	@Override
	public IEstado asignarTecnico(Movimiento unM) {
		unM.setEstadoActual("CAMBIA DE ESTADO A REPARANDO - EN REPARACION.");
		return new Reparando();

	}

	@Override
	public IEstado esperarRepuestos(Movimiento unM) {
		unM.setEstadoActual("NO ESPERA: NO ES EL ESTADO REPARANDO.");
		return this;
	}

	@Override
	public IEstado finalizarSoporte(Movimiento unM) {
		unM.setEstadoActual("NO FINALIZA: NO ES EL ESTADO REPARANDO.");
		return this;
	}

	@Override
	public IEstado noHayRepuestos(Movimiento unM) {
		// TODO Auto-generated method stub
		unM.setEstadoActual("NO CANCELA: NO ES EL ESTADO ESPERANDO");
		return this;
	}

	@Override
	public IEstado llegaronRepuestos(Movimiento unM) {
		unM.setEstadoActual("NO ES EL ESTADO ESPERANDO");
		return this;
	}

}
