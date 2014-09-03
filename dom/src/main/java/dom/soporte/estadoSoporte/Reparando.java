package dom.soporte.estadoSoporte;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import servicio.email.EmailService;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

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

	public Reparando(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Movimiento (property)
	private Soporte soporte;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	// }}
	/**
	 * Permite cambiar el Tecnico encargado del Soporte Tecnico.
	 * <p>
	 * Chequea que el nuevo Tecnico este Disponible, en caso afirmativo,
	 * desvincula el Tecnico anterior de la Computadora y del Soporte, y vincula
	 * el Tecnico nuevo al Soporte.
	 * </p>
	 * <p>
	 * Se mantiene en el estado Reparando
	 * </p>
	 * 
	 * @param tecnico
	 */
	@Override
	public void asignarTecnico(final Tecnico tecnico) {
		if (this.getSoporte().getTecnico() != null) {
			this.getSoporte().getTecnico()
					.desvincularComputadora(this.getSoporte().getComputadora());
			this.getSoporte().setTecnico(null);
		}
		if (this.getSoporte().getTecnico().estaDisponible()) {
			this.getSoporte().getTecnico().sumaComputadora();
			this.getSoporte().getTecnico()
					.addToComputadora(this.getSoporte().getComputadora());
			this.getSoporte().setEstado(this.getSoporte().getReparando());
			this.container.informUser("ASIGNADO NUEVO TECNICO.");
		} else {
			this.getSoporte().setTecnico(null);
			this.container
					.informUser("El Tecnico seleccionado no esta disponible.");
		}

	}

	@Override
	public void solicitarInsumos() {
		this.getSoporte().setEstado(this.getSoporte().getEsperando());
		this.container.informUser("ESPERANDO QUE LOS REPUESTOS LLEGUEN.");
	}

	@Override
	public void finalizarSoporte() {
		emailService.send(this.getSoporte().getComputadora());
		this.getSoporte().getTecnico().desvincularComputadora();
		this.getSoporte().setEstado(this.getSoporte().getEntregando());
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
		this.getSoporte().setEstado(this.getSoporte().getCancelado());
		this.container.informUser("EQUIPO DADO DE BAJA.");
	}
}
