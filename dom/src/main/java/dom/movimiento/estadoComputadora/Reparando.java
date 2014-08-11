package dom.movimiento.estadoComputadora;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
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

	@Override
	public IEstado asignarTecnico(Movimiento unM) {
		unM.setEstadoActual("NO ES EL ESTADO RECIBIDO.");
		return this;

	}

	@Override
	public IEstado esperarRepuestos(Movimiento unM) {
		unM.setEstadoActual("CAMBIA AL ESTADO ESPERANDO");
		return new Esperando();
	}

	@Override
	public IEstado finalizarSoporte(Movimiento unM) {
		unM.setEstadoActual("CAMBIA AL ESTADO ENTREGANDO.");
		emailService.send(unM.getComputadora());
		unM.getTecnico().restaComputadora();
		return new Entregado();
	}

	@javax.inject.Inject
	private EmailService emailService;

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
