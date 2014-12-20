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
package fixture.instalar;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScript.Discoverability;
import org.apache.isis.applib.fixturescripts.FixtureScript.ExecutionContext;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;
import fixture.datos.DatosFixture;
import fixture.datos.DatosFixtureBaja;

public class SectorFixture extends FixtureScript{

	public SectorFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }
	
	@Override
	protected void execute(ExecutionContext executionContext) {
		
		int Cantidad=3;
		

		List<Sector> lSector = new ArrayList<Sector>();
		
		int refcantidad=Cantidad;
		
		for (int x = 0; x < Cantidad; x++) {
			// Lcursos.add(createCurso(plan, anio, GenericData.ObtenerLetras(x),
			// turno[valorturno], executionContext));

			lSector.add(create(DatosFixture.ObtenerSector(), executionContext));
			
			this.container.informUser("Entro hasta aqui");
		}
	}
	
    private Sector create(final String nombreSector, ExecutionContext executionContext){
    	return executionContext.add(this, sectorRepositorio.create(nombreSector));
    }
        
    @javax.inject.Inject
    private SectorRepositorio sectorRepositorio;
}