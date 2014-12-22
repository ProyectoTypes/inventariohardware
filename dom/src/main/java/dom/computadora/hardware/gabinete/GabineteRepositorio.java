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

/**
 * Clase GabineteRepositorio.
 */
@DomainService
@Named("Gabinete")
@Hidden
public class GabineteRepositorio {

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "gabinete";
	}

	/**
	 * Nombre del Icono.
	 * @return the string
	 */
	public String iconName() {
		return "Gabinete";
	}

	/**
	 * Agregar Gabinete.
	 * @param ip 
	 * @param mac 
	 * @param marcaDisco 
	 * @param tipoDisco 
	 * @param tamanoDisco 
	 * @param modeloProcesador 
	 * @param modeloRam 
	 * @param tamanoRam
	 * @param marcaRam
	 * @param modeloMotherboard
	 * @return
	 */
	@MemberOrder(sequence = "10")
	@Named("Agregar Gabinete")
	public Gabinete create(final @Named("IP") String ip,
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
	 * Método que lista las Placas de Red.
	 * @return
	 */
	public List<PlacaDeRed> choices0AddGabinete() {
		return this.placaderedRepositorio.listAll();
	}

	/**
	 * Método que lista los Disos Rígidos.
	 * @return
	 */
	public List<Disco> choices1AddGabinete() {
		return this.discoRepositorio.listAll();
	}

	/**
	 * Método que lista los Procesadores.
	 * @return
	 */
	public List<Procesador> choices2AddGabinete() {
		return this.procesadorRepositorio.listAll();
	}

	/**
	 * Método que lista las Memorias.
	 * @return 
	 */
	public List<MemoriaRam> choices3AddGabinete() {
		return this.memoriaramRepositorio.listAll();
	}

	/**
	 * Método que lista las Motherboards.
	 * @return 
	 */
	public List<Motherboard> choices4AddGabinete() {
		return this.motherboardRepositorio.listAll();
	}

	/**
	 * Nuevo gabinete.
	 * @param placaDeRed
	 * @param hdd 
	 * @param procesador 
	 * @param memoriaRam 
	 * @param motherboard 
	 * @return 
	 */
	private Gabinete nuevoGabinete(final PlacaDeRed placaDeRed,
			final Disco hdd, final Procesador procesador,
			final MemoriaRam memoriaRam, final Motherboard motherboard) {

		Gabinete obj = this.container.newTransientInstance(Gabinete.class);
		obj.agregarHdd(hdd);
		obj.agregarMemoriaRam(memoriaRam);
		obj.setPlacaDeRed(placaDeRed);
		obj.setMotherboard(motherboard);
		obj.setProcesador(procesador);
		this.container.persistIfNotAlready(obj);
		this.container.flush();
		return obj;
	}
	
	/**
	 * Inyección del Contenedor.
	 */
	@Inject
	private DomainObjectContainer container;
	
	/**
	 * Inyección del servicio para Placas de Red.
	 */
	@Inject
	private PlacaDeRedRepositorio placaderedRepositorio;
	
	/**
	 * Inyección del servicio para Disos Rígidos.
	 */
	@Inject
	private DiscoRepositorio discoRepositorio;
	
	/**
	 * Inyección del servicio para Procesador.
	 */
	@Inject
	private ProcesadorRepositorio procesadorRepositorio;
	
	/**
	 * Inyección del servicio para Memoria.
	 */
	@Inject
	private MemoriaRamRepositorio memoriaramRepositorio;

	/**
	 * Inyección del servicio para Mother.
	 */
	@Inject
	private MotherboardRepositorio motherboardRepositorio;
}