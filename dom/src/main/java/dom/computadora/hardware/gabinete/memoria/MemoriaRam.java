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
package dom.computadora.hardware.gabinete.memoria;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

/**
 * Clase MemoriaRam.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
		+ "FROM dom.computadora.hardware.gabinete.memoria.MemoriaRam") })
@ObjectType("MemoriaRam")

public class MemoriaRam {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Instantiates a new memoria ram.
	 *
	 * @param modeloRam the modelo ram
	 * @param tamanoRam the tamano ram
	 * @param marcaRam the marca ram
	 */
	public MemoriaRam(String modeloRam, int tamanoRam, String marcaRam) {
		this.modelo = modeloRam;
		this.tamano = tamanoRam;
		this.marca = marcaRam;
	}

	/**
	 * Titulo de la clase
	 *
	 * @return the string
	 */
	public String title() {
		return this.getTamano()+" gb";
	}

	/**
	 * Nombre de icono
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Memoria";
	}

	// //////////////////////////////////////
	// Modelo (Atributo)
	// //////////////////////////////////////
	/**
	 * Modelo de memori.
	 */
	private String modelo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Modelo de la memoria:")
	@MemberOrder(sequence = "130")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	// //////////////////////////////////////
	// Tamaño (Atributo)
	// //////////////////////////////////////
	/**
	 * Tamaño de la memoria.
	 */
	private int tamano;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tamano de la memoria:")
	@MemberOrder(sequence = "140")
	public int getTamano() {
		return tamano;
	}

	public void setTamano(final int tamaño) {
		this.tamano = tamaño;
	}

	// //////////////////////////////////////
	// Marca (Atributo)
	// //////////////////////////////////////
	/**
	 * Marca de la memoria.
	 */
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca de la memoria:")
	@MemberOrder(sequence = "150")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}
}
