package fixture.permisos;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.permiso.Permiso;
import dom.permiso.PermisoRepositorio;

public class PermisoFixture extends FixtureScript {

	public PermisoFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		// prereqs
		execute(new PermisoFixtureBaja(), executionContext);
		// create
		create("Admin","*",executionContext);
		create("Computadora", "dom.computadora", executionContext);
		create("Impresora", "dom.impresora", executionContext);
		create("Insumo", "dom.insumo", executionContext);
		create("Monitor", "dom.monitor", executionContext);
		create("Monitoreo", "dom.monitoreo", executionContext);
		create("Permiso", "dom.permiso", executionContext);
		create("Rol", "dom.rol", executionContext);
		create("Sector", "dom.sector", executionContext);
		create("Software", "dom.software", executionContext);
		create("Soporte", "dom.soporte", executionContext);
		create("Estados del Soporte", "dom.soporte.estadosoporte",
				executionContext);
		create("Tecnico", "dom.tecnico", executionContext);
		create("Usuario", "dom.usuario", executionContext);
		create("Usuario shiro", "dom.usuarioshiro", executionContext);
		create("Email", "servicio.email", executionContext);
		create("Estadisticas", "servicio.estadisticas", executionContext);
		create("Excel", "servicio.excel", executionContext);
		create("Servicio Monitoreo", "servicio.monitoreo", executionContext);

	}

	// //////////////////////////////////////

	private Permiso create(final String nombre, final String path,
			ExecutionContext executionContext) {
		return executionContext.add(this, permisoRepositorio.addPermiso(nombre, path));
	}

	// //////////////////////////////////////

	@javax.inject.Inject
	private PermisoRepositorio permisoRepositorio;

}
