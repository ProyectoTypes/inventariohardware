package dom.computadora.hardware.gabinete;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.disco.DiscoRepositorio;
import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.memoria.MemoriaRamRepositorio;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.motherboard.MotherboardRepositorio;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRedRepositorio;
import dom.computadora.hardware.gabinete.procesador.Procesador;
import dom.computadora.hardware.gabinete.procesador.ProcesadorRepositorio;

// TODO: Auto-generated Javadoc
/**
 * Clase GabineteRepositorio.
 */
@DomainService
@Named("Gabinete")
@Hidden
public class GabineteRepositorio {
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "gabinete";
	}

	/**
	 * Nombre del clase
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Gabinete";
	}

	/**
	 * Agregar gabinete.
	 *
	 * @param ip the ip
	 * @param mac the mac
	 * @param marcaDisco the marca disco
	 * @param tipoDisco the tipo disco
	 * @param tamanoDisco the tamano disco
	 * @param modeloProcesador the modelo procesador
	 * @param modeloRam the modelo ram
	 * @param tamanoRam the tamano ram
	 * @param marcaRam the marca ram
	 * @param modeloMotherboard the modelo motherboard
	 * @return the gabinete
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Gabinete")
	public Gabinete agregarGabinete(final @Named("IP") String ip,
			final @Named("MAC") String mac,
			final @Named("HDD Marca ") String marcaDisco,
			final @Named("HDD Categoria ") CategoriaDisco tipoDisco,
			final @Named("HDD Tamaño ") int tamanoDisco,
			final @Named("CPU Modelo ") String modeloProcesador,
			final @Named("RAM Modelo") String modeloRam,
			final @Named("RAM Tamaño") int tamanoRam,
			final @Named("RAM Marca") String marcaRam,
			final @Named("Modelo Motherboard") String modeloMotherboard) {
		PlacaDeRed placaDeRed = new PlacaDeRed(ip, mac);
		Disco hdd = new Disco(marcaDisco, tipoDisco, tamanoDisco);
		Procesador procesador = new Procesador(modeloProcesador);
		MemoriaRam memoriaRam = new MemoriaRam(modeloRam, tamanoRam, marcaRam);
		Motherboard motherboard = new Motherboard(modeloMotherboard);
		return nuevoGabinete(placaDeRed, hdd, procesador, memoriaRam,
				motherboard);
	}

	@MemberOrder(sequence = "10")
	@Named("Agregar Gabinete")
	public Gabinete addGabinete(
			final @Named("Placa de Red") PlacaDeRed placaDeRed,
			final @Named("Hdd") Disco hdd,
			final @Named("Procesador") Procesador procesador,
			final @Named("Memoria Ram") MemoriaRam memoriaRam,
			final @Named("Motherboard") Motherboard motherboard) {
		return nuevoGabinete(placaDeRed, hdd, procesador, memoriaRam,
				motherboard);
	}

	/**
	 * Metodo que lista las placas de red.
	 *
	 * @return the list
	 */
	public List<PlacaDeRed> choices0AddGabinete() {
		return this.placaderedRepositorio.listar();
	}

	/**
	 * Metodo que lista los disos rigidos.
	 *
	 * @return the list
	 */
	public List<Disco> choices1AddGabinete() {
		return this.discoRepositorio.listar();
	}

	/**
	 * Metodo que lista los procesadores.
	 *
	 * @return the list
	 */
	public List<Procesador> choices2AddGabinete() {
		return this.procesadorRepositorio.listar();
	}

	/**
	 * Metodo que lista las memorias.
	 *
	 * @return the list
	 */
	public List<MemoriaRam> choices3AddGabinete() {
		return this.memoriaramRepositorio.listar();
	}

	/**
	 * Metodo que lista las motherboards.
	 *
	 * @return the list
	 */
	public List<Motherboard> choices4AddGabinete() {
		return this.motherboardRepositorio.listar();
	}

	/**
	 * Nuevo gabinete.
	 *
	 * @param placaDeRed the placa de red
	 * @param hdd the hdd
	 * @param procesador the procesador
	 * @param memoriaRam the memoria ram
	 * @param motherboard the motherboard
	 * @return the gabinete
	 */
	private Gabinete nuevoGabinete(final PlacaDeRed placaDeRed,
			final Disco hdd, final Procesador procesador,
			final MemoriaRam memoriaRam, final Motherboard motherboard) {

		Gabinete obj = this.container.newTransientInstance(Gabinete.class);
		obj.agregarHdd(hdd);
		obj.agregarMemoriaRam(memoriaRam);
		obj.agregarPlacaDeRed(placaDeRed);
		obj.setMotherboard(motherboard);
		obj.setProcesador(procesador);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}

	/** The container. */
	@Inject
	private DomainObjectContainer container;
	
	/** The placadered repositorio. */
	@Inject
	private PlacaDeRedRepositorio placaderedRepositorio;
	
	/** The disco repositorio. */
	@Inject
	private DiscoRepositorio discoRepositorio;
	
	/** The procesador repositorio. */
	@Inject
	private ProcesadorRepositorio procesadorRepositorio;
	
	/** The memoriaram repositorio. */
	@Inject
	private MemoriaRamRepositorio memoriaramRepositorio;
	
	/** The motherboard repositorio. */
	@Inject
	private MotherboardRepositorio motherboardRepositorio;

}
