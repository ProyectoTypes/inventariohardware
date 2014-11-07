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
package dom.tecnico;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

import dom.permiso.Permiso;
import dom.rol.Rol;
import dom.sector.Sector;
import dom.sector.SectorRepositorio;

@DomainService(menuOrder = "30")
@Named("TECNICO")
public class TecnicoRepositorio {

	public TecnicoRepositorio() {

	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "tecnico";
	}

	public String iconName() {
		return "Tecnico";
	}

	@Programmatic
	@PostConstruct
	public void init() {
		List<Tecnico> tecnicos = listar();
		if (tecnicos.isEmpty()) {
			Permiso permiso = new Permiso();
			Rol rol = new Rol();
			SortedSet<Permiso> permisos = new TreeSet<Permiso>();
			permiso.setNombre("ADMIN");
			permiso.setPath("*");
			permisos.add(permiso);
			rol.setNombre("ADMINISTRADOR");
			rol.setListaPermisos(permisos);
			final SortedSet<Rol> roles = new TreeSet<Rol>();
			roles.add(rol);
			this.nuevoTecnico("Administrado", "Tecnico",
					"inventariohardware@gmail.com", null,
					this.currentUserName(), "sven", "pass", roles);
		}
	}

	// //////////////////////////////////////
	// Agregar Tecnico
	// //////////////////////////////////////
	@NotContributed
	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Tecnico addTecnico(final @Named("Apellido") String apellido,
			final @Named("Nombre") String nombre,
			final @Named("email") String email, final @Optional Sector sector,
			final @Named("Nick") String nick,
			final @Named("Password") String password,
			final @Named("Rol") Rol rol) {
		final SortedSet<Rol> rolesList = new TreeSet<Rol>();
		if (rol != null) {
			rolesList.add(rol);
		}
		return nuevoTecnico(apellido, nombre, email, sector,
				this.currentUserName(), nick, password, rolesList);
	}

	@Programmatic
	public Tecnico nuevoTecnico(final String apellido, final String nombre,
			final String email, final Sector sector, final String creadoPor,
			final String nick, final String password,
			final SortedSet<Rol> rolList) {
		final Tecnico unTecnico = container.newTransientInstance(Tecnico.class);

		unTecnico.setApellido(apellido.toUpperCase().trim());
		unTecnico.setNombre(nombre.toUpperCase().trim());
		unTecnico.setEmail(email);
		unTecnico.setSector(sector);
		unTecnico.setCreadoPor(creadoPor);
		unTecnico.setNick(nick);
		unTecnico.setPassword(password);
		unTecnico.setHabilitado(true);
		unTecnico.setSoporte(null);
		if (!rolList.isEmpty()) {
			SortedSet<Rol> listaDeRoles = new TreeSet<Rol>(rolList);
			unTecnico.setRolesList(listaDeRoles);
		}

		unTecnico.setCantidadComputadora(new BigDecimal(0));

		container.persistIfNotAlready(unTecnico);
		container.flush();
		return unTecnico;

	}

	// //////////////////////////////////////
	// Buscar Sector
	// //////////////////////////////////////

	@Named("Sector")
	@DescribedAs("Buscar el Sector en mayuscula")
	public List<Sector> choices3AddTecnico() {
		return sectorRepositorio.listar();
	}

	// //////////////////////////////////////
	// Listar Tecnico
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Tecnico> listar() {
		final List<Tecnico> listaTecnicos = this.container
				.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
						"eliminarTecnicoTrue"));
		if (listaTecnicos.isEmpty()) {
			this.container.warnUser("No hay tecnicos cargados en el sistema");
		}
		return listaTecnicos;

	}

	// //////////////////////////////////////
	// Buscar Tecnico
	// //////////////////////////////////////

	@MemberOrder(sequence = "30")
	public List<Tecnico> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Apellido") @MinLength(2) String apellidoUsuario) {
		final List<Tecnico> listarTecnicos = this.container
				.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
						"buscarPorApellido", "creadoPor", this
								.currentUserName(), "apellido", apellidoUsuario
								.toUpperCase().trim()));
		if (listarTecnicos.isEmpty())
			this.container
					.warnUser("No se encontraron Tecnicos cargados en el sistema.");
		return listarTecnicos;
	}

	@Programmatic
	public List<Tecnico> autoComplete(final String apellido) {
		return container.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
				"autoCompletarPorApellido", "creadoPor", currentUserName(),
				"apellido", apellido));
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

	@javax.inject.Inject
	private SectorRepositorio sectorRepositorio;
}