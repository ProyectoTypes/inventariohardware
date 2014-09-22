/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package dom.rol;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Where;

import dom.permiso.Permiso;

@DomainService(menuOrder = "81", repositoryFor = Rol.class)
@Named("Rol")
public class RolRepositorio {

	public String getId() {
		return "rol";
	}

	public String iconName() {
		return "Tecnico";
	}

	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "1")
	@Named("Lista de Roles.")
	public List<Rol> listAll() {
		return container.allInstances(Rol.class);
	}

	@MemberOrder(sequence = "2")
	@Named("Crear Rol")
	@Hidden(where = Where.OBJECT_FORMS)
	public Rol addRol(final @Named("Nombre") String nombre,
			final @Named("Permiso") Permiso permiso) {
		final Rol rol = container.newTransientInstance(Rol.class);
		final SortedSet<Permiso> permissionsList = new TreeSet<Permiso>();
		if (permiso != null) {
			permissionsList.add(permiso);
			rol.setListaPermisos(permissionsList);
		}
		rol.setNombre(nombre);
		container.persistIfNotAlready(rol);
		return rol;
	}

	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(sequence = "4")
	@Named("Eliminar Rol")
	public String removeRol(@Named("Rol") Rol rol) {
		String roleName = rol.getNombre();
		container.remove(rol);
		return "The role " + roleName + " has been removed";
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
