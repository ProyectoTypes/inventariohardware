package dom.movimiento.equipo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEntregado")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "entregadoUnique", members = { "idEntregado" }) })
@ObjectType("ENTREGADO")
@Audited
@Bookmarkable
public class Entregando implements IEstado {
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return "ENTREGADO ";
	}

	public String iconName() {
		return "sector";
	}
	public Entregando(Movimiento movimiento) {
		this.movimiento = movimiento;
	}
	// {{ Movimiento (property)
	private Movimiento movimiento;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")

	public Movimiento getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(final Movimiento movimiento) {
		this.movimiento = movimiento;
	}

	// }}
	@Override
	public void asignarTecnico( ) {
		this.getMovimiento().setEstadoActual("YA SE INICIALIZO.");
		
	}

	@Override
	public void esperarRepuestos( ) {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO ESPERANDO.");
	}

	@Override
	public void noHayRepuestos( ) {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO ESPERANDO");
	}

	@Override
	public void finalizarSoporte( ) {
		this.getMovimiento().setEstadoActual("FINALIZACION DEL SOPORTE");
	}

	@Override
	public void llegaronRepuestos( ) {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO CANCELADO");
	}
}
