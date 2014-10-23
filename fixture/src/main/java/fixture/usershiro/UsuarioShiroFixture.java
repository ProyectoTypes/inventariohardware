/*
 * This is a software made for inventory control
 * 
 * Copyright (C) 2014, ProyectoTypes
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * 
 * 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/
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
