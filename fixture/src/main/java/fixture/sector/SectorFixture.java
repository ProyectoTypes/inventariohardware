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

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScript.Discoverability;
import org.apache.isis.applib.fixturescripts.FixtureScript.ExecutionContext;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;
import fixture.datos.DatosFixture;
import fixture.datos.DatosFixtureBaja;

public class SectorFixture extends FixtureScript {

	public SectorFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		
		int Cantidad=DatosFixture.ObtenerCantidad()*14;
		
		List<Sector> listSec = new ArrayList<Sector>();
		
		// Crea los Sectores de manera aleatoria.
		for(int x=0; x<=Cantidad;x++)
		{
			Sector sector = new Sector();
			Sector.setSector(DatosFixture.ObtenerSector());
			
			listSec.add(sector);
     	}
        	for(Sector sec:removerrepetidos(listSec))
        		create(sec.getSector(), executionContext);
	}

	/**
	 * Lista los Sectores y remueve los repetidos.
	 * @param listaUsuario
	 * @return
	 */
	private List<Sector> removerrepetidos(List<Sector> listaSector) {
		for (int x = 0; x < listaSector.size() - 1; x++) {
			for (int y = x + 1; y < listaSector.size(); y++) {
				if (listaSector.get(x).getSector().equals(listaSector.get(y).getSector())) {
					listaSector.remove(y);
				}
			}
		}
		return listaSector;
	}
	
	/**
	 * Crear un Sector.
	 * @param nombre
	 * @param executionContext
	 * @return
	 */
	private Sector create(final String nombre, ExecutionContext executionContext) {
		return executionContext.add(this, sectores.create(nombre));
	}
	
	/**
	 * 
	 * @param executionContext
	 */
	public void BorrarDBAlumnos(ExecutionContext executionContext) {
		execute(new DatosFixtureBaja("Usuario"), executionContext);
	        return;
	}
	    
	/**
	 * Inyección del servicio Sector.
	 */
	@javax.inject.Inject
	private SectorRepositorio sectores;
}