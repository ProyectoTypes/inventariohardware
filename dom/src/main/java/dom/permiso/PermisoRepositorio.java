package dom.permiso;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

@DomainService(menuOrder = "45", repositoryFor = Permiso.class)
@Named("Permisos")
public class PermisoRepositorio {

	public String getId() {
		return "Permiso";
	}

	public String iconName() {
		return "Tecnico";
	}



	@MemberOrder(sequence = "2")
	@Named("Nuevo Permiso")
	public Permiso add(
			final @Named("Nombre") String nombre,
			final @Named("Path") String path) {
		final Permiso permiso = container.newTransientInstance(Permiso.class);

		permiso.setNombre(nombre);
		permiso.setPath(path);

		container.persistIfNotAlready(permiso);
		return permiso;
	}


	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(sequence = "4")
	@Named("Eliminar Permiso")
	public String eliminar(@Named("Permiso") Permiso permiso) {
		String permissionDescription = permiso.getNombre();
		container.remove(permiso);
		return "El Permiso: " + permissionDescription + " ha sido eliminado";
	}


	
	@javax.inject.Inject
	DomainObjectContainer container;

}
