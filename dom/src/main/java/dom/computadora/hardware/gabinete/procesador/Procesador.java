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
package dom.computadora.hardware.gabinete.procesador;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

// TODO: Auto-generated Javadoc
/**
 * Clase Procesador.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
		+ "FROM dom.computadora.hardware.gabinete.procesador.Procesador") })
@ObjectType("Procesador")
public class Procesador {

	/**
	 * Titulo de la clase
	 *
	 * @return the string
	 */
	public String title() {
		return this.getModelo();
	}

	/**
	 * Nombre del icono
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Procesador";
	}
	// //////////////////////////////////////
	// Modelo (propiedad)
	// //////////////////////////////////////

	/** The modelo. */
	private String modelo;

	/**
	 * Instantiates a new procesador.
	 *
	 * @param modeloProcesador the modelo procesador
	 */
	public Procesador(String modeloProcesador) {
		this.modelo = modeloProcesador;
	}
	
	/**
	 * Instancia de un nuevo procesador.
	 */
	public Procesador()
	{
		
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Modelo de la Computadora:")
	@MemberOrder(sequence = "190")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}
}
