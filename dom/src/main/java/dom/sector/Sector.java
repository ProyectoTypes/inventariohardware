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
package dom.sector;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

/**
 * Sector: clase que representa los Sectores que pertenecen al Ministerio de Gobierno, Educación y Justicia. 
 * @author ProyectoTypes
 * @since 25/05/2014
 * @version 1.0.0
 */

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Sector_nombreSector_must_be_unique", members = {
		"creadoPor", "nombreSector" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorNombreSector", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "
				+ "WHERE nombreSector.indexOf(:nombreSector) >= 0"),
		@javax.jdo.annotations.Query(name = "ListarSectores", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "
				+ "WHERE nombreSector.indexOf(:nombreSector) >= 0") })
@ObjectType("SECTOR")
@AutoComplete(repository = SectorRepositorio.class, action = "autoComplete")
@MemberGroupLayout(columnSpans = { 3, 0, 0, 9 })
public class Sector implements Comparable<Sector> {

	/**
	 * Titulo de la clase.
	 * @return the string
	 */
	public String title() {
		return this.getNombreSector();
	}

	/**
	 * Nombre del Icono.
	 * @return 
	 */
	public String iconName() {
		return "Sector";
	}

	/**
	 * Nombre del Sector.
	 */
	private String nombreSector;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre del Sector:")
	@MemberOrder(sequence = "10")
	public String getNombreSector() {
		return nombreSector;
	}

	public void setNombreSector(final String nombreSector) {
		this.nombreSector = nombreSector;
	}

	/**
	 * Creado Por.
	 */
	private String creadoPor;

	@Hidden(where = Where.ALL_TABLES)
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(final String creadoPor) {
		this.creadoPor = creadoPor;
	}

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	public char habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	public char getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final char habilitado) {
		this.habilitado = habilitado;
	}

	/**
	 * Implementacion de la interface comparable, necesaria para toda entidad.
	 * @param sector
	 * @return 
	 */
	@Override
	public int compareTo(final Sector sector) {
		return ObjectContracts.compare(this, sector, "nombreSector");
	}
}