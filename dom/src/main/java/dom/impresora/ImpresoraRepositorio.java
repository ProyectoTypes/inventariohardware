package dom.impresora;

import org.apache.isis.applib.DomainObjectContainer;

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
