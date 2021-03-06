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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.query.QueryDefault;

import dom.permiso.Permiso;
import dom.rol.Rol;
import dom.sector.Sector;
import dom.sector.SectorRepositorio;

/**
 * TecnicoRepositorio: permite crear, buscar, eliminar y listar los Técnicos que pertenecen al Ministerio de Gobierno, Educación y Justicia. 
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */

@DomainService(menuOrder = "10")
@Named("TECNICO")
public class TecnicoRepositorio {

	/**
	 * Retorna el nombre del icono para el Técnico.
	 * @return String
	 */
	public String getId() {
		return "Tecnico";
	}

	public String iconName() {
		return "Tecnico";
	}

	/**
	 * Método que al iniciar la aplicación crea por defecto un Técnico del tipo Administrador.
	 * @throws NoSuchAlgorithmException
	 */
	@Programmatic
	@PostConstruct
	public void init() throws NoSuchAlgorithmException {
		List<Tecnico> tecnicos = listAll();
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
			this.nuevoTecnico("Técnico", "Administrador",
					"inventariohardware@gmail.com", null, "admin", "admin",
					"admin", roles);
		}
	}

	/**
	 * Obtiene los datos del Técnico.
	 * @param apellido
	 * @param nombre
	 * @param email
	 * @param sector
	 * @param nick
	 * @param password
	 * @param rol
	 * @return nuevoTecnico
	 */
	@NotContributed
	@MemberOrder(name = "Personal", sequence = "10")
	@Named("Agregar Técnico")
	public Tecnico create(final @Named("Apellido") String apellido,
			final @Named("Nombre") String nombre,
			final @Named("email") String email, 
			final @Optional Sector sector,
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
	
	/**
	 * Toma los datos ingresados del Técnico y lo persiste.
	 * @param apellido
	 * @param nombre
	 * @param email
	 * @param sector
	 * @param creadoPor
	 * @param nick
	 * @param password
	 * @param rolList
	 * @return
	 */
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
		try {
			unTecnico.setPassword(hash256(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		unTecnico.setDisponible(true);
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

	/**
	 * Método que permite el listado de los Sectores.
	 * @return sectorRepositorio.listar()
	 */
	@Named("Sector")
	@DescribedAs("Buscar el Sector en mayuscula")
	public List<Sector> choices3Create() {
		return sectorRepositorio.listAll();
	}

	/**
	 * Método que permite listar todos los Técnicos habilitados.
	 * @return listaTecnicos
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Hidden
	@MemberOrder(name = "Personal", sequence = "20")
	@Named("Listar Técnicos")
	public List<Tecnico> listAll() {
		final List<Tecnico> listaTecnicos;
		if (this.container.getUser().getName().contentEquals("sven")) {
			listaTecnicos = this.container
					.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
							"listar"));
		} else
			listaTecnicos = this.container
					.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
							"listarHabilitados"));
		if (listaTecnicos.isEmpty()) {
			this.container.warnUser("No hay Técnicos cargados en el sistema.");
		}
		return listaTecnicos;

	}
	
	/**
	 * Método que permite listar todos los Técnicos disponibles.
	 * @return listarDisponibles
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(name = "Personal", sequence = "10")
	@Named("Listar Técnico")
	public List<Tecnico> listarDisponibles() {
		return this.container
					.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
							"listarDisponibles"));
	}

	/**
	 * Buscar Técnico.
	 * @param apellidoUsuario
	 * @return
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(name = "Personal", sequence = "30")
	@Named("Buscar Técnico")
	public List<Tecnico> buscar(
			final @Named("Apellido") @MinLength(2) String apellidoUsuario) {
		final List<Tecnico> listarTecnicos = this.container
				.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
						"buscarPorApellido", "apellido", apellidoUsuario
								.toUpperCase().trim()));
		if (listarTecnicos.isEmpty())
			this.container
					.warnUser("No se encontraron Técnicos cargados en el sistema.");
		return listarTecnicos;
	}

	/**
	 * Método que contiene la capacidad de autocompletar las propiedades de referencia del Técnico y 
	 * permite mostrarlas en un cuadro de lista despegable.
	 * @param apellido
	 * @return
	 */
	@Programmatic
	public List<Tecnico> autoComplete(final String apellido) {
		return container.allMatches(new QueryDefault<Tecnico>(Tecnico.class,
				"autoCompletarPorApellido", "apellido", apellido));
	}

	/**
	 * Permite encodear un string a SHA-256.
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String hash256(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data.getBytes());
		return bytesToHex(md.digest());
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(
					1));
		return result.toString();
	}

	/**
	 * Obtiene el nombre del Técnico que se encuentra logueado.
	 * @return
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	/**
	 * Inyección del contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;

	/**
	 * Inyección del servicio del Tecnico.
	 */
	@javax.inject.Inject
	private SectorRepositorio sectorRepositorio;
}