package dom.computadora.hardware.gabinete;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.disco.DiscoRepositorio;
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
		return this.placaderedRepositorio.listar();
	}

	public List<Disco> choices1AddGabinete() {
		return this.discoRepositorio.listar();
	}

	public List<Procesador> choices2AddGabinete() {
		return this.procesadorRepositorio.listar();
	}

	public List<MemoriaRam> choices3AddGabinete() {
		return this.memoriaramRepositorio.listar();
	}

	public List<Motherboard> choices4AddGabinete() {
		return this.motherboardRepositorio.listar();
	}

	private Gabinete nuevoGabinete(final PlacaDeRed placaDeRed, final Disco hdd,
			final Procesador procesador, final MemoriaRam memoriaRam,
			final Motherboard motherboard) {

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
