package dom.movimiento.estadoComputadora;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;

import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEntregado")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "entregadoUnique", members = { "idEntregado" }) })
@ObjectType("ENTREGADO")
@Audited
@Bookmarkable
public class Entregado implements IEstado {
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return "ENTREGADO ";
	}

	public String iconName() {
		return "sector";
	}

	@Override
	public IEstado asignarTecnico(Movimiento unM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEstado esperarRepuestos(Movimiento unM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEstado finalizarSoporte(Movimiento unM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEstado noHayRepuestos(Movimiento unM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEstado llegaronRepuestos(Movimiento unM) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
