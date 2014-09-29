package fixture.usuario;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;

public class UsuariosFixture extends FixtureScript {

	public UsuariosFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	// //////////////////////////////////////
	@Override
	protected void execute(ExecutionContext executionContext) {
		// prereqs
		execute(new UsuariosFixtureBaja(), executionContext);
		// create
		List<Sector> listasectores = sectores.listar();
		create(listasectores.get(0), "Perez", "Juan", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(1), "Garcia", "Pedro", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(2), "Vergara", "Fabian", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(3), "Wider", "Exequiel", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(4), "Arancibia", "Juan", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(5), "Villegas", "Diego", "cipoleto@gmail.com",
				executionContext);

	}

	// //////////////////////////////////////

	private Usuario create(final Sector sector, final String apellido,
			final String nombre, final String email,
			ExecutionContext executionContext) {
		return executionContext.add(this,
				usuarios.addUsuario(sector, apellido, nombre, email));
	}

	@javax.inject.Inject
	private UsuarioRepositorio usuarios;
	@javax.inject.Inject
	private SectorRepositorio sectores;

}
