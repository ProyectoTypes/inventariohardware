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
package dom.computadora.software;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
@DomainService
@Named("SOFTWARE")
public class SoftwareRepositorio {
	
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String title() {
		return "Software";
	}

	public String iconName() {
		return "Software";
	}
	
	// //////////////////////////////////////
	// Agregar Software
	// //////////////////////////////////////
	
	@MemberOrder(sequence = "10")
	@Named("Agregar Software")
	public Software create(final @Named("Codigo") String codigo,
			final @Named("Nombre") String nombre,
			final @Named("Marca") String marca,
			final @Optional @Named("Observaciones") String observaciones) {
		return nuevosSoftware(codigo, nombre, marca, observaciones,
				this.currentUserName());
	}
	
	@Programmatic
	public Software nuevosSoftware(final String codigo,	final String nombre, 
			final String marca,	final String observaciones, final String creadoPor) {
		final Software unSoftware = container.newTransientInstance(Software.class);
		unSoftware.setCodigo(codigo.toUpperCase().trim());
		unSoftware.setNombre(nombre.toUpperCase().trim());
		unSoftware.setMarca(marca.toUpperCase().trim());
		unSoftware.setObservaciones(observaciones.toUpperCase().trim());
		unSoftware.setHabilitado(true);
		unSoftware.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unSoftware);
		container.flush();
		return unSoftware;
	}

	// //////////////////////////////////////
	// Listar Software
	// //////////////////////////////////////
	@Named("Listar Software")
	@MemberOrder(sequence = "100")
	public List<Software> listAll() {
		final List<Software> listaSoftware = this.container
				.allMatches(new QueryDefault<Software>(Software.class,
						"listarSoftwareTrue"));
		if (listaSoftware.isEmpty()) {
			this.container.warnUser("No hay Software cargadas en el sistema.");
		}
		return listaSoftware;
	}
	/**
	 * AutoComplete
	 * @param nombre
	 * @return
	 */
	@Programmatic
	public List<Software> autoComplete(final String nombre) {
		return container.allMatches(new QueryDefault<Software>(Software.class,
				"autoCompletePorSoftware", "nombre", nombre.toUpperCase()
						.trim()));
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
