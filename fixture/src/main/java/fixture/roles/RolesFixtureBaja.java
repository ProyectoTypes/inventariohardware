package fixture.roles;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

@SuppressWarnings("deprecation")
public class RolesFixtureBaja  extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        isisJdoSupport.executeUpdate("delete from \"Rol_listaPermisos\"");
        isisJdoSupport.executeUpdate("delete from \"Rol\"");

    }


	@javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;

}
