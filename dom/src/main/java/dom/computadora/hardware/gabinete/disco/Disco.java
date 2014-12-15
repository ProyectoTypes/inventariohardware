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
package dom.computadora.hardware.gabinete.disco;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

// TODO: Auto-generated Javadoc
/**
 * The Class Disco.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
		+ "FROM dom.computadora.hardware.gabinete.disco.Disco") })
@ObjectType("Hdd")
public class Disco {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public Disco(String marcaDisco, CategoriaDisco tipoDisco, int tamanoDisco) {
		this.marca = marcaDisco;
		this.tipo = tipoDisco;
		this.tamano = tamanoDisco;
	}

	public String title() {
		return this.getTamano()+" gb";
	}

	public String iconName() {
		return "Disco";
	}

	// //////////////////////////////////////
	// tipo (Atributo)
	// //////////////////////////////////////

	public static enum CategoriaDisco {
		SATA, IDE, SCSI, SAS;
	}

	private CategoriaDisco tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tipo de disco:")
	@MemberOrder(sequence = "100")
	public CategoriaDisco getTipo() {
		return tipo;
	}

	public void setTipo(final CategoriaDisco tipo) {
		this.tipo = tipo;
	}

	// //////////////////////////////////////
	// Tamaño (Atributo)
	// //////////////////////////////////////
	private int tamano;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tamano del disco:")
	@MemberOrder(sequence = "110")
	public int getTamano() {
		return tamano;
	}

	public void setTamano(final int tamaño) {
		this.tamano = tamaño;
	}

	// //////////////////////////////////////
	// Marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del disco:")
	@MemberOrder(sequence = "120")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}
}
