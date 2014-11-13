package dom.computadora.hardware.gabinete;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.disco.Hdd;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;

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
			final @Named("Hdd") Hdd hdd,
			final @Named("Procesador") Procesador procesador,
			final @Named("Memoria Ram") MemoriaRam memoriaRam,
			final @Named("Motherboard") Motherboard motherboard) {
		return nuevoGabinete(placaDeRed, hdd, procesador, memoriaRam,
				motherboard);
	}

	private Gabinete nuevoGabinete(final PlacaDeRed placaDeRed, final Hdd hdd,
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
}
