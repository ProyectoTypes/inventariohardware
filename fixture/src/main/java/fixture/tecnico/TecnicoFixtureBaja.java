package fixture.tecnico;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

@SuppressWarnings("deprecation")
public class TecnicoFixtureBaja extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"Tecnico\"");
    }
	@javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;

}
