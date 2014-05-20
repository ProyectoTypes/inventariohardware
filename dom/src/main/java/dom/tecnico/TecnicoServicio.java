package dom.tecnico;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

@Named("TECNICOS")
public class TecnicoServicio {

	public TecnicoServicio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "tecnico";
	}

	public String iconName() {
		return "Tecnico";
	}
	@MemberOrder(sequence = "10")
	public Tecnico agregar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") String apellido,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombre,
			final @Optional @RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") @Named("E-mail") String email) {
		return nuevoTecnico(apellido, nombre, email,
				this.currentUserName());
	}

	@Programmatic
	public Tecnico nuevoTecnico(final String apellido, final String nombre,
			final String email,
			final String creadoPor) {
		final Tecnico unUsuario = container.newTransientInstance(Tecnico.class);
		unUsuario.setApellido(apellido);
		unUsuario.setNombre(nombre);
		unUsuario.setEmail(email);
		unUsuario.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unUsuario);
		container.flush();
		return unUsuario;

	}
	@MemberOrder(sequence="20")
	public List<Tecnico> listarTodos()
	{
		final List<Tecnico> listaTecnicos = this.container.allMatches(
				new QueryDefault<Tecnico>(Tecnico.class, "getAll","creadoPor",this.currentUserName()));
		if(listaTecnicos.isEmpty())
		{
			this.container.warnUser("No hay usuarios cargados en el sistema");
		}
		return listaTecnicos;
				
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