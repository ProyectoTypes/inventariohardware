package dom.movimiento;

import java.util.List;

import javax.inject.Named;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.LocalDate;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;
import dom.movimiento.estadoComputadora.Cancelado;
import dom.movimiento.estadoComputadora.IEstado;
import dom.movimiento.estadoComputadora.Recepcionado;
import dom.movimiento.estadoComputadora.Reparando;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Movimiento_observaciones_must_be_unique", members = { "fecha,creadoPor,observaciones" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorMovimiento", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor && "
				+ "tecnico.getNombre().indexOf(:buscarTecnico) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && computadora.getIp().indexOf(:ip) >= 0"), })
@ObjectType("MOVIMIENTO")
@Audited
@AutoComplete(repository = MovimientoRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Movimiento implements Comparable<Movimiento> {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	public String title() {
		return "Movimiento";
	}

	public String iconName() {
		return "Movimiento";
	}

	// //////////////////////////////////////
	// Obeservaciones (propiedad)
	// //////////////////////////////////////

	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones de la Computadora:")
	@MemberOrder(sequence = "40")
	@MultiLine(numberOfLines = 10)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(final String observaciones) {
		this.observaciones = observaciones;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	private boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "40")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	// //////////////////////////////////////
	// fecha (propiedad)
	// //////////////////////////////////////

	private LocalDate fecha;

	@Disabled
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "20")
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	// //////////////////////////////////////
	// creadoPor (propiedad)
	// //////////////////////////////////////

	private String creadoPor;

	@Hidden(where = Where.ALL_TABLES)
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	// //////////////////////////////////////
	// Relacion Computadora/Movimiento.
	// //////////////////////////////////////
	private Computadora computadora;

	@MemberOrder(sequence = "100")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Computadora getComputadora() {
		return computadora;
	}

	public void setComputadora(Computadora computadora) {
		this.computadora = computadora;
	}

	@Named("Cambiar Computadora")
	public void modificarComputadora(final Computadora unaComputadora) {
		Computadora currentComputadora = getComputadora();
		// check for no-op
		if (unaComputadora == null || unaComputadora.equals(currentComputadora)) {
			return;
		}
		// associate new
		setComputadora(unaComputadora);
		// additional business logic
		// onModifyComputadora(currentComputadora, unaComputadora);
	}

	public void clearComputadora() {
		Computadora currentComputadora = getComputadora();
		// check for no-op
		if (currentComputadora == null) {
			return;
		}
		// dissociate existing
		setComputadora(null);
		// additional business logic
		// onClearComputadora(currentComputadora);
	}

	public List<Computadora> autoComplete0ModificarComputadora(
			final String search) {
		return this.computadoraRepositorio.autoComplete(search);
	}

	// //////////////////////////////////////
	// Relacion Tecnico/Movimiento.
	// //////////////////////////////////////

	private Tecnico tecnico;

	@Optional
	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	@Named("Reparador")
	public Movimiento modificarTecnico(final Tecnico unTecnico) {
		Tecnico currentTecnico = this.getTecnico();
		// check for no-op
		if (unTecnico == null || unTecnico.equals(currentTecnico)) {
			return this;
		}
		// associate new
		setTecnico(unTecnico);
		
		// Logica de Negocio: Agregar un nuevo estado (2).
		this.container.warnUser("Estado: " + this.getEstado().toString());
		this.estado.equipoRecibido();
		
		this.container.warnUser("Estado2: " + this.getEstado().toString()
				+ "clase: " + this.getEstado().getClass());
		
		this.setEstadoActual(this.estado.toString());
		return this;
	}

	private String estadoActual;

	@Disabled
	@MemberOrder(sequence = "300")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public String getEstadoActual() {
		return this.estadoActual;
	}

	public void setEstadoActual(String mostrarEstado) {
		this.estadoActual = mostrarEstado;
	}

	/**
	 * PATRON STATE
	 */
	// Atributos del Contexto
	private IEstado recepcionado;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public IEstado getRecepcionado() {
		return this.recepcionado;
	}

	@Programmatic
	public void setRecepcionado(IEstado recepcionado) {
		this.recepcionado = recepcionado;
	}

	private IEstado reparando;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public IEstado getReparando() {
		return reparando;
	}

	@Programmatic
	public void setReparando(IEstado reparando) {
		this.reparando = reparando;
	}

	private IEstado cancelado;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public IEstado getCancelado() {
		return this.cancelado;
	}

	@Programmatic
	public void setCancelado(IEstado cancelado) {
		this.cancelado = cancelado;
	}

	// FIN: Atributos del Contexto
	// Constructor
	public Movimiento() {
		// this.container.warnUser("Constructor: ");
		this.recepcionado = new Recepcionado(this);
		this.reparando = new Reparando(this);
		this.cancelado = new Cancelado(this);
		this.estado = recepcionado;
		this.estadoActual = this.estado.toString();
	}

	// FIN: Constructor
	// Operaciones de la Interface.
	@Programmatic
	public void equipoRecibido() {
		this.estado.equipoRecibido();
	}
	@Programmatic
	public void equipoReparado() {
		this.estado.equipoReparado();
	}
	@Programmatic
	public void equipoFinalizado() {
		this.estado.equipoFinalizado();
	}

	// FIN: Operaciones de la Interface.
	// Atributo estado.
	private IEstado estado;

	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "true")
	public IEstado getEstado() {
		return estado;
	}

	public void setEstado(IEstado estado) {
		this.estado = estado;
	}

	public String toString() {
		return "Estado Actual de Movimiento: " + this.estado.toString();
	}

	public void clearTecnico() {
		Tecnico currentTecnico = getTecnico();
		// check for no-op
		if (currentTecnico == null) {
			return;
		}
		// dissociate existing
		setTecnico(null);
	}

	public List<Tecnico> choices0ModificarTecnico() {
		return tecnicoRepositorio.listar();
	}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////
	@Override
	public int compareTo(final Movimiento movimiento) {
		return ObjectContracts.compare(this, movimiento,
				"fecha,creadoPor,observaciones");
	}

	// ////////////////////////////////////
	// Injected Services
	// ////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;

	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}