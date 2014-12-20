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
		List<Sector> listasectores = sectores.listAll();
		create(listasectores.get(0), "Perez", "Juan", "cipoleto@gmail.com",
				executionContext);
		create(listasectores.get(1), "Garcia", "Pedro", "oscarsepulveda16@yahoo.com.ar",
				executionContext);
		create(listasectores.get(2), "Wiedermann", "Fernando", "ewiedermann@neuquen.gov.ar",
				executionContext);
		create(listasectores.get(3), "Buffolo", "Laura", "lawiedermann@yahoo.com.ar",
				executionContext);
		create(listasectores.get(4), "Addati", "Soledad", "soledad_addati@yahoo.com.ar",
				executionContext);
		create(listasectores.get(5), "Wiedermann", "Rodrigo", "exequie.wiedermann@gmail.com",
				executionContext);

	}

	// //////////////////////////////////////

	private Usuario create(final Sector sector, final String apellido,
			final String nombre, final String email,
			ExecutionContext executionContext) {
		return executionContext.add(this,
				usuarios.create(sector, apellido, nombre, email));
	}

	@javax.inject.Inject
	private UsuarioRepositorio usuarios;
	@javax.inject.Inject
	private SectorRepositorio sectores;

}
