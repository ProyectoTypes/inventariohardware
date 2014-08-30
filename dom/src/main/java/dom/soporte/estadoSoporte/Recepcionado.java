package dom.soporte.estadoSoporte;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.soporte.Soporte;

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

	// {{ Movimiento (property)
	private Soporte movimiento;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(final Soporte movimiento) {
		this.movimiento = movimiento;
	}

	// }}
	public Recepcionado(Soporte unMovimiento) {
		this.movimiento = unMovimiento;
	}

	@Override
	public void asignarTecnico() {
		if (this.getMovimiento().getTecnico().estaDisponible()) {
			this.getMovimiento().getTecnico().sumaComputadora();
			this.getMovimiento().getTecnico()
					.addToComputadora(this.getMovimiento().getComputadora());
			this.getMovimiento().setEstado(this.getMovimiento().getReparando());
		} else {
			this.getMovimiento().setTecnico(null);
			this.container
					.informUser("El Tecnico seleccionado no esta disponible.");
		}

	}

	@Override
	public void solicitarInsumos() {
		this.container
				.informUser("AVISO: ES NECESARIO ASIGNAR UN TECNICO PARA EL SOPORTE.");
	}

	@Override
	public void finalizarSoporte() {
		this.container
				.informUser("AVISO: ES NECESARIO ASIGNAR UN TECNICO PARA EL SOPORTE.");
	}

	@Override
	public void noHayRepuestos() {
		this.container
				.informUser("AVISO: ES NECESARIO ASIGNAR UN TECNICO PARA EL SOPORTE.");
	}

	@Override
	public void llegaronRepuestos() {
		this.container
				.informUser("AVISO: ES NECESARIO ASIGNAR UN TECNICO PARA EL SOPORTE.");
	}

	@javax.inject.Inject
	private DomainObjectContainer container;

	@Override
	public void asignarEquipo() {
		
		
	}
}
