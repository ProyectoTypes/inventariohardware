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
package dom.usuarioshiro;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;

import dom.permiso.Permiso;
import dom.rol.Rol;

@DomainService(menuOrder = "80", repositoryFor = UsuarioShiro.class)
@Named("Configuracion")
public class UsuarioShiroRepositorio {

	public String getId() {
		return "usuarioshiro";
	}

	public String iconName() {
		return "Tecnico";
	}

	@Programmatic
	@PostConstruct
	public void init() {
		List<UsuarioShiro> usuarios = listAll();
		if (usuarios.isEmpty()) {
			Permiso permiso = new Permiso();
			Rol rol = new Rol();
			SortedSet<Permiso> permisos = new TreeSet<Permiso>();

			permiso.setNombre("ADMIN");
			permiso.setPath("*");
			permisos.add(permiso);
			rol.setNombre("ADMINISTRADOR");
			rol.setListaPermisos(permisos);

			addUsuarioShiro("sven", "pass", rol);
		}
	}

	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "1")
	@Named("Ver todos")
	public List<UsuarioShiro> listAll() {
		return container.allInstances(UsuarioShiro.class);
	}
	@MemberOrder(sequence = "2")
	@Named("Crear Usuario")
	@Hidden(where = Where.OBJECT_FORMS)
	public UsuarioShiro addUsuarioShiro(final @Named("Nick") String nick,
			final @Named("Password") String password,
			final @Named("Rol") Rol rol) {
		final UsuarioShiro obj = container
				.newTransientInstance(UsuarioShiro.class);

		final SortedSet<Rol> rolesList = new TreeSet<Rol>();
		if (rol != null) {
			rolesList.add(rol);
			obj.setRolesList(rolesList);
		}
		obj.setNick(nick);
		obj.setPassword(password);
		container.persistIfNotAlready(obj);
		return obj;
	}
	@Programmatic
	public UsuarioShiro addUsuarioShiro(final @Named("Nick") String nick,
			final @Named("Password") String password,
			final @Named("Rol") List<Rol> rol) {
		final UsuarioShiro obj = container
				.newTransientInstance(UsuarioShiro.class);

		if (!rol.isEmpty()) {
			SortedSet<Rol> listaDeRoles = new TreeSet<Rol>(rol);
			obj.setRolesList(listaDeRoles);
		}
		obj.setNick(nick);
		obj.setPassword(password);
		container.persistIfNotAlready(obj);
		return obj;
	}
	@ActionSemantics(Of.NON_IDEMPOTENT)
	@MemberOrder(sequence = "4")
	@Named("Eliminar Usuario")
	public String removeUsuarioShiro(@Named("Usuario") UsuarioShiro usuarioShiro) {
		String userName = usuarioShiro.getNick();
		container.remove(usuarioShiro);
		return "The user " + userName + " has been removed";
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
