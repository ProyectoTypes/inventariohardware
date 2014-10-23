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
