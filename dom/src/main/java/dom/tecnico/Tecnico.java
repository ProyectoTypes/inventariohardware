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

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.constraints.Max;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import dom.computadora.Computadora;
import dom.persona.Persona;
import dom.rol.Rol;
import dom.sector.Sector;
import dom.soporte.Soporte;

/**
 * Técnico: clase que representa a las personas que pertenecen al Departamento de Sistemas. 
 * Extiende de la clase Persona.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Tecnico_apellido_must_be_unique", members = { "nick" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico "
				+ "WHERE  "
				+ "apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "listarDisponibles", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico " + "WHERE disponible == true"),
		@javax.jdo.annotations.Query(name = "buscarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico"
				+ "WHERE "
				+ "apellido.indexOf(:apellido) >= 0") })
@ObjectType("TECNICO")
@Audited
@AutoComplete(repository = TecnicoRepositorio.class, action = "autoComplete")
public class Tecnico extends Persona implements Comparable<Persona> {

	/**
	 * Obtiene el Apellido y Nombre del Técnico.
	 * @return String
	 */
	public String title() {
		return this.getApellido() + ", " + this.getNombre();
	}

	public String iconName() {
		return "Tecnico";
	}
	
	/**
	 * Retorna el nick del Técnico que se va a persistir.
	 * @return nick
	 */
	private String nick;

	@MemberOrder(sequence = "40")
	@Column(allowsNull = "false")
	public String getNick() {
		return nick;
	}

	/**
	 * Setea el nick del Técnico que se va a persistir.
	 * @param nick
	 */
	public void setNick(final String nick) {
		this.nick = nick;
	}

	/**
	 * Retorna el password del Técnico que se va a persistir.
	 * @return password
	 */
	private String password;

	@MemberOrder(sequence = "50")
	@Hidden
	@Column(allowsNull = "false")
	public String getPassword() {
		return password;
	}

	/**
	 * Setea el password del Técnico que se va a persistir.
	 * @param password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	@Join
	@Element(dependent = "false")
	private SortedSet<Rol> listaDeRoles = new TreeSet<Rol>();
	
	@MemberOrder(name = "Lista de Roles", sequence = "2")
	@Render(org.apache.isis.applib.annotation.Render.Type.EAGERLY)
	public SortedSet<Rol> getListaDeRoles() {
		return listaDeRoles;
	}

	public void setRolesList(final SortedSet<Rol> listaDeRoles) {
		this.listaDeRoles = listaDeRoles;
	}

	/**
	 * Método que permite asignar un Rol al Técnico.
	 * @param rol
	 * @return
	 */
	@MemberOrder(name = "Lista de Roles", sequence = "3")
	@Named("Agregar Rol")
	@DescribedAs("Agrega un Rol al Usuario.")
	public Tecnico addRole(final @Named("Role") Rol rol) {

		listaDeRoles.add(rol);

		return this;
	}

	/**
	 * Método que permite quitar el Rol del Técnico.
	 * @param rol
	 * @return
	 */
	// FIXME: El rol ADMINISTRADOR NO DEBE SER BORRADO.
	@MemberOrder(name = "Lista de Roles", sequence = "5")
	@Named("Eliminar Rol")
	public Tecnico removeRole(final @Named("Rol") Rol rol) {
		if (this.getNick().toUpperCase().contentEquals("SVEN"))
			this.container.warnUser("EL USUARIO SVEN NO PUEDE SER ELIMINADO.");
		else
			getListaDeRoles().remove(rol);
		return this;
	}

	/**
	 * Permite listar los Roles.
	 * @return
	 */
	public SortedSet<Rol> choices0RemoveRole() {
		return getListaDeRoles();
	}

	/**
	 * Método cuya funcionalidad es deshabilitar un Técnico.
	 * @return tecnicoRepositorio.listar()
	 */
	@Named("Eliminar Tecnico")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "6")
	public List<Tecnico> eliminar() {
		if (!this.getNick().toUpperCase().contentEquals("SVEN")) {
			setHabilitado(true);
			container.flush();
			container.warnUser("Registro eliminado");
		} else
			container.warnUser("El Registro Sven No puede ser eliminado.");

		return tecnicoRepositorio.listAll();
	}

	
	private Soporte soporte;

	@MemberOrder(sequence = "200")
	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	public void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	/**
	 * Relacion bidirecciononal entre Técnico y Computadora.
	 * @return computadoras
	 */
	@Join
	@Element(dependent = "False")
	private SortedSet<Computadora> computadoras = new TreeSet<Computadora>();

	@MemberOrder(name = "Computadoras", sequence = "7")
	public SortedSet<Computadora> getComputadoras() {
		return computadoras;
	}

	public void setComputadoras(final SortedSet<Computadora> computadoras) {
		this.computadoras = computadoras;
	}

	/**
	 * Método que permite agregar una Computadora con sus respectivos atributos.
	 * @param unaComputadora
	 */
	@Programmatic
	@Named("Agregar Computadora")
	@MemberOrder(name = "Computadoras", sequence = "7")
	public void addToComputadora(final Computadora unaComputadora) {
		if (unaComputadora == null
				|| getComputadoras().contains(unaComputadora)) {
			return;
		}
		unaComputadora.clearTecnico();
		unaComputadora.setTecnico(this);
		this.getComputadoras().add(unaComputadora);
	}

	/**
	 * Método que permite eliminar una Computadora con sus respectivos atributos.
	 * @param unaComputadora
	 */
	@Programmatic
	@Named("Eliminar Computadora")
	@MemberOrder(name = "Computadoras", sequence = "7")
	public void removeFromComputadora(final Computadora unaComputadora) {
		if (unaComputadora == null
				|| !getComputadoras().contains(unaComputadora)) {
			return;
		}
		unaComputadora.setTecnico(null);
		this.getComputadoras().remove(unaComputadora);
	}

	/**
	 * Permite saber cuantas computadoras esta reparando el Tecnico.
	 * @return cantidadComputadora
	 */
	private BigDecimal cantidadComputadora;

	@Max(5)
	@javax.jdo.annotations.Column(allowsNull = "true")
	public BigDecimal getCantidadComputadora() {
		return cantidadComputadora;
	}
	
	public void setCantidadComputadora(final BigDecimal cantidadComputadora) {
		this.cantidadComputadora = cantidadComputadora;
	}

	/**
	 * SumaComputadora: Controla que no sean mas de cinco equipos por Tecnico.
	 */
	@Programmatic
	public void sumaComputadora() {
		BigDecimal valor = BigDecimal.valueOf(1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));
	}

	/**
	 * RestaComputadora: Dependiendo del Estado, resta una Computadora al Técnico.
	 */
	@Programmatic
	public void restaComputadora() {
		BigDecimal valor = BigDecimal.valueOf(-1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));
	}

	private Boolean disponible;

	/**
	 * Retorna la disponibilidad del Técnico.
	 * @return boolean
	 */
	@MemberOrder(sequence = "7")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Boolean getDisponible() {
		return disponible;
	}

	/**
	 * Setea la propiedad Disponible.
	 * @param disponible
	 */
	public void setDisponible(final Boolean disponible) {
		this.disponible = disponible;
	}

	/**
	 * Retorna la disponibilidad del Técnico.
	 * @return boolean
	 */
	@Programmatic
	public Boolean estaDisponible() {
		BigDecimal tope = BigDecimal.valueOf(10);
		if (this.cantidadComputadora.compareTo(tope) == -1)
			return true;
		else
			return false;
	}

	/**
	 * Método que elimina el Sector.
	 * @return
	 */
	@Hidden(where = Where.OBJECT_FORMS)    
    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(sequence = "120")
    @Named("Eliminar Sector")    
    public String removeSector(@Named("Eliminar: ") Sector delTecnico, @Named("¿Está seguro?") Boolean seguro) {
    		
		delTecnico.setHabilitado('N');
		String remTecnico = delTecnico.title();						
		return  remTecnico + " fue eliminado";
			
	}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////
	
	@Override
	public int compareTo(final Persona persona) {
		return ObjectContracts.compare(this, persona, "apellido");
	}

	/**
	 * Inyección del contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	/**
	 * Inyección del servicio del Técnico.
	 */
	@Inject
	private TecnicoRepositorio tecnicoRepositorio;
}