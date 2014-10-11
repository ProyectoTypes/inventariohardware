package dom.soporte.estadosoporte;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
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

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idReparando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "reparandoUnique", members = { "idReparando" }) })
@ObjectType("REPARANDO")
public class Reparando implements IEstado {

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return "REPARANDO ";
	}

	public String iconName() {
		return "Reparando";
	}

	public Reparando(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Soporte (property)
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
	@Hidden
	public void asignarTecnico(final Tecnico tecnico) {

		if (this.getSoporte().getTecnico().estaDisponible()) {
			if (this.getSoporte().getTecnico() != null) {
				this.getSoporte().getTecnico()
						.restaComputadora(this.getSoporte().getComputadora());
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
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param observaciones
	 */
	@Override
	@Hidden
	public void solicitarInsumos(final int cantidad, final String producto,
			final String marca, final String modelo) {
		Insumo unInsumo = this.insumoRepositorio.addInsumo(cantidad, producto,
				marca, modelo);
		this.getSoporte().agregarUnInsumo(unInsumo);
		this.getSoporte().clearTecnico();
		this.getSoporte().setEstado(this.getSoporte().getEsperando());
		this.container.informUser("EN ESPERA DE LOS INSUMOS SOLICITADOS.");
	}

	/**
	 * Permite enviar un correo al Usuario informandole que el Soporte Tecnico
	 * ha finalizado.
	 * <p>
	 * Reparando -> Entregado
	 * </p>
	 */
	@Override
	@Hidden
	public void finalizarSoporte() {
		// Enviando email.
		emailService.send(this.getSoporte().getComputadora());
		// Desvinculando tecnico/computadora.
		this.getSoporte().getTecnico()
				.removeFromComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getEntregando());
		this.container
				.informUser("SOPORTE TECNICO FINALIZADO. EL USUARIO HA SIDO NOTIFICADO VIA EMAIL.");
	}

	/**
	 * No es posible realizar el Soporte ya que los insumos no se encuentran
	 * disponibles, se debera asignar una nueva Computadora al Usuario.
	 * <p>
	 * El Usuario será desvinculado de la Computadora anterior, al igual que el
	 * Tecnico. Impresora deberá ser desvinculada de la Computadora.
	 * </p>
	 * <p>
	 * Cambio de Estado: Esperando -> Cancelado
	 * </p>
	 * 
	 * @param ip
	 * @param mother
	 * @param procesador
	 * @param disco
	 * @param memoria
	 * @param impresora
	 */
	@Override
	@Hidden
	public void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
		// Creando nueva computadora.
		this.computadoraRepositorio.addComputadora(this.getSoporte()
				.getComputadora().getUsuario(), ip, mother, procesador, disco,
				memoria, impresora);

		// Desvinculando Usuario/Tecnico/Impresora de Computadora -
		this.getSoporte().getTecnico()
				.removeFromComputadora(this.getSoporte().getComputadora());

		this.getSoporte().getComputadora().limpiarImpresora();
		this.getSoporte().getComputadora().setHabilitado(false);
		this.getSoporte().setEstado(this.getSoporte().getCancelado());
		this.container
				.informUser("EQUIPO DADO DE BAJA. ASIGNADO NUEVO EQUIPO.");
	}

	@Override
	@Hidden
	public boolean escondeAsignarTecnico() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeFinalizarSoporte() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeSolicitarInsumos() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeLlegaronInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeNoHayInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeAsignarNuevoEquipo() {
		return false;
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
