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
package fixture.sector;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

public class SectorFixture extends FixtureScript {

	public SectorFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	// //////////////////////////////////////

	private Sector create(final String nombre, ExecutionContext executionContext) {
		return executionContext.add(this, sectores.create(nombre));
	}

	// //////////////////////////////////////
	@Override
	protected void execute(ExecutionContext executionContext) {

		// prereqs
		execute(new SectorFixtureBaja(), executionContext);

		// create
		create("Administración", executionContext);
		create("Informática", executionContext);
		create("Ventas", executionContext);
		create("Contaduria", executionContext);
		create("Mesa de Entrada", executionContext);
		create("Depósito", executionContext);

	}

	@javax.inject.Inject
	private SectorRepositorio sectores;

}
