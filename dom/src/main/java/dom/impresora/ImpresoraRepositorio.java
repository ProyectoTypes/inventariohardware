package dom.impresora;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import dom.usuario.UsuarioRepositorio;

@Named("IMPRESORA")
public class ImpresoraRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String title() {
		return "IMPRESORA";
	}

	public String iconName() {
		return "IMPRESORA";
	}

	// //////////////////////////////////////
	// Agregar Impresora
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Impresora addImpresora(
			final @Named("Modelo") String modeloImpresora,
			final @Named("Fabricante") String fabricanteImpresora,
			final @Named("Tipo") String tipoImpresora) {
		return nuevaImpresora(modeloImpresora, fabricanteImpresora,
				tipoImpresora, this.currentUserName());
	}

	@Programmatic
	public Impresora nuevaImpresora(final String modeloImpresora,
			final String fabricanteImpresora, final String tipoImpresora,
			final String creadoPor) {
		final Impresora unaImpresora = container
				.newTransientInstance(Impresora.class);
		unaImpresora.setModeloImpresora(modeloImpresora.toUpperCase().trim());
		unaImpresora.setFabricanteImpresora(fabricanteImpresora.toUpperCase()
				.trim());
		unaImpresora.setTipoImpresora(tipoImpresora.toUpperCase().trim());
		unaImpresora.setHabilitado(true);
		unaImpresora.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unaImpresora);
		container.flush();
		return unaImpresora;
	}

	// //////////////////////////////////////
	// Listar Impresora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Impresora> listar() {
		final List<Impresora> listaImpresora = this.container
				.allMatches(new QueryDefault<Impresora>(Impresora.class,
						"eliminarImpresoraTrue", "creadoPor", this
								.currentUserName()));
		if (listaImpresora.isEmpty()) {
			this.container
					.warnUser("No hay Impresoras cargadas en el sistema.");
		}
		return listaImpresora;
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
	private ImpresoraRepositorio impresoraRepositorio;

	@javax.inject.Inject
	private UsuarioRepositorio usuarioRepositorio;
}
