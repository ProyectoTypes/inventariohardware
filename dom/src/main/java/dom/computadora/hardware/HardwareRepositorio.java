package dom.computadora.hardware;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;

import dom.computadora.hardware.gabinete.Gabinete;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.monitor.Monitor;

/**
 * Clase HardwareRepositorio.
 */
@DomainService
@Named("Hardware")
@Hidden
public class HardwareRepositorio {
	
	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "hardware";
	}

	/**
	 * Nombre del Icono.
	 * @return 
	 */
	public String iconName() {
		return "Hardware";
	}

	/**
	 * Agregando el hardware.
	 * @param fabricante 
	 * @param monitor 
	 * @param gabinete 
	 * @param impresora 
	 * @return  hardware
	 */
	@NotContributed
	@MemberOrder(sequence = "10")
	@Named("Agregar Hardware")
	public Hardware create(final @Named("Fabricante") String fabricante,
			final @Named("Monitor") Monitor monitor,
			final @Named("Gabinete") Gabinete gabinete,
			final @Named("Impresora") Impresora impresora) {
		return nuevoHardware(fabricante, monitor, gabinete, impresora);
	}

	/**
	 * Nuevo Hardware.
	 * @param fabricante
	 * @param monitor
	 * @param gabinete 
	 * @param impresora
	 * @return hardware
	 */
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

	/**
	 * Inyección del Contenedor.
	 */
	@Inject
	private DomainObjectContainer container;
}
