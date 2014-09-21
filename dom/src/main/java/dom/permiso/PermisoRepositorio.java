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


	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "1")
	@Named("Todos los Permisos")
	public List<Permiso> listAll() {
		return container.allInstances(Permiso.class);
	}


	@MemberOrder(sequence = "2")
	@Named("Nuevo Permiso")
	public Permiso create(
			final @Named("Detalles") String detalles,
			final @Named("Text") String text) {
		final Permiso permiso = container.newTransientInstance(Permiso.class);

		permiso.setNombre(detalles);
		permiso.setPath(text);

		container.persistIfNotAlready(permiso);
		return permiso;
	}


	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(sequence = "4")
	@Named("Eliminar Permiso")
	public String removePermission(@Named("Permiso") Permiso permiso) {
		String permissionDescription = permiso.getNombre();
		container.remove(permiso);
		return "El Permiso: " + permissionDescription + " ha sido eliminado";
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
