package dom.computadora.hardware.gabinete;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;

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
		return "COMPONENTES INTERNOS DEL GABINETE";
	}

	/**
	 * Nombre del icono.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "gabinete";
	}

	
	// //////////////////////////////////////
	// Mother (propiedad)
	// //////////////////////////////////////

	private Motherboard motherboard;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Mother de la Computadora:")
	@MemberOrder(sequence = "20")
	public Motherboard getMotherboard() {
		return motherboard;
	}

	public void setMotherboard(final Motherboard motherboard) {
		this.motherboard = motherboard;
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

	public Gabinete(PlacaDeRed placaDeRed, List<Disco> listaHdd,
			Procesador procesador, List<MemoriaRam> listaMemoriaRam,
			Motherboard motherboard) {
		super();
		this.placaDeRed = placaDeRed;
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
		this.placaDeRed = new PlacaDeRed();
		this.listaDisco = new ArrayList<Disco>();
		this.procesador = new Procesador();
		this.listaMemoriaRam = new ArrayList<MemoriaRam>();;
		this.motherboard = new Motherboard();

	}
	
	/** lista placa de red. */
	private PlacaDeRed placaDeRed;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Mother de la Computadora:")
	@MemberOrder(sequence = "20")
	public PlacaDeRed getPlacaDeRed() {
		return placaDeRed;
	}

	public void setPlacaDeRed(final PlacaDeRed placaDeRed) {
		this.placaDeRed = placaDeRed;
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
}
