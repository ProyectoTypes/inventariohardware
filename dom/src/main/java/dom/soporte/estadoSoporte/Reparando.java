package dom.soporte.estadoSoporte;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import servicio.email.EmailService;
import dom.computadora.Computadora.CategoriaDisco;
import dom.computadora.ComputadoraRepositorio;
import dom.impresora.Impresora;
import dom.insumo.Insumo;
import dom.insumo.InsumoRepositorio;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;
import dom.usuario.Usuario;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idReparando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "reparandoUnique", members = { "idReparando" }) })
@ObjectType("REPARANDO")
@Audited
@Bookmarkable
public class Reparando implements IEstado {

	public String title() {
		return "REPARANDO ";
	}

	public String iconName() {
		return "sector";
	}

	public Reparando(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Movimiento (property)
	private Soporte soporte;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	@SuppressWarnings("unused")
	private void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	// }}
	/**
	 * Permite cambiar el Tecnico encargado del Soporte Tecnico.
	 * <p>
	 * Chequea que el nuevo Tecnico este Disponible, en caso afirmativo, de ser
	 * necesario desvincula el Tecnico anterior de la Computadora y del Soporte.
	 * Finalmente vincula el Tecnico nuevo al Soporte.
	 * </p>
	 * <p>
	 * Se mantiene en el estado Reparando
	 * </p>
	 * 
	 * @param tecnico
	 */
	@Override
	public void asignarTecnico(final Tecnico tecnico) {

		if (this.getSoporte().getTecnico().estaDisponible()) {
			if (this.getSoporte().getTecnico() != null) {
				this.getSoporte()
						.getTecnico()
						.desvincularComputadora(
								this.getSoporte().getComputadora());
				this.getSoporte().setTecnico(null);
			}
			this.getSoporte().getTecnico().sumaComputadora();
			this.getSoporte().getTecnico()
					.addToComputadora(this.getSoporte().getComputadora());

			this.container.informUser("ASIGNADO NUEVO TECNICO.");
		} else {
			this.getSoporte().setTecnico(null);
			this.container
					.informUser("EL TECNICO SELECCIONADO NO SE ENCUENTRA DISPONIBLE.");
		}

	}

	/**
	 * Se persiste el Insumo solicitado, luego es vinculado al Soporte.
	 * <p>
	 * Cambio de Estado: Reparando -> Esperando
	 * </p>
	 * 
	 * @param codigo
	 *            ,cantidad,producto,marca,observaciones
	 */
	@Override
	public void solicitarInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones) {
		Insumo unInsumo = this.insumoRepositorio.addInsumo(codigo, cantidad,
				producto, marca, observaciones);
		this.getSoporte().agregarUnInsumo(unInsumo);
		this.getSoporte().setEstado(this.getSoporte().getEsperando());
		this.container.informUser("EN ESPERA DE LOS INSUMOS SOLICITADOS.");
	}

	@Override
	public void finalizarSoporte() {
		emailService.send(this.getSoporte().getComputadora());
		this.getSoporte().getTecnico()
				.desvincularComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getEntregando());
		this.container.informUser("SOPORTE TECNICO FINALIZADO.");
	}

	@Override
	public void noHayInsumos() {
		this.container.informUser("EL EQUIPO CONTINUA EN REPARACION.");

	}

	@Override
	public void llegaronInsumos() {
		this.container.informUser("ES NECESARIO SOLICITAR REPUESTOS.");
	}

	/**
	 * Deshabilita la Computadora, desvinculandolo del Usuario y de la
	 * Impresora. Ingresa al sistema una nueva Computadora con el Usuario
	 * correspondiente.
	 * <p>
	 * Reparando -> Cancelado
	 * </p>
	 * 
	 * @param ip
	 *            ,mother,procesador,disco,memoria,impresora
	 */
	@Override
	public void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {

		Usuario usuario = this.getSoporte().getComputadora().getUsuario();
		this.getSoporte().getComputadora().setHabilitado(false);
		this.getSoporte().getComputadora().setUsuario(null);
		this.getSoporte().getComputadora().setImpresora(null);

		this.computadoraRepositorio.addComputadora(usuario, ip, mother,
				procesador, disco, memoria, impresora);

		this.getSoporte().setEstado(this.getSoporte().getCancelado());
		this.container.informUser("EQUIPO DADO DE BAJA. ASIGNADO NUEVO EQUIPO.");
	}

	@Inject
	private EmailService emailService;
	@Inject
	private DomainObjectContainer container;
	@Inject
	private InsumoRepositorio insumoRepositorio;
	@Inject
	private ComputadoraRepositorio computadoraRepositorio;
}
