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

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@ObjectType("MEMORIA")
@Audited

public class Memoria {
	
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getMarca();
	}

	public String iconName() {
		return "Disco";
	}
	
	// //////////////////////////////////////
	// Modelo (Atributo)
	// //////////////////////////////////////
	private String modelo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Modelo de la memoria:")
	@MemberOrder(sequence = "10")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	// //////////////////////////////////////
	// Tamaño (Atributo)
	// //////////////////////////////////////
	private int tamaño;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tamano de la memoria:")
	@MemberOrder(sequence = "20")
	public int getTamaño() {
		return tamaño;
	}

	public void setTamaño(final int tamaño) {
		this.tamaño = tamaño;
	}

	// //////////////////////////////////////
	// Marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca de la memoria:")
	@MemberOrder(sequence = "30")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}
}
