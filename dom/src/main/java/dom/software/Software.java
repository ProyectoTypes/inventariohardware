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
package dom.software;

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
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Insumos_must_be_unique", members = {
		"creadoPor", "codigo" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorSoftware", language = "JDOQL", value = "SELECT "
				+ "FROM dom.software.Software "
				+ "WHERE creadoPor == :creadoPor && "
				+ "codigo.indexOf(:codigo) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarSoftwareFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.software.Software "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "listarSoftwareTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.software.Software "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.software.Software "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && codigo.indexOf(:codigo) >= 0"), })
@ObjectType("SOFTWARE")
@Audited
@AutoComplete(repository = SoftwareRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Software {
	
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getCodigo();
	}

	public String iconName() {
		return "Software";
	}
	
	
	// //////////////////////////////////////
	// codigo (Atributo)
	// //////////////////////////////////////
	private String codigo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Codigo numero de software:")
	@MemberOrder(sequence = "10")
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
	// //////////////////////////////////////
	// codigo (Atributo)
	// //////////////////////////////////////
	private String tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tipo de software:")
	@MemberOrder(sequence = "10")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	// //////////////////////////////////////
	// producto (Atributo)
	// //////////////////////////////////////
	private String nombre;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre de software:")
	@MemberOrder(sequence = "30")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	// //////////////////////////////////////
	// marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del Software:")
	@MemberOrder(sequence = "40")
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	
	// //////////////////////////////////////
	// observaciones (Atributo)
	// //////////////////////////////////////
	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones del insumo:")
	@MemberOrder(sequence = "50")
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
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

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}
}
