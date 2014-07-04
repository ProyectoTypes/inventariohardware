package dom.movimiento;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

@Named("Movimiento")
public class MovimientoRepositorio {

	public MovimientoRepositorio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "movimiento";
	}

	public String iconName() {
		return "Tecnico";
	}

	// //////////////////////////////////////
	// Insertar un Movimiento.
	// //////////////////////////////////////
	
	@Named("Recepcion")
	@MemberOrder(sequence = "10")
	@PublishedAction
	public Movimiento add(final @Named("Computadora") Computadora computadora,
			final @Named("Tecnico") Tecnico tecnico,
			final @Named("Observaciones") String observaciones) {
		return nuevoMovimiento(computadora, tecnico, observaciones, this.currentUserName());
	}
	
	public List<Computadora> autoComplete0Add(final @MinLength(2) String search) {
		List<Computadora> listaComputadora = computadoraRepositorio.autoComplete(search);
		return listaComputadora;
	}
	public List<Tecnico> autoComplete1Add(final @MinLength(2) String search) {
		List<Tecnico> listaTecnicos = tecnicoRepositorio.autoComplete(search);
		return listaTecnicos;
	}
	
	@Programmatic
	public Movimiento nuevoMovimiento(final Computadora computadora, final Tecnico tecnico, final String observaciones, 
				final String creadoPor) {
		final Movimiento unMovimiento = this.container.newTransientInstance(Movimiento.class);
		unMovimiento.setHabilitado(true);
		unMovimiento.setCreadoPor(creadoPor);
		unMovimiento.setComputadora(computadora);
		unMovimiento.setTecnico(tecnico);
		unMovimiento.setObservaciones(observaciones);
		unMovimiento.setFecha(LocalDate.now());
		this.container.persistIfNotAlready(unMovimiento);
		this.container.flush();
		return unMovimiento;

	}

	// ///////////////////////////////////////
	// AutoComplete
	// ///////////////////////////////////////
	
	/**
	 * Servicio utilizado por Sector
	 * 
	 */
	@Programmatic
	public List<Movimiento> autoComplete(final String buscarTecnico) {
		return container.allMatches(new QueryDefault<Movimiento>(
				Movimiento.class, "autoCompleteMovimiento", "creadoPor", this
						.currentUserName(), "buscarTecnico", buscarTecnico
						.toUpperCase().trim()));
	}

	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Movimiento> listar() {
		final List<Movimiento> listaMovimientos = this.container
				.allMatches(new QueryDefault<Movimiento>(Movimiento.class,
						"listar", "creadoPor", this
								.currentUserName()));
		if (listaMovimientos.isEmpty()) {
			this.container
					.warnUser("No hay Movimiento cargados en el sistema.");
		}
		return listaMovimientos;
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
	
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;
	
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}
