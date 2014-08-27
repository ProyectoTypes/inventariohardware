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
package dom.monitor;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@Named("Monitor")

public class MonitorRepositorio {
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String title() {
		return "Monitor";
	}

	public String iconName() {
		return "Monitor";
	}
	
	// //////////////////////////////////////
	// Agregar Monitor
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Monitor addMonitor(final @Named("Codigo") String codigo,
			final @Named("Tipo") int tipo,
			final @Named("Nombre") String nombre,
			final @Named("Marca") String marca,
			final @Optional @Named("Observaciones") String observaciones) {
		return nuevosMonitor(codigo, tipo, nombre, marca, observaciones,
				this.currentUserName());
	}
	
	@Programmatic
	public Monitor nuevosMonitor(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones, final String creadoPor) {
		final Monitor unMonitor = container.newTransientInstance(Monitor.class);
		unMonitor.setCodigo(codigo.toUpperCase().trim());
		unMonitor.setTipo(codigo.toUpperCase().trim());
		unMonitor.setPulgadas(codigo.toUpperCase().trim());
		unMonitor.setMarca(marca.toUpperCase().trim());
		unMonitor.setObservaciones(observaciones.toUpperCase().trim());
		unMonitor.setHabilitado(true);
		unMonitor.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unMonitor);
		container.flush();
		return unMonitor;
	}

	// //////////////////////////////////////
	// Listar Insumos
	// //////////////////////////////////////

	@MemberOrder(sequence = "100")
	public List<Monitor> listar() {
		final List<Monitor> listaSoftware = this.container
				.allMatches(new QueryDefault<Monitor>(Monitor.class,
						"listarMonitorTrue"));
		if (listaSoftware.isEmpty()) {
			this.container.warnUser("No hay Software cargadas en el sistema.");
		}
		return listaSoftware;
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////
	private String currentUserName() {
		return container.getUser().getName();
	}
	

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	@javax.inject.Inject
	private DomainObjectContainer container;
}
