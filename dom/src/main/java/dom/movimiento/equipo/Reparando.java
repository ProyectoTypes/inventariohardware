package dom.movimiento.equipo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import servicio.email.EmailService;
import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idReparando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "reparandoUnique", members = { "idReparando" }) })
@ObjectType("REPARANDO")
@Audited
@Bookmarkable
public class Reparando implements IEstado {
	public String title() {
		return "REPARANDO ";
	}

	public String iconName() {
		return "sector";
	}

	public Reparando(Movimiento movimiento) {
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
	public void asignarTecnico() {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO RECIBIDO.");

	}

	@Override
	public void esperarRepuestos() {
		this.getMovimiento().setEstadoActual("CAMBIA AL ESTADO ESPERANDO");
		this.getMovimiento().setEstado(this.getMovimiento().getEsperando());
	}

	@Override
	public void finalizarSoporte() {
		this.getMovimiento().setEstadoActual("CAMBIA AL ESTADO ENTREGANDO.");
		emailService.send(this.getMovimiento().getComputadora());
		this.getMovimiento().getTecnico().restaComputadora();
		this.getMovimiento().setEstado(this.getMovimiento().getEntregando());
	}

	@javax.inject.Inject
	private EmailService emailService;

	@Override
	public void noHayRepuestos() {
		// TODO Auto-generated method stub
		this.getMovimiento().setEstadoActual(
				"NO CANCELA: NO ES EL ESTADO ESPERANDO");
	}

	@Override
	public void llegaronRepuestos() {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO ESPERANDO");
	}

}
