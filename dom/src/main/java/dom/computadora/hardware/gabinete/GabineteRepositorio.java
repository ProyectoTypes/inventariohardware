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

	public String iconName() {
		return "Gabinete";
	}

	// //////////////////////////////////////
	// Agregar
	// //////////////////////////////////////
	
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

	public List<PlacaDeRed> choices0AddGabinete() {
		return this.placaderedRepositorio.listAll();
	}

	public List<Disco> choices1AddGabinete() {
		return this.discoRepositorio.listAll();
	}

	public List<Procesador> choices2AddGabinete() {
		return this.procesadorRepositorio.listAll();
	}

	public List<MemoriaRam> choices3AddGabinete() {
		return this.memoriaramRepositorio.listAll();
	}

	public List<Motherboard> choices4AddGabinete() {
		return this.motherboardRepositorio.listAll();
	}

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

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	
	@Inject
	private DomainObjectContainer container;
	
	@Inject
	private PlacaDeRedRepositorio placaderedRepositorio;
	
	@Inject
	private DiscoRepositorio discoRepositorio;
	
	@Inject
	private ProcesadorRepositorio procesadorRepositorio;
	
	@Inject
	private MemoriaRamRepositorio memoriaramRepositorio;
	
	@Inject
	private MotherboardRepositorio motherboardRepositorio;
}
