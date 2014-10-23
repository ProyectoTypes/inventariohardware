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
package dom.permiso;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

@DomainService(menuOrder = "82", repositoryFor = Permiso.class)
@Named("Permisos")
public class PermisoRepositorio {

	public String getId() {
		return "Permiso";
	}

	public String iconName() {
		return "Tecnico";
	}

	@MemberOrder(sequence = "2")
	@Named("Nuevo Permiso")
	public Permiso addPermiso(final @Named("Nombre") String nombre,
			final @Named("Path") String path) {
		final Permiso permiso = container.newTransientInstance(Permiso.class);

		permiso.setNombre(nombre);
		permiso.setPath(path);

		container.persistIfNotAlready(permiso);
		return permiso;
	}

	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(sequence = "4")
	@Named("Eliminar Permiso")
	public String eliminar(@Named("Permiso") Permiso permiso) {
		String permissionDescription = permiso.getNombre();
		container.remove(permiso);
		return "El Permiso: " + permissionDescription + " ha sido eliminado";
	}

	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "1")
	@Named("Todos los Permisos")
	public List<Permiso> listAll() {
		return container.allInstances(Permiso.class);
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
