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

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.ObjectContracts;

import dom.computadora.Computadora;
import dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Usuario_apellido_must_be_unique", members = { "id" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.usuario.Usuario "
				+ "WHERE apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarUsuarioFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.usuario.Usuario "
				+ "WHERE habilitado == false"),
		@javax.jdo.annotations.Query(name = "eliminarUsuarioTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.usuario.Usuario " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorApellido", language = "JDOQL", value = "SELECT "
				+ "FROM dom.usuario.Usuario "
				+ "WHERE apellido.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "getUsuario", language = "JDOQL", value = "SELECT "
				+ "FROM dom.usuario.Usuario " + "WHERE creadoPor == :creadoPor") })
@ObjectType("USUARIO")
@Audited
@AutoComplete(repository = UsuarioRepositorio.class, action = "autoComplete")
public class Usuario extends Persona implements Comparable<Persona> {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getApellido() + " " + this.getNombre();
	}

	public String iconName() {
		return "Usuario";
	}

	/***********************************************************
	 * Un Usuario tiene una sola Computadora.
	 */
	private Computadora computadora;

	@MemberOrder(sequence = "70")
	@javax.jdo.annotations.Column(allowsNull = "true")
    @Persistent(mappedBy="usuario")
	public Computadora getComputadora() {
		return computadora;
	}

	public void setComputadora(final Computadora computadora) {
		this.computadora = computadora;
	}

	// //////////////////////////////////////
	// Operaciones de Computadora:
	// //////////////////////////////////////

	public void modifyComputadora(final Computadora unaComputadora) {

		Computadora currentComputadora = getComputadora();
		if (unaComputadora == null || unaComputadora.equals(currentComputadora)) {
			return;
		}
		currentComputadora.modifyUsuario(this);
	}

	public void clearComputadora() {
		Computadora computadora = this.getComputadora();
		if (computadora == null) {
			return;
		}
		computadora.clearUsuario();
	}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////
	/**
	 * Implementa Comparable<Usuario> Necesario para ordenar por apellido la
	 * clase Usuario.
	 */
	@Override
	public int compareTo(final Persona persona) {
		return ObjectContracts.compare(this, persona, "apellido");
	}

}