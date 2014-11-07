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

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.constraints.Max;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.util.ObjectContracts;

import dom.computadora.Computadora;
import dom.persona.Persona;
import dom.rol.Rol;
import dom.sector.Sector;
import dom.soporte.Soporte;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Tecnico_apellido_must_be_unique", members = { "nick" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico "
				+ "WHERE creadoPor == :creadoPor && "
				+ "apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarTecnicoFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "eliminarTecnicoTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnico.Tecnico " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnio.Tecnico"
				+ "WHERE creadoPor == :creadoPor && "
				+ "apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "getTecnico", language = "JDOQL", value = "SELECT FROM dom.tecnico.Tecnico WHERE creadoPor == :creadoPor") })
@ObjectType("TECNICO")
@Audited
@AutoComplete(repository = TecnicoRepositorio.class, action = "autoComplete")
public class Tecnico extends Persona implements Comparable<Persona> {

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return this.getApellido() + ", " + this.getNombre();
	}

	public String iconName() {
		return "Tecnico";
	}

	private String nick;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	
	public String getNick() {
		return nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	private String password;

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Join
	@Element(dependent = "false")
	private SortedSet<Rol> listaDeRoles = new TreeSet<Rol>();

	@MemberOrder(name="Lista de Roles",sequence = "2")
	@Render(org.apache.isis.applib.annotation.Render.Type.EAGERLY)
	public SortedSet<Rol> getListaDeRoles() {
		return listaDeRoles;
	}

	public void setRolesList(final SortedSet<Rol> listaDeRoles) {
		this.listaDeRoles = listaDeRoles;
	}

	@MemberOrder(name="Lista de Roles",sequence = "3")
	@Named("Agregar Rol")
	@DescribedAs("Agrega un Rol al Usuario.")
	public Tecnico addRole(final @Named("Role") Rol rol) {

		listaDeRoles.add(rol);

		return this;
	}

	@MemberOrder(name="Lista de Roles",sequence = "5")
	@Named("Eliminar Rol")
	public Tecnico removeRole(final @Named("Rol") Rol rol) {
		if(this.getNick().toUpperCase().contentEquals("ADMIN"))
			this.container.warnUser("EL ADMINISTRADOR NO PUEDE SER ELIMINADO.");
		else
			getListaDeRoles().remove(rol);
		return this;
	}

	public SortedSet<Rol> choices0RemoveRole() {
		return getListaDeRoles();
	}

	/**
	 * Método que utilizo para deshabilitar un Tecnico.
	 * 
	 * @return la propiedad habilitado en false.
	 */
	@Named("Eliminar Tecnico")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "6")
	public List<Tecnico> eliminar() {
		if (getEstaHabilitado() == true) {
			setHabilitado(false);
			container.isPersistent(this);
			container.warnUser("Eliminado " + container.titleOf(this));
		}
		return null;
	}

	// {{ Movimiento (property)
	private Soporte soporte;

	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	public void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	// }}

	/**
	 * Relacion entre Tecnico/Computadora. Un Tecnico podrá tener hasta 5
	 * Computadoras.
	 */
	// {{ Computadoras (Collection)
	@Join
	@Element(dependent = "False")
	private SortedSet<Computadora> computadoras = new TreeSet<Computadora>();

	@MemberOrder(name="Computadoras",sequence = "7")
	public SortedSet<Computadora> getComputadoras() {
		return computadoras;
	}

	public void setComputadoras(final SortedSet<Computadora> computadoras) {
		this.computadoras = computadoras;
	}

	// }}

	// ///////////////////////////////////////////////////
	// Operaciones de COMPUTADORA: Agregar/Borrar
	// ///////////////////////////////////////////////////
	@Named("Agregar Computadora")
	@MemberOrder(name="Computadoras",sequence = "7")
	public void addToComputadora(final Computadora unaComputadora) {
		if (unaComputadora == null
				|| getComputadoras().contains(unaComputadora)) {
			return;
		}
		unaComputadora.clearTecnico();
		unaComputadora.setTecnico(this);
		this.getComputadoras().add(unaComputadora);
	}

	@Named("Eliminar Computadora")
	@MemberOrder(name="Computadoras",sequence = "7")
	public void removeFromComputadora(final Computadora unaComputadora) {
		if (unaComputadora == null
				|| !getComputadoras().contains(unaComputadora)) {
			return;
		}
		unaComputadora.setTecnico(null);
		this.getComputadoras().remove(unaComputadora);
	}

	/*
	 * Permite saber cuantas computadoras esta reparando el tecnico.
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
	 * SumaComputadora: Controla que no sean mas de 5 equipos por Tecnico.
	 * A.comparetTo(B) -> 0 : Si son iguales ; 1: A > B ; -1: B > A
	 */
	@Programmatic
	public void sumaComputadora() {
		BigDecimal valor = BigDecimal.valueOf(1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));
	}

	@Programmatic
	public void restaComputadora(final Computadora computadora) {
		BigDecimal valor = BigDecimal.valueOf(-1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));
	}

	// {{ Disponible (property)
	private Boolean disponible;

	@MemberOrder(sequence = "7")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(final Boolean disponible) {
		this.disponible = disponible;
	}

	@Programmatic
	public Boolean estaDisponible() {
		BigDecimal tope = BigDecimal.valueOf(10);
		if (this.cantidadComputadora.compareTo(tope) == -1)
			return true;
		else
			return false;

	}

	// }}

	/**
	 * Elimina el sector de forma logica.
	 * 
	 * @return
	 */

	@MemberOrder(sequence = "120")
	@Named("Eliminar Sector")
	public Tecnico clear() {
		Sector currentSector = this.getSector();
		if (currentSector == null) {
			return this;
		}
		this.getSector().setHabilitado(false);
		return this;
	}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////
	@Override
	public int compareTo(final Persona persona) {
		return ObjectContracts.compare(this, persona, "apellido");
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
}