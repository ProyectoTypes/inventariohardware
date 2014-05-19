package dom.usuario;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;

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
		return "ToDoItem";
	}

	public Usuario agregarUsuario(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") String apellido,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombre,
			final @RegEx(validation = "\\d{6,8}") @Named("DNI") String documento,
			final @RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") @Named("E-mail") String email,
			final @RegEx(validation = "^[0-9]{2,3}-? ?[0-9]{6,7}$") @Named("Celular") int celular) {
		return newUser(apellido, nombre, documento, email, celular,
				this.currentUserName());
	}

	public Usuario newUser(final String apellido, final String nombre,
			final String documento, final String email, final int celular,
			final String creadoPor) {
		final Usuario unUsuario = container.newTransientInstance(Usuario.class);
		unUsuario.setApellido(apellido);
		unUsuario.setNombre(nombre);
		unUsuario.setDocumento(documento);
		unUsuario.setEmail(email);
		unUsuario.setCelular(celular);
		unUsuario.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unUsuario);
		container.flush();
		return unUsuario;

	}

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;

}