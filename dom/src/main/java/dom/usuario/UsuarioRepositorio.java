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

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.query.QueryDefault;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

/**
 * UsuarioRepositorio: permite crear, buscar, eliminar y listar los Usuarios que pertenecen al Ministerio de Gobierno, Educación y Justicia. 
 * @author ProyectoTypes
 * @since 19/05/2014
 * @version 1.0.0
 */

@DomainService(menuOrder = "20")
@Named("Usuario")
public class UsuarioRepositorio {

	/**
	 * Retorna el nombre del icono para el Sector.
	 * @return
	 */
	public String getId() {
		return "usuario";
	}

	/**
	 * Nombre del Icono.
	 * @return the string
	 */
	public String iconName() {
		return "Usuario";
	}
	
	/**
	 * Listar Usuario: permite listar todos los Usuarios ingresados al sistema.
	 * @return 
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "10")
	@Named("Listar Usuario")
	public List<Usuario> listAll() {
		return filtroUS(container.allMatches(new QueryDefault<Usuario>(
				Usuario.class, "ListarUsuarios")), true);
	}
	
	private List<Usuario> filtroUS(List<Usuario> Usuarios, boolean S) {
		List<Usuario> filtroUS = new ArrayList<Usuario>();

		for (Usuario sec : Usuarios) {
			if (sec.getEstaHabilitado() == S)
				filtroUS.add(sec);
		}
		return filtroUS;
	}

	/**
	 * Método utilizado para cargar los datos de un usuario por formulario.
	 * @param sector
	 * @param apellido
	 * @param nombre
	 * @param email
	 * @return 
	 */
	@MemberOrder(sequence = "20")
	@Named("Crear Usuario")
	public Usuario create(
			final Sector sector,
			final @RegEx(validation = "[A-Za-z]+") @Named("Apellido") String apellido,
			final @RegEx(validation = "[A-Za-z]+") @Named("Nombre") String nombre,
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
	 * @return 
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
	 * Busca los Usuario.
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
	 * Buscar Usuarios: método que busca por apellido un Usuario.
	 * @param apellido
	 * @return 
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "30")
	@Named("Buscar Usuarios")
	public List<Usuario> buscar(
			final @RegEx(validation = "[A-Za-z]+") @Named("Apellido") @MinLength(2) String apellido) {
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
	 * Eliminar Usuario: permite eliminar un Usuario.
	 * @param delUsuario
	 * @param seguro
	 * @return
	 */
	@Hidden(where = Where.OBJECT_FORMS)    
    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(sequence = "40")
    @Named("Eliminar Usuario")    
    public String removeUsuario(@Named("Eliminar: ") Usuario delUsuario, @Named("¿Está seguro?") Boolean seguro) {
		delUsuario.setHabilitado(false);
		String remUsuario = delUsuario.title();						
		return  remUsuario + " fue eliminado.";
	}
	
	/**
	 * Lista que devuelve los Sectores ha eliminar.
	 * @return
	 */
	public List<Usuario> choices0RemoveUsuario(){
		return filtroUS(container.allMatches(new QueryDefault<Usuario>(Usuario.class,
				"ListarUsuarios")),true);
	}
	
	/**
	 * Confirma la eliminación del Sector.
	 * @param delSector
	 * @param seguro
	 * @return
	 */
	public String validateRemoveUsuario(Usuario delUsuario, Boolean seguro) {
		if (!seguro) {
			return "Marque en la opción si está seguro.";
		}
		return null;
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