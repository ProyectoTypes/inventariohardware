package dom.computadora.hardware;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.hardware.gabinete.Gabinete;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.computadora.hardware.monitor.Monitor;
import dom.computadora.hardware.monitor.MonitorRepositorio;

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
		return "COMPONENTES";
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

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "20")
	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}
	public List<Monitor> choicesMonitor()
	{
		return this.repositorioMonitor.listAll();
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

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "40")
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	}
	public List<Impresora> choicesImpresora()
	{
		return this.repositorioImpresora.listAll();
	}
	/**
	 * Método para quitar una Impresora.
	 */
	@Hidden
	public void limpiarImpresora() {
		Impresora impresora = getImpresora();
		if (impresora == null) {
			return;
		}
		impresora.limpiarHardware(this);
	}
	@Inject
	private ImpresoraRepositorio repositorioImpresora;
	@Inject
	private MonitorRepositorio repositorioMonitor;

}
