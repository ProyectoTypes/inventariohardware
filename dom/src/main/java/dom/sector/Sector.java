/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package dom.sector;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Sector_nombreSector_must_be_unique", members = {
		"creadoPor", "nombreSector" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorNombreSector", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector " + "WHERE creadoPor == :creadoPor && nombreSector.indexOf(:apellido) >= 0"),
		@javax.jdo.annotations.Query(name = "todosLosSectores", language = "JDOQL", value = "SELECT FROM dom.sector.Sector WHERE creadoPor == :creadoPor && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarSectorFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarSectorTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && nombreSector == :nombre") })
@ObjectType("SECTOR")
@Audited
@AutoComplete(repository = SectorRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Sector implements Comparable<Sector> {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	public String title() {
		return this.getNombreSector();
	}

	public String iconName() {
		return "Tecnico";
	}

	// //////////////////////////////////////
	// Descripcion de las propiedades.
	// //////////////////////////////////////
	private String nombreSector;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Nombre del Sector:")
	@MemberOrder(sequence = "10")
	public String getNombreSector() {
		return nombreSector;
	}

	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
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

	// //////////////////////////////////////
	// Complete (property),
	// Se utiliza en las acciones add (action), DeshacerAgregar (action)
	// //////////////////////////////////////

	private boolean complete;

	@Disabled
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	// implements Comparable<Sector> ...Necesaria para realizar un Orden
	// natural.
	@Override
	public int compareTo(final Sector sector) {
		return ObjectContracts.compare(this, sector, "nombreSector");
	}

	@javax.inject.Inject
	private DomainObjectContainer container;

	@javax.inject.Inject
	private SectorRepositorio sectorRepositorio;
	

	/**
	 * Agregando relacion entre sector y persona (1:n bidir forenkey).
	 */
	// {{ Persona (Collection)
	@Persistent(mappedBy = "sector", dependentElement = "False")
	private SortedSet<Persona> personas = new TreeSet<Persona>();

	@MemberOrder(sequence = "1")
	public SortedSet<Persona> getPersona() {
		return personas;
	}

	public void setPersona(final SortedSet<Persona> personas) {
		this.personas = personas;
	}
	// }}
}