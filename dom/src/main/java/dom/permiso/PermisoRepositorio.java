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
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;

/**
 * Clase PermisoRepositorio.
 */
@DomainService(menuOrder = "82", repositoryFor = Permiso.class)
@Named("Permisos")
public class PermisoRepositorio {

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "Permiso";
	}
	
	/**
	 * Nombre del Icono.
	 * @return string
	 */
	public String iconName() {
		return "Tecnico";
	}

	/**
	 * Agregar Permiso. Método que se encarga de la creación de un nuevo permiso.
	 * @param nombre
	 * @param path
	 * @param clase
	 * @param campo
	 * @param escritura
	 * @return permiso
	 */
	@MemberOrder(name = "Configurar Seguridad", sequence = "20")
	@Named("Nuevo Permiso")
	public Permiso create(
			final @Named("Nombre") String nombre,
			final @Named("Directorio") String path,
			@Optional @DescribedAs("Por defecto: '*' ") @Named("Clase") String clase,
			@Optional @DescribedAs("Por defecto: '*' ") @Named("Metodo/Atributo") String campo,
			final @Optional @DescribedAs("Por defecto: lectura/escritura ") @Named("Permiso de Escritura") boolean escritura) {
		final Permiso permiso = container.newTransientInstance(Permiso.class);

		permiso.setNombre(nombre.toUpperCase().trim());
		if (clase == "" || clase == null)
			clase = "*";
		if (campo == "" || campo == null)
			campo = "*";
		String acceso = "*";
		if (!escritura)
			acceso = "r";
		String directorio = path + ":" + clase + ":" + campo + ":" + acceso;

		permiso.setPath(directorio);

		container.persistIfNotAlready(permiso);
		return permiso;
	}

	/**
	 * Default2addpermiso se encarga de poner la "clase" en default.
	 * @return string
	 */
	public String default2Create() {
		return "*";
	}

	public String default3Create() {
		return "*";
	}

	public boolean default4Create() {
		return true;
	}
	
	/**
	 * Método que permite listar todos los Permisos.
	 * @return the list
	 */
	@ActionSemantics(Of.SAFE)
	@MemberOrder(name = "Configurar Seguridad", sequence = "20")
	@Named("Todos los Permisos")
	public List<Permiso> listAll() {
		return container.allInstances(Permiso.class);
	}

	/**
	 * Eliminar Permiso
	 * @param permiso
	 * @return string
	 */
	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(name = "Configurar Seguridad", sequence = "20")
	@Named("Eliminar Permiso")
	public String eliminar(@Named("Permiso") Permiso permiso) {
		String permissionDescription = permiso.getNombre();
		container.remove(permiso);
		return "El Permiso: " + permissionDescription
				+ " ha sido eliminado correctamente.";
	}
	
	/** 
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	DomainObjectContainer container;
}