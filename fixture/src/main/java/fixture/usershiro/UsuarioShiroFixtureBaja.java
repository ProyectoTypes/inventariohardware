package fixture.usershiro;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

@SuppressWarnings("deprecation")
public class UsuarioShiroFixtureBaja  extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"UsuarioShiro_listaDeRoles\"");
        isisJdoSupport.executeUpdate("delete from \"UsuarioShiro\"");

    }


	@javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;

}
