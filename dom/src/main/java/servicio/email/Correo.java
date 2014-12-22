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

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "buscarCorreo", language = "JDOQL", value = "SELECT "
		+ "FROM servicio.email.Correo "),@javax.jdo.annotations.Query(name = "buscarCorreoPorUsuario", language = "JDOQL", value = "SELECT "
				+ "FROM servicio.email.Correo "+"WHERE tecnico == :usuario order by fechaActual asc"  )})

@ObjectType("CORREO")
@Immutable
public class Correo implements Comparable<Correo> {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String iconName() {
		return "Correo";
	}

	public String title() {
		return "Correo";
	}

	// //////////////////////////////////////
	// Asunto (propiedad)
	// //////////////////////////////////////

	private String asunto;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Asunto")
	@MemberOrder(sequence = "1")
	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(final String asunto) {
		this.asunto = asunto;
	}

	// //////////////////////////////////////
	// Email (propiedad)
	// //////////////////////////////////////

	private String email;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Email")
	@MemberOrder(sequence = "2")
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	// //////////////////////////////////////
	// Fecha (propiedad)
	// //////////////////////////////////////

	private Date fechaActual;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Fecha")
	@MemberOrder(sequence = "3")
	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(final Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	// //////////////////////////////////////
	// Respondido (propiedad)
	// //////////////////////////////////////

	private boolean respondido;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Hidden(where = Where.ALL_TABLES)
	public boolean isRespondido() {
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}

	// //////////////////////////////////////
	// Mensaje (propiedad)
	// //////////////////////////////////////

	private String mensaje;

	@javax.jdo.annotations.Column(allowsNull = "true")
//	@Hidden(where = Where.ALL_TABLES)
	@MultiLine(numberOfLines = 6)
	@MemberOrder(sequence = "2")

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}

	// //////////////////////////////////////
	// Correo Empresa (propiedad)
	// //////////////////////////////////////

	private CorreoEmpresa correoEmpresa;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Hidden
	public CorreoEmpresa getCorreoEmpresa() {
		return correoEmpresa;
	}

	public void setCorreoEmpresa(CorreoEmpresa correoEmpresa) {
		this.correoEmpresa = correoEmpresa;
	}

	// //////////////////////////////////////
	// Tecnico (propiedad)
	// //////////////////////////////////////

	private String tecnico;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Hidden
	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(final String tecnico) {
		this.tecnico = tecnico;
	}

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	/**
	 * Se ordenan los correos por fecha de ingreso.
	 */
	@Override
	public int compareTo(Correo mensaje) {
		return this.fechaActual.compareTo(mensaje.getFechaActual());
	}

}
