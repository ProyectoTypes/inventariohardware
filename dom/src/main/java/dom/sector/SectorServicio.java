package dom.sector;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

@Named("SECTOR")
public class SectorServicio {
	public SectorServicio()
	{
		
	}
	
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "servicio";
	}

	public String iconName() {
		return "Tecnico";
	}
	// //////////////////////////////////////
	// Insertar un Sector.
	// //////////////////////////////////////
	@MemberOrder(sequence = "10")
	public Sector agregar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombreSector)
	{
		return nuevoSector(nombreSector,this.currentUserName());
	}
	@Programmatic
	public Sector nuevoSector(final String nombreSector,final String creadoPor)
	{
		final Sector unSector = this.container.newTransientInstance(Sector.class);
		unSector.setNombreSector(nombreSector);
		unSector.setHabilitado(true);
		unSector.setCreadoPor(creadoPor);
		this.container.persistIfNotAlready(unSector);
		this.container.flush();
		return unSector;
		
		
	}
	// //////////////////////////////////////
	// ListarTodos
	// //////////////////////////////////////
	@MemberOrder(sequence="20")
	public List<Sector> listarTodos()
	{
		final List<Sector> listarSectores = this.container.allMatches(
				new QueryDefault<Sector>(Sector.class, "todosLosSectores", "creadoPor",this.currentUserName()));
		if(listarSectores.isEmpty())
			this.container.warnUser("No se encontraron sectores cargados en el sistema.");
		return listarSectores;
	}
		
	// //////////////////////////////////////
	// AutoComplete: Servicio utilizado por Sector.
	// //////////////////////////////////////
	@Programmatic
	public List<Sector> autoComplete(final String buscarNombreSector) {
		return container.allMatches(
				new QueryDefault<Sector>(Sector.class,
						"findByCreadoPorAndNombreSector", "creadoPor", this
								.currentUserName(), "nombreSector",
						buscarNombreSector));
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