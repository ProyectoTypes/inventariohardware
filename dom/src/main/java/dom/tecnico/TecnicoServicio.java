package dom.tecnico;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
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
	
	
	// //////////////////////////////////////
    // NotYetComplete (action)
    // //////////////////////////////////////
	
	@Hidden
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "10")
	public List<Tecnico> noCompletados(){
		final List<Tecnico> listaTecnicos = peroCompletos();
		if(listaTecnicos.isEmpty()){
			container.informUser("Se borró la información solicitada");
		}
		return listaTecnicos;
	}
	
	
	@Programmatic
	public List<Tecnico> peroCompletos(){
		return container.allMatches(
				new QueryDefault<Tecnico>(Tecnico.class, 
						"eliminarTecnicoTrue",
						"creadoPor", currentUserName()));
	}
	
	
	@MemberOrder(sequence = "30")
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
		final Tecnico unTecnico = container.newTransientInstance(Tecnico.class);
		unTecnico.setApellido(apellido);
		unTecnico.setNombre(nombre);
		unTecnico.setEmail(email);
		unTecnico.setHabilitado(true);
		unTecnico.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unTecnico);
		container.flush();
		return unTecnico;

	}
	@MemberOrder(sequence="20")
	public List<Tecnico> listarTodos()
	{
		final List<Tecnico> listaTecnicos = this.container.allMatches(
				new QueryDefault<Tecnico>(Tecnico.class, "eliminarTecnicoTrue","creadoPor",this.currentUserName()));
		if(listaTecnicos.isEmpty())
		{
			this.container.warnUser("No hay tecnicos cargados en el sistema");
		}
		return listaTecnicos;
				
	}
	
	
    // //////////////////////////////////////
    // AutoComplete
    // //////////////////////////////////////

    @Programmatic // not part of metamodel
    public List<Tecnico> autoComplete(final String apellido) {
        return container.allMatches(
                new QueryDefault<Tecnico>(Tecnico.class, 
                        "findByCreadoPorByAndApellidoContains", 
                        "creadoPor", currentUserName(), 
                        "apellido", apellido));
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