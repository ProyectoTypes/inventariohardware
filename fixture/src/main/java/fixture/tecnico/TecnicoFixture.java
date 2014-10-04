package fixture.tecnico;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.sector.Sector;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

public class TecnicoFixture extends FixtureScript  {

	public TecnicoFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	// //////////////////////////////////////

	private Tecnico create(final Sector sector,final String apellido, final String nombre, final String email, ExecutionContext executionContext) {
		return executionContext.add(this, tecnicos.addTecnico(sector, apellido, nombre, email));
	}

	// //////////////////////////////////////
	@Override
	protected void execute(ExecutionContext executionContext) {

		// prereqs
		execute(new TecnicoFixtureBaja(), executionContext);
		// create
		create(null,"Munoz","Daniel","cipoleto@gmail.com", executionContext);
		create(null,"Sanchez","Lucas","cipoleto@gmail.com", executionContext);
		create(null,"Rodriguez","Antonio","cipoleto@gmail.com", executionContext);
		create(null,"Massaro","Diego","cipoleto@gmail.com", executionContext);
		create(null,"Cassanova","Carla","cipoleto@gmail.com", executionContext);


	}

	@javax.inject.Inject
	private TecnicoRepositorio tecnicos;
}
