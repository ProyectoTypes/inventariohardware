package dom.impresora;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;

import dom.usuario.UsuarioRepositorio;

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
		unaImpresora.setModeloImpresora(modeloImpresora);
		unaImpresora.setFabricanteImpresora(fabricanteImpresora);
		unaImpresora.setTipoImpresora(tipoImpresora);
		container.persistIfNotAlready(unaImpresora);
		container.flush();
		return unaImpresora;

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
