package dom.computadora.hardware;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.Gabinete;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.monitor.Monitor;

@DomainService
@Named("Hardware")
@Hidden
public class HardwareRepositorio {
	
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "hardware";
	}

	public String iconName() {
		return "Hardware";
	}
	
	// //////////////////////////////////////
	// Agregar
	// //////////////////////////////////////
	
	@MemberOrder(sequence = "10")
	@Named("Agregar Hardware")
	public Hardware create(final @Named("Fabricante") String fabricante,
			final @Named("Monitor") Monitor monitor,
			final @Named("Gabinete") Gabinete gabinete,
			final @Named("Impresora") Impresora impresora) {
		return nuevoHardware(fabricante, monitor, gabinete, impresora);
	}

	private Hardware nuevoHardware(final String fabricante,
			final Monitor monitor, final Gabinete gabinete,
			final Impresora impresora) {

		Hardware hardware = this.container.newTransientInstance(Hardware.class);
		hardware.setFabricante(fabricante);
		hardware.setGabinete(gabinete);
		hardware.setImpresora(impresora);
		hardware.setMonitor(monitor);
		this.container.persistIfNotAlready(hardware);
		this.container.flush();
		return hardware;
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	
	@Inject
	private DomainObjectContainer container;
}