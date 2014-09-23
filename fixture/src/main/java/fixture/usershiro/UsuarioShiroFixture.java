package fixture.usershiro;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.rol.Rol;
import dom.rol.RolRepositorio;
import dom.usuarioshiro.UsuarioShiro;
import dom.usuarioshiro.UsuarioShiroRepositorio;
import fixture.roles.RolesFixture;

public class UsuarioShiroFixture extends FixtureScript {

	public UsuarioShiroFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		// prereqs
		execute(new UsuarioShiroFixtureBaja(), executionContext);
		// create
		execute(new RolesFixture(), executionContext);
		List<Rol> listaDeRoles = this.rolesRepositorio.listAll();
		create("sven", "pass", listaDeRoles, executionContext);
		listaDeRoles.remove(0);
		create("bobo", "bobo", listaDeRoles, executionContext);

	}

	// //////////////////////////////////////

	private UsuarioShiro create(final String nick, final String password,
			final List<Rol> rol, ExecutionContext executionContext) {
		return executionContext.add(this,
				usuarioRepositorio.addUsuarioShiro(nick, password, rol));
	}

	// //////////////////////////////////////

	@javax.inject.Inject
	private UsuarioShiroRepositorio usuarioRepositorio;

	@javax.inject.Inject
	private RolRepositorio rolesRepositorio;
}
