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
package fixture.permisos;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.permiso.Permiso;
import dom.permiso.PermisoRepositorio;

public class PermisoFixture extends FixtureScript {

	public PermisoFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		// prereqs
		execute(new PermisoFixtureBaja(), executionContext);
		// create
		create("Admin","*",executionContext);
		create("Computadora", "dom.computadora", executionContext);
		create("Impresora", "dom.impresora", executionContext);
		create("Insumo", "dom.insumo", executionContext);
		create("Monitor", "dom.monitor", executionContext);
		create("Monitoreo", "dom.monitoreo", executionContext);
		create("Permiso", "dom.permiso", executionContext);
		create("Rol", "dom.rol", executionContext);
		create("Sector", "dom.sector", executionContext);
		create("Software", "dom.software", executionContext);
		create("Soporte", "dom.soporte", executionContext);
		create("Estados del Soporte", "dom.soporte.estadosoporte",
				executionContext);
		create("Tecnico", "dom.tecnico", executionContext);
		create("Usuario", "dom.usuario", executionContext);
		create("Usuario shiro", "dom.usuarioshiro", executionContext);
		create("Email", "servicio.email", executionContext);
		create("Estadisticas", "servicio.estadisticas", executionContext);
		create("Excel", "servicio.excel", executionContext);
		create("Servicio Monitoreo", "servicio.monitoreo", executionContext);

	}

	// //////////////////////////////////////

	private Permiso create(final String nombre, final String path,
			ExecutionContext executionContext) {
		return executionContext.add(this, permisoRepositorio.addPermiso(nombre, path));
	}

	// //////////////////////////////////////

	@javax.inject.Inject
	private PermisoRepositorio permisoRepositorio;

}
