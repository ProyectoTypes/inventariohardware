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

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.util.ObjectContracts;

import dom.computadora.Computadora;
import dom.movimiento.Movimiento;
import dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Tecnico_apellido_must_be_unique", members = { "id" }) })
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
				+ "FROM dom.tecnico.Tecnico "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.tecnio.Tecnico"
				+ "WHERE creadoPor == :creadoPor && "
				+ "apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "getTecnico", language = "JDOQL", value = "SELECT FROM dom.tecnico.Tecnico WHERE creadoPor == :creadoPor") })
@ObjectType("TECNICO")
@Audited
@AutoComplete(repository = TecnicoRepositorio.class, action = "autoComplete")
//
@Bookmarkable
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

	// //////////////////////////////////////
	// Borrar Usuario
	// //////////////////////////////////////
	/**
	 * Método que utilizo para deshabilitar un Tecnico.
	 * 
	 * @return la propiedad habilitado en false.
	 */
	@Named("Eliminar")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "1")
	public List<Tecnico> eliminar() {
		if (getEstaHabilitado() == true) {
			setHabilitado(false);
			container.isPersistent(this);
			container.warnUser("Eliminado " + container.titleOf(this));
		}
		return null;
	}

	// {{ Movimiento (property)
	private Movimiento movimiento;

	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Movimiento getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(final Movimiento movimiento) {
		this.movimiento = movimiento;
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

	@MemberOrder(sequence = "1")
	@Size(min = 0, max = 5)
	// Chequear si funciona el @Size .
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
	public void addToComputadora(final Computadora unaComputadora) {
		// check for no-op
		if (unaComputadora == null
				|| getComputadoras().contains(unaComputadora)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		unaComputadora.clearTecnico();
		// associate arg
		unaComputadora.setTecnico(this);
		this.getComputadoras().add(unaComputadora);
	}

	@Named("Eliminar Computadora")
	public void removeFromComputadora(final Computadora unaComputadora) {
		// check for no-op
		if (unaComputadora == null
				|| !getComputadoras().contains(unaComputadora)) {
			return;
		}
		// dissociate arg
		unaComputadora.setTecnico(null);
		getComputadoras().remove(unaComputadora);
	}

	// ///////////////////////////////////////////////////
	// Campo que diferencia a tecnico de usuario. El valor
	// por el momento se hara manualmente pero lo ideal es
	// que cambie automaticamente segun el patron State.
	// ///////////////////////////////////////////////////

	private BigDecimal cantidadComputadora;

	@Max(5)
	@javax.jdo.annotations.Column(allowsNull = "true")
	public BigDecimal getCantidadComputadora() {
		return cantidadComputadora;
	}

	public void setCantidadComputadora(BigDecimal cantidadComputadora) {
		this.cantidadComputadora = cantidadComputadora;
	}

	/**
	 * SumaComputadora: Controla que no sean mas de 5 equipos por Tecnico.
	 * A.comparetTo(B) -> 0 : Si son iguales ; 1: A > B ; -1: B > A
	 */
	@Programmatic
	public void sumaComputadora() {
		BigDecimal valor = new BigDecimal(1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));
	}

	@Programmatic
	public void restaComputadora() {
		BigDecimal valor = new BigDecimal(-1);
		this.setCantidadComputadora(this.cantidadComputadora.add(valor));

	}

	// {{ Disponible (property)
	private Boolean disponible;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(final Boolean disponible) {
		this.disponible = disponible;
	}

	@Programmatic
	public Boolean estaDisponible() {
		BigDecimal tope = new BigDecimal(5);
		if (this.cantidadComputadora.compareTo(tope) == -1)
			return true;
		else
			return false;

	}

	// }}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////
	/**
	 * Implementa Comparable<Tecnico> Necesario para ordenar por apellido la
	 * clase Tecnico.
	 */
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