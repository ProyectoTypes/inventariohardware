package fixture.tecnico;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.sector.Sector;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;
import fixture.sector.SectorFixtureBaja;

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
//		Sector sector = new Sector();
//		sector.setNombreSector("Informatica");
		// create
		create(null,"Munoz","Daniel","cipoleto@gmail.com", executionContext);
	

	}

	@javax.inject.Inject
	private TecnicoRepositorio tecnicos;
}
