package dom.computadora.hardware;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.hardware.gabinete.Gabinete;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.monitor.Monitor;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@ObjectType("Hardware")
@Audited
public class Hardware {
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return "HARDWARE";
	}

	public String iconName() {
		return "hardware";
	}

	/**
	 * Constructor: Aplicando la composicion.
	 * 
	 * @param monitor
	 * @param impresora
	 * @param gabinete
	 */
	public Hardware(Monitor monitor, Impresora impresora, Gabinete gabinete) {
		this.monitor = monitor;
		this.impresora = impresora;
		this.gabinete = gabinete;
	}

	// //////////////////////////////////////
	// Fabricante (propiedad)
	// //////////////////////////////////////

	// FIXME: Que sucede en la composicion cuando todos las clases que componen
	// a hardware tienen un campo fabricante?
	private String fabricante;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Fabricante del Hardware:")
	@MemberOrder(sequence = "10")
	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(final String fabricante) {
		this.fabricante = fabricante;
	}

	private Monitor monitor;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	private Gabinete gabinete;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public Gabinete getGabinete() {
		return gabinete;
	}

	public void setGabinete(Gabinete gabinete) {
		this.gabinete = gabinete;
	}

	private Impresora impresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	}

}
