package dom.usuario;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

@Named("USUARIOS")
public class UsuarioServicio {

	public UsuarioServicio() {

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
	@MemberOrder(sequence = "10")
	public Usuario agregar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") String apellido,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombre,
			final @Optional @RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") @Named("E-mail") String email,
			final @RegEx(validation = "^[0-9]{2,3}-? ?[0-9]{6,7}$") @Named("Celular") int celular) {
		return newUser(apellido, nombre, email, celular,
				this.currentUserName());
	}

	@Programmatic
	public Usuario newUser(final String apellido, final String nombre,
			final String email, final int celular,
			final String creadoPor) {
		final Usuario unUsuario = container.newTransientInstance(Usuario.class);
		unUsuario.setApellido(apellido);
		unUsuario.setNombre(nombre);
		unUsuario.setEmail(email);
		unUsuario.setCelular(celular);
		unUsuario.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unUsuario);
		container.flush();
		return unUsuario;

	}
	@MemberOrder(sequence="20")
	public List<Usuario> listarTodos()
	{
		final List<Usuario> listaUsuarios = this.container.allMatches(
				new QueryDefault<Usuario>(Usuario.class, "getAll","creadoPor",this.currentUserName()));
		if(listaUsuarios.isEmpty())
		{
			this.container.warnUser("No hay usuarios cargados en el sistema");
		}
		return listaUsuarios;
				
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

}