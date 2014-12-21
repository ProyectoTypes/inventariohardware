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
package dom.persona;

import java.util.List;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

/**
 * Persona: clase abstracta que contiene los atributos pincipales del objeto.
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Persona {

	/**
	 * Apellido de la Persona.
	 */
	private String apellido;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Apellido de la Persona:")
	@RegEx(validation = "[A-Za-z]+")
	@MemberOrder(sequence = "10")
	public String getApellido() {
		return apellido;
	}

	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}

	/**
	 * Nombre de la Persona.
	 */
	private String nombre;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre de la Persona:")
	@RegEx(validation = "[A-Za-z]+")
	@MemberOrder(sequence = "20")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Email de la Persona.
	 */
	private String email;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	@MemberOrder(sequence = "30")
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Propiedad Habilitado.
	 */
	public boolean habilitado;

	@Hidden
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	/**
	 * Creado Por.
	 */
	private String creadoPor;

	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(final String creadoPor) {
		this.creadoPor = creadoPor;
	}

	/**
	 * Sector de la Persona.
	 */
	private Sector sector;

	@MemberOrder(sequence = "100")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Sector getSector() {
		return sector;
	}

	public void setSector(final Sector sector) {
		this.sector = sector;
	}

	/**
	 * Modificar Sector: permite modificar el Sector que posee un Usuario.
	 * @param sector
	 * @return persona
	 */
	@MemberOrder(sequence = "110")
	@Named("Modificar Sector")
	public Persona mod(final Sector sector) {
		Sector currentSector = getSector();
		if (sector == null || sector.equals(currentSector)) {
			return this;
		}
		return this;
	}

	/**
	 * Autocomplete0mod: método que completa el sector mientras se lo va cargando.
	 * @param search
	 * @return list
	 */
	@Named("Sector")
	@DescribedAs("Buscar el Sector")
	public List<Sector> autoComplete0Mod(final @MinLength(2) String search) {
		return sectorRepositorio.autoComplete(search);
	}

	/**
	 * Autocompletesector: método que completa el sector mientras se lo va cargando.
	 * @param search
	 * @return list
	 */
	@Named("Sector")
	@DescribedAs("Buscar el Sector")
	public List<Sector> autoCompleteSector(final @MinLength(2) String search) {
		return sectorRepositorio.autoComplete(search);
	}

	/**
	 * Inyección del servicio para Sector.
	 */
	@javax.inject.Inject
	private SectorRepositorio sectorRepositorio;
}