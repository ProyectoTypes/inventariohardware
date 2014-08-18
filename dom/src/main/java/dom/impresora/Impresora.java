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
package dom.impresora;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;

import dom.computadora.Computadora;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Impresora_modeloImpresora_must_be_unique", members = {
		"creadoPor", "modeloImpresora" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorModeloImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor && modeloImpresora.indexOf(:modeloImpresora) >= 0"),
		@javax.jdo.annotations.Query(name = "todasLasImpresoras", language = "JDOQL", value = "SELECT FROM dom.impresora.Impresora "
				+ " WHERE creadoPor == :creadoPor && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPormodeloImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && modeloImpresora.indexOf(:modeloImpresora) >= 0") })
@ObjectType("IMPRESORA")
@Audited
@AutoComplete(repository = ImpresoraRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Impresora {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	public String title() {
		return this.getModeloImpresora();
	}

	public String iconName() {
		return "IMPRESORA";
	}

	// //////////////////////////////////////
	// Modelo Impresora
	// //////////////////////////////////////

	private String modeloImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Nombre de la Impresora")
	@MemberOrder(sequence = "10")
	public String getModeloImpresora() {
		return modeloImpresora;
	}

	public void setModeloImpresora(final String modeloImpresora) {
		this.modeloImpresora = modeloImpresora;
	}

	// //////////////////////////////////////
	// Fabricante Impresora
	// //////////////////////////////////////

	private String fabricanteImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Fabricante de la Impresora:")
	@MemberOrder(sequence = "20")
	public String getFabricanteImpresora() {
		return fabricanteImpresora;
	}

	public void setFabricanteImpresora(final String fabricanteImpresora) {
		this.fabricanteImpresora = fabricanteImpresora;
	}

	// //////////////////////////////////////
	// Tipo de Impresora
	// //////////////////////////////////////

	private String tipoImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Tipo de Impresora:")
	@MemberOrder(sequence = "30")
	public String getTipoImpresora() {
		return tipoImpresora;
	}

	public void setTipoImpresora(final String tipoImpresora) {
		this.tipoImpresora = tipoImpresora;
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	private String creadoPor;

	@Hidden(where = Where.ALL_TABLES)
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	// //////////////////////////////////////
	// Relacion Impresora/Computadora
	// //////////////////////////////////////

	@Persistent(mappedBy = "impresora", dependentElement = "False")
	@Join
	private SortedSet<Computadora> computadora = new TreeSet<Computadora>();

	public SortedSet<Computadora> getComputadora() {
		return computadora;
	}

	public void setComputadora(final SortedSet<Computadora> computadora) {
		this.computadora = computadora;
	}
	
	@Named ("Agregar Impresora")
	public void agregarComputadora (final Computadora unaComputadora){
		if (unaComputadora == null || getComputadora().contains(unaComputadora)){
			return;
		}
		unaComputadora.limpiarImpresora();
		unaComputadora.setImpresora(this);
		getComputadora().add(unaComputadora);
	}
	
	@Named ("Eliminar de Computadora")
	public void removeFromComputadora (final Computadora unaComputadora){
		if (unaComputadora == null || !getComputadora().contains(unaComputadora)){
			return;
		}
		unaComputadora.setImpresora(null);
		this.getComputadora().remove(unaComputadora);
		return;
	}
	// //////////////////////////////////////
	// Complete (property),
	// Se utiliza en las acciones add (action), DeshacerAgregar (action)
	// //////////////////////////////////////

	private boolean complete;

	@Disabled
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}
}