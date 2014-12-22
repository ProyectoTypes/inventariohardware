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
package servicio.email;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
		+ "FROM servicio.email.CorreoEmpresa ") })
@ObjectType("CORREOEMPRESA")
public class CorreoEmpresa {

	// //////////////////////////////////////
	// Titulo
	// //////////////////////////////////////

	public String title() {
		return getCorreo();
	}

	public String iconName() {
		return "Correo";
	}

	// //////////////////////////////////////
	// Correo (propiedad)
	// //////////////////////////////////////

	private String correo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "1")
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	// //////////////////////////////////////
	// Pass (propiedad)
	// //////////////////////////////////////

	private String pass;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "2")
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

//	/**
//	 * Configuracion Avanzada.
//	 */
//	private String host;
//
//	@javax.jdo.annotations.Column(allowsNull = "false")
//	@MemberOrder(sequence = "30")
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	private String portEntrante;
//
//	@javax.jdo.annotations.Column(allowsNull = "false")
//	@MemberOrder(sequence = "40")
//	public String getPortEntrante() {
//		return portEntrante;
//	}
//
//	public void setPortEntrante(String port) {
//		this.portEntrante = port;
//	}
//	private String portSaliente;
//
//	@javax.jdo.annotations.Column(allowsNull = "false")
//	@MemberOrder(sequence = "40")
//	public String getPortSaliente() {
//		return portSaliente;
//	}
//
//	public void setPortSaliente(String port) {
//		this.portSaliente = port;
//	}
}
