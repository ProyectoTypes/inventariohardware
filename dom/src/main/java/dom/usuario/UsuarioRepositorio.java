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
package dom.usuario;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

/**
 * Clase UsuarioRepositorio.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */
@DomainService(menuOrder = "40")
@Named("USUARIO")
public class UsuarioRepositorio {

	public UsuarioRepositorio() {

	}

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "usuario";
	}

	/**
	 * Nombre del Icono.
	 * @return
	 */
	public String iconName() {
		return "Usuario";
	}

	/**
	 * Método utilizado para cargar los datos de un usuario por formulario.
	 * @param sector
	 * @param apellido
	 * @param nombre
	 * @param email
	 * @return usuario
	 */
	@MemberOrder(name = "Personal", sequence = "40")
	@Named("(+) Usuario")
	public Usuario create(
			final Sector sector,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") String apellido,
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombre,
			final @RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") @Named("E-mail") String email) {
		return nuevoUsuario(sector, apellido, nombre, email,
				this.currentUserName());
	}

	/**
	 * Nuevo usuario, método que toma los datos ingresado y los persiste.
	 * @param sector
	 * @param apellido
	 * @param nombre
	 * @param email
	 * @param creadoPor
	 * @return usuario
	 */
	@Programmatic
	public Usuario nuevoUsuario(final Sector sector, final String apellido,
			final String nombre, final String email, final String creadoPor) {
		final Usuario unUsuario = container.newTransientInstance(Usuario.class);
		unUsuario.setSector(sector);
		unUsuario.setApellido(apellido.toUpperCase().trim());
		unUsuario.setNombre(nombre.toUpperCase().trim());
		unUsuario.setEmail(email);
		unUsuario.setHabilitado(true);
		unUsuario.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unUsuario);
		container.flush();
		return unUsuario;
	}

	/**
	 * Busca los Sectores.
	 * @param search
	 * @return
	 */
	@Named("Sector")
	@DescribedAs("Buscar el Sector en mayuscula")
	public List<Sector> autoComplete0Create(
			final @MinLength(2) String search) {
		return sectorRepositorio.autoComplete(search);

	}

	/**
	 * Listar, método para lista a los usuarios
	 * @return 
	 */
	@MemberOrder(name = "Personal", sequence = "50")
	@Named("--Listar Usuarios")
	public List<Usuario> listAll() {
		final List<Usuario> listaUsuarios;
	//	if (this.container.getUser().getName().contentEquals("sven"))
	//		listaUsuarios = this.container
	//				.allMatches(new QueryDefault<Usuario>(Usuario.class,
	//						"listar"));
	//	else
			listaUsuarios = this.container
					.allMatches(new QueryDefault<Usuario>(Usuario.class,
							"listarHabilitados"));
		if (listaUsuarios.isEmpty()) {
			this.container.warnUser("No hay Usuarios cargados en el sistema.");
		}
		return listaUsuarios;

	}

	/**
	 * Buscar Usuarios: método que busca por apellido un Usuario.
	 * @param apellido
	 * @return 
	 */
	@MemberOrder(name = "Personal", sequence = "41")
	@Named("--Buscar Usuarios")
	public List<Usuario> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") @MinLength(2) String apellido) {
		final List<Usuario> listarUsuarios = this.container
				.allMatches(new QueryDefault<Usuario>(Usuario.class,
						"buscarPorApellido", "apellido", apellido.toUpperCase()
								.trim()));
		if (listarUsuarios.isEmpty())
			this.container
					.warnUser("No se encontraron Usuarios cargados en el sistema.");
		return listarUsuarios;
	}

	/**
	 * AutoComplete
	 * @param apellido
	 * @return
	 */
	@Programmatic
	public List<Usuario> autoComplete(final String apellido) {
		return container.allMatches(new QueryDefault<Usuario>(Usuario.class,
				"autoCompletePorApellido", "apellido", apellido.toUpperCase()
						.trim()));
	}

	/**
	 * Retorna el nombre del Usuario logueado.
	 * @return
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}
	
	/**
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;

	/**
	 * Inyección del servicio para Sector.
	 */
	@javax.inject.Inject
	private SectorRepositorio sectorRepositorio;
}