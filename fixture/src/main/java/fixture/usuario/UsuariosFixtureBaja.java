package fixture.usuario;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

@SuppressWarnings("deprecation")
public class UsuariosFixtureBaja extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"Usuario\"");

    }
    @javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;
}
