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
package dom.computadora.hardware.monitor;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.hardware.monitor.Monitor.TipoMonitor;

// TODO: Auto-generated Javadoc
/**
 * Clase MonitorRepositorio.
 */
@DomainService
@Named("Monitor")
public class MonitorRepositorio {
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	/**
	 * Titulo de la Clase.
	 *
	 * @return the string
	 */
	public String title() {
		return "Monitor";
	}

	/**
	 * Nombre del icono de la clase
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Monitor";
	}

	// //////////////////////////////////////
	// Agregar Monitor
	// //////////////////////////////////////
	/**
	 * Adds the monitor.
	 *
	 * @param tamaño 
	 * @param tipo 
	 * @param marca 
	 * @return monitor
	 */
	@NotContributed
	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Monitor addMonitor(final @Named("Tamaño en plg.") int tamaño,
			final @Named("Tipo") TipoMonitor tipo,
			final @Named("Marca") String marca) {
		return nuevosMonitor(tamaño, tipo, marca, this.currentUserName());
	}

	/**
	 * Nuevos monitor
	 * pasamos los parametros de carga
	 *
	 * @param tamaño the tamaño
	 * @param tipo the tipo
	 * @param marca the marca
	 * @param creadoPor the creado por
	 * @return the monitor
	 */
	@Programmatic
	public Monitor nuevosMonitor(final int tamaño, final TipoMonitor tipo,
			final String marca, final String creadoPor) {
		final Monitor unMonitor = container.newTransientInstance(Monitor.class);
		unMonitor.setTamanio(tamaño);
		unMonitor.setTipo(tipo);
		unMonitor.setMarca(marca.toUpperCase().trim());
		unMonitor.setHabilitado(true);
		unMonitor.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unMonitor);
		container.flush();
		return unMonitor;
	}

	// //////////////////////////////////////
	// Listar Monitor
	// //////////////////////////////////////

	/**
	 * Metodo para Listar los monitores.
	 *
	 * @return the list
	 */
	@MemberOrder(sequence = "100")
	public List<Monitor> listar() {
		final List<Monitor> listaSoftware = this.container
				.allMatches(new QueryDefault<Monitor>(Monitor.class,
						"listarMonitorTrue"));
		if (listaSoftware.isEmpty()) {
			this.container.warnUser("No hay Monitores cargados en el sistema.");
		}
		return listaSoftware;
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////
	/**
	 * Current user name.
	 *
	 * @return the string
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	/** The container. */
	@javax.inject.Inject
	private DomainObjectContainer container;
}