package dom.soporte.estadosoporte;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.Computadora.CategoriaDisco;
import dom.impresora.Impresora;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEntregado")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "entregadoUnique", members = { "idEntregado" }) })
@ObjectType("ENTREGADO")
public class Entregando implements IEstado {
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return "ENTREGADO ";
	}

	public String iconName() {
		return "Entregando";
	}

	public Entregando(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Soporte (property)
	private Soporte soporte;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	@SuppressWarnings("unused")
	private void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	// }}
	@Override
	@Hidden
	public void asignarTecnico(final Tecnico tecnico) {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO.");

	}

	@Override
	@Hidden
	public void solicitarInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones) {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO.");
	}

	@Override
	@Hidden
	public void noHayInsumos(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO.");
	}

	@Override
	@Hidden
	public void finalizarSoporte() {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO.");

	}

	@Override
	@Hidden
	public void llegaronInsumos() {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO");
	}

	@Override
	@Hidden
	public void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
		this.container.informUser("EL SOPORTE HA SIDO FINALIZADO.");
	}

	@Override
	@Hidden
	public boolean escondeAsignarTecnico() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeFinalizarSoporte() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeSolicitarInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeLlegaronInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeNoHayInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeAsignarNuevoEquipo() {
		return true;
	}

	@javax.inject.Inject
	private DomainObjectContainer container;

}
