package dom.computadora.hardware.gabinete;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;

// TODO: Auto-generated Javadoc
/**
 * Clase Gabinete.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@ObjectType("Gabinete")
public class Gabinete {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Titulo de la clase
	 *
	 * @return the string
	 */
	public String title() {
		return "GABINETE";
	}

	/**
	 * Nombre del icono.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "gabinete";
	}

	/**
	 * Constructor: Composicion.
	 *
	 * @param listaPlacaDeRed the lista placa de red
	 * @param listaHdd the lista hdd
	 * @param procesador the procesador
	 * @param listaMemoriaRam the lista memoria ram
	 * @param motherboard the lista motherboard
	 */

	public Gabinete(List<PlacaDeRed> listaPlacaDeRed, List<Disco> listaHdd,
			Procesador procesador, List<MemoriaRam> listaMemoriaRam,
			Motherboard motherboard) {
		super();
		this.listaPlacaDeRed = listaPlacaDeRed;
		this.listaDisco = listaHdd;
		this.procesador = procesador;
		this.listaMemoriaRam = listaMemoriaRam;
		this.motherboard = motherboard;
	}
	
	/**
	 * Instantiates a new gabinete.
	 */
	public Gabinete()
	{
		this.listaPlacaDeRed = new ArrayList<PlacaDeRed>();
		this.listaDisco = new ArrayList<Disco>();
		this.procesador = new Procesador();
		this.listaMemoriaRam = new ArrayList<MemoriaRam>();;
		this.motherboard = new Motherboard();

	}
	
	/** lista placa de red. */
	private List<PlacaDeRed> listaPlacaDeRed;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "50")
	public List<PlacaDeRed> getListaPlacaDeRed() {
		return listaPlacaDeRed;
	}

	public void setListaPlacaDeRed(List<PlacaDeRed> listaPlacaDeRed) {
		this.listaPlacaDeRed = listaPlacaDeRed;
	}

	/**
	 * Agregar placa de red.
	 *
	 * @param placaDeRed the placa de red
	 */
	@Programmatic
	public void agregarPlacaDeRed(final PlacaDeRed placaDeRed) {
		this.listaPlacaDeRed.add(placaDeRed);
	}

	/** The lista disco. */
	private List<Disco> listaDisco;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "60")
	public List<Disco> getListaDisco() {
		return listaDisco;
	}

	public void setListaDisco(List<Disco> listaDisco) {
		this.listaDisco = listaDisco;
	}
	
	/**
	 * Agregar hdd.
	 *
	 * @param hdd the hdd
	 */
	@Programmatic
	public void agregarHdd(final Disco hdd) {
		this.listaDisco.add(hdd);
	}

	/** The procesador. */
	private Procesador procesador;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "70")
	public Procesador getProcesador() {
		return procesador;
	}

	public void setProcesador(Procesador procesador) {
		this.procesador = procesador;
	}

	/** The lista memoria ram. */
	private List<MemoriaRam> listaMemoriaRam;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "80")
	public List<MemoriaRam> getListaMemoriaRam() {
		return listaMemoriaRam;
	}

	public void setListaMemoriaRam(List<MemoriaRam> listaMemoriaRam) {
		this.listaMemoriaRam = listaMemoriaRam;
	}

	/**
	 * Agregar memoria ram.
	 *
	 * @param memoriaRam the memoria ram
	 */
	@Programmatic
	public void agregarMemoriaRam(final MemoriaRam memoriaRam) {
		this.listaMemoriaRam.add(memoriaRam);
	}

	/** The motherboard. */
	private Motherboard motherboard;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(sequence = "90")
	public Motherboard getMotherboard() {
		return motherboard;
	}

	public void setMotherboard(Motherboard motherboard) {
		this.motherboard = motherboard;
	}
}
