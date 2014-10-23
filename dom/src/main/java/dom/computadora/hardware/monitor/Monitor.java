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
package dom.computadora.hardware.monitor;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Monitor_must_be_unique", members = {
		"creadoPor", "codigo" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorMonitor", language = "JDOQL", value = "SELECT "
				+ "FROM dom.monitor.Monitor "
				+ "WHERE creadoPor == :creadoPor && "
				+ "codigo.indexOf(:codigo) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarMonitorFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.monito.Monitor "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "listarMonitorTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.monitor.Monitor "
				+ "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.monitor.Monitor "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && codigo.indexOf(:codigo) >= 0"), })
@ObjectType("MONITOR")
@Audited
@AutoComplete(repository = MonitorRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Monitor {
	
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getCodigo();
	}

	public String iconName() {
		return "Monitor";
	}
	
	
	// //////////////////////////////////////
	// codigo (Atributo)
	// //////////////////////////////////////
	private String codigo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Codigo numero de monitor:")
	@MemberOrder(sequence = "10")
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(final String codigo) {
		this.codigo = codigo;
	}
	
	
	// //////////////////////////////////////
	// codigo (Atributo)
	// //////////////////////////////////////
	private String tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tipo de monitor:")
	@MemberOrder(sequence = "10")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(final String tipo) {
		this.tipo = tipo;
	}
	
	
	// //////////////////////////////////////
	// producto (Atributo)
	// //////////////////////////////////////
	private String pulgadas;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre de monitor:")
	@MemberOrder(sequence = "30")
	public String getPulgadas() {
		return pulgadas;
	}

	public void setPulgadas(final String pulgadas) {
		this.pulgadas = pulgadas;
	}
	
	
	// //////////////////////////////////////
	// marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del monitor:")
	@MemberOrder(sequence = "40")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}
	
	
	// //////////////////////////////////////
	// observaciones (Atributo)
	// //////////////////////////////////////
	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones del monitor:")
	@MemberOrder(sequence = "50")
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(final String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "60")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
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

	public void setCreadoPor(final String creadoPor) {
		this.creadoPor = creadoPor;
	}
}