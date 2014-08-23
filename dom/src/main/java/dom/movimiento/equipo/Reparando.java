package dom.movimiento.equipo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
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
	//Deberia dejar que se asigne otro tecnico. y restarle la cant al anterior.
	@Override
	public void asignarTecnico() {
		if (this.getMovimiento().getTecnico().estaDisponible()) {
			this.getMovimiento().getTecnico().sumaComputadora();
			this.getMovimiento().getTecnico()
					.addToComputadora(this.getMovimiento().getComputadora());
			this.getMovimiento().setEstado(this.getMovimiento().getReparando());
			this.container.informUser("ASIGNADO NUEVO TECNICO.");
		} else {
			this.getMovimiento().setTecnico(null);
			this.container
					.informUser("El Tecnico seleccionado no esta disponible.");
		}

	}

	@Override
	public void solicitarInsumos() {
		this.getMovimiento().setEstado(this.getMovimiento().getEsperando());
		this.container.informUser("ESPERANDO QUE LOS REPUESTOS LLEGUEN.");
	}

	@Override
	public void finalizarSoporte() {
		emailService.send(this.getMovimiento().getComputadora());
		this.getMovimiento().getTecnico().restaComputadora();
		this.getMovimiento().setEstado(this.getMovimiento().getEntregando());
		this.container.informUser("SOPORTE TECNICO FINALIZADO.");
	}

	@javax.inject.Inject
	private EmailService emailService;

	@Override
	public void noHayRepuestos() {
		this.container.informUser("EL EQUIPO CONTINUA EN REPARACION.");

	}

	@Override
	public void llegaronRepuestos() {
		this.container.informUser("ES NECESARIO SOLICITAR REPUESTOS.");
	}
	@javax.inject.Inject
	private DomainObjectContainer container;

	@Override
	public void asignarEquipo() {
		
		
		
	}
}
