package dom.computadora.hardware;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.hardware.gabinete.Gabinete;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.monitor.Monitor;

/**
 * Calse Hardware.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@ObjectType("Hardware")
public class Hardware {
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Titulo de la clase.
	 *
	 * @return the string
	 */
	public String title() {
		return "HARDWARE";
	}

	/**
	 * Nombre del icono de la clase.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "hardware";
	}

	/**
	 * Constructor.
	 *
	 * @param monitor the monitor
	 * @param impresora the impresora
	 * @param gabinete the gabinete
	 */
	public Hardware(Monitor monitor, Impresora impresora, Gabinete gabinete) {
		this.monitor = monitor;
		this.impresora = impresora;
		this.gabinete = gabinete;
	}

	/**
	 * Instancia de un nuevo hardware.
	 */
	public Hardware() {
		this.monitor = new Monitor();
		this.impresora = new Impresora();
		this.gabinete = new Gabinete();
	}

	// //////////////////////////////////////
	// Fabricante (propiedad)
	// //////////////////////////////////////

	/** Monitor. */
	private Monitor monitor;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "20")
	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	/** Gabinete. */
	private Gabinete gabinete;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "30")
	public Gabinete getGabinete() {
		return gabinete;
	}

	public void setGabinete(Gabinete gabinete) {
		this.gabinete = gabinete;
	}

	/** Impresora. */
	private Impresora impresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "40")
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	}

}
