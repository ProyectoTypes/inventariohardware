package fixture.roles;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.permiso.Permiso;
import dom.permiso.PermisoRepositorio;
import dom.rol.Rol;
import dom.rol.RolRepositorio;

public class RolesFixture extends FixtureScript {

	public RolesFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		// prereqs
		execute(new RolesFixtureBaja(), executionContext);
		// create
		List<Permiso> permisos = permisoRepositorio.listAll();
		create("Admin",permisos, executionContext);
		permisos.remove(0);
		permisos.remove(5);
		permisos.remove(5);
		permisos.remove(11);
		create("Base",permisos,executionContext);
		// create
	
	}

	// //////////////////////////////////////

	private Rol create(final String nombre, final List<Permiso> permisos,
			ExecutionContext executionContext) {
		return executionContext.add(this, rolRepositorio.addRol(nombre, permisos));
	}

	// //////////////////////////////////////

	@javax.inject.Inject
	private PermisoRepositorio permisoRepositorio;
	@javax.inject.Inject
	private RolRepositorio rolRepositorio;
}
