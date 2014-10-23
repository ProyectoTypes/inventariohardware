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
	public Permiso addPermiso(
			final @Named("Nombre") String nombre,
			final @Named("Directorio") String path,
			@Optional @DescribedAs("Por defecto: '*' ") @Named("Clase") String clase,
			@Optional @DescribedAs("Por defecto: '*' ") @Named("Metodo/Atributo") String campo,
			final @Optional @DescribedAs("Por defecto: lectura/escritura ") @Named("Permiso de Escritura") boolean escritura) {
		final Permiso permiso = container.newTransientInstance(Permiso.class);

		permiso.setNombre(nombre);
		if (clase == "" || clase ==null)
			clase = "*";
		if (campo == ""|| campo ==null)
			campo = "*";
		String acceso = "*";
		if (!escritura)
			acceso = "r";
		String directorio = path + ":" + clase + ":" + campo + ":" + acceso;

		permiso.setPath(directorio);

		container.persistIfNotAlready(permiso);
		return permiso;
	}
	public String default2AddPermiso() {
		return "*";
	}public String default3AddPermiso() {
		return "*";
	}
	public boolean default4AddPermiso() {
		return true; 
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
