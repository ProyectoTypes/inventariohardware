package dom.soporte.estadoSoporte;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

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
	private Soporte soporte;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	// }}
	public Recepcionado(Soporte unMovimiento) {
		this.soporte = unMovimiento;
	}

	/**
	 * Permite asignar un Tecnico encargado del Soporte Tecnico.
	 * 
	 * <p>
	 * Verifica que el Tecnico nuevo este disponible, en caso afirmativo lo
	 * vincula con Soporte.
	 * </p>
	 * <p>
	 * Es el encargado de cambiar de estados: Recepcionado -> Reparando.
	 * </p>
	 * @param tecnico
	 */
	@Override
	public void asignarTecnico(final Tecnico tecnico) {
		if (this.getSoporte().getTecnico().estaDisponible()) {
			this.getSoporte().getTecnico().sumaComputadora();
			this.getSoporte().getTecnico()
					.addToComputadora(this.getSoporte().getComputadora());
			this.getSoporte().setEstado(this.getSoporte().getReparando());
		} else {
			this.getSoporte().setTecnico(null);
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
