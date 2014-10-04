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
		create(null,"Vergara","Nicolas","nicolas.vergara@proyectotypes.com.ar", executionContext);
		create(null,"Sepulveda","Oscar","oscar.sepulveda@proyectotypes.com.ar", executionContext);
		create(null,"Munoz","Daniel","daniel.munoz@proyectotypes.com.ar", executionContext);
		create(null,"Wiedermann","Exequiel","exequie.wiedermann@proyectotypes.com.ar", executionContext);
	}

	@javax.inject.Inject
	private TecnicoRepositorio tecnicos;
}
