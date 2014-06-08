package dom.usuario;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

@Named("USUARIO")
public class UsuarioRepositorio {

	public UsuarioRepositorio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "usuario";
	}

	public String iconName() {
		return "Usuario";
	}

	// //////////////////////////////////////
	// Agregar Usuario
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Usuario add(final @Optional Sector sector,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") String apellido,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombre,
			final @Optional @RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@ [A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") @Named("E-mail") String email
			) {
		return nuevoUsuario(apellido, nombre, email, sector,
				this.currentUserName());
	}

	@Named("Sector")
	@DescribedAs("Buscar el Sector en mayuscula")
	public List<Sector> autoComplete0Add(final @MinLength(2) String search) {
		return sectorRepositorio.autoComplete(search);

	}

	@Programmatic
	public Usuario nuevoUsuario(final String apellido, final String nombre,
			final String email, final Sector sector, final String creadoPor) {
		final Usuario unUsuario = container.newTransientInstance(Usuario.class);
		unUsuario.setApellido(apellido.toUpperCase().trim());
		unUsuario.setNombre(nombre.toUpperCase().trim());
		unUsuario.setEmail(email);
		unUsuario.setSector(sector);
		unUsuario.setHabilitado(true);
		unUsuario.setCreadoPor(creadoPor);
		if(sector!=null)
		{	
			unUsuario.setSector(sector);
			sector.add(unUsuario);
		}
		container.persistIfNotAlready(unUsuario);
		container.flush();
		return unUsuario;

	}

	// //////////////////////////////////////
	// Listar Usuario
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Usuario> listar() {
		final List<Usuario> listaUsuarios = this.container
				.allMatches(new QueryDefault<Usuario>(Usuario.class,
						"eliminarUsuarioTrue", "creadoPor", this
								.currentUserName()));
		if (listaUsuarios.isEmpty()) {
			this.container.warnUser("No hay Usuarios cargados en el sistema.");
		}
		return listaUsuarios;

	}

	// //////////////////////////////////////
	// Buscar Usuario
	// //////////////////////////////////////

	@MemberOrder(sequence = "30")
	public List<Usuario> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") @MinLength(2) String apellido) {
		final List<Usuario> listarUsuarios = this.container
				.allMatches(new QueryDefault<Usuario>(Usuario.class,
						"buscarPorApellido", "creadoPor", this
								.currentUserName(), "apellido", apellido
								.toUpperCase().trim()));
		if (listarUsuarios.isEmpty())
			this.container
					.warnUser("No se encontraron Usuarios cargados en el sistema.");
		return listarUsuarios;
	}

	@Programmatic
	public List<Usuario> autoComplete(final String apellido) {
		return container.allMatches(new QueryDefault<Usuario>(Usuario.class,
				"autoCompletePorApellido", "creadoPor", this.currentUserName(),
				"apellido", apellido.toUpperCase().trim()));
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
	private SectorRepositorio sectorRepositorio;
}