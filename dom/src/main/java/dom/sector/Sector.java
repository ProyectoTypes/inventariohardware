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

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.SortedBy;
import org.apache.isis.applib.annotation.TypicalLength;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import com.google.common.collect.Ordering;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Sector_nombreSector_must_be_unique", members = {
		"creadoPor", "nombreSector" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarCreadoPorYNombreSector", language = "JDOQL", value = "SELECT "
				+ "FROM dom.sector.Sector " + "WHERE creadoPor == :creadoPor"),
		@javax.jdo.annotations.Query(
				name = "todosLosSectores", language = "JDOQL",
				value = "SELECT FROM dom.sector.Sector WHERE creadoPor == :creadoPor && habilitado == true") })
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
	// Dependencies (collection),
	// Add (action), Remove (action)
	// //////////////////////////////////////

	// Sobreescribir el orden natural.
	public static class ComparadorDependeciasSector implements
			Comparator<Sector> {
		@Override
		public int compare(Sector p, Sector q) {
			Ordering<Sector> byNombreSector = new Ordering<Sector>() {
				public int compare(final Sector p, final Sector q) {
					return Ordering.natural().nullsFirst()
							.compare(p.getNombreSector(), q.getNombreSector());
				}
			};
			return byNombreSector.compound(Ordering.<Sector> natural())
					.compare(p, q);
		}
	}

	// //////////////////////////////////////
	// Dependencies
	// //////////////////////////////////////
	@javax.jdo.annotations.Persistent(table = "SectorDependencias")
	@javax.jdo.annotations.Join(column = "dependingId")
	@javax.jdo.annotations.Element(column = "dependentId")
	private SortedSet<Sector> dependencias = new TreeSet<Sector>();

	@SortedBy(ComparadorDependeciasSector.class)
	public SortedSet<Sector> getDependencias() {
		return dependencias;
	}

	public void setDependencias(SortedSet<Sector> dependencias) {
		this.dependencias = dependencias;
	}

	@PublishedAction
	public Sector agregar(@TypicalLength(20) final Sector sector) {
		this.getDependencias().add(sector);
		return this;
	}

	public List<Sector> autoComplete0Agregar(
			final @MinLength(2) String buscarNombreSector) {
		final List<Sector> lista = sectorRepositorio
				.autoComplete(buscarNombreSector);
		lista.removeAll(this.getDependencias());
		lista.remove(this);
		return lista;
	}

	public String deshacerAgregar(final Sector sector) {
		if (this.isComplete()) {
			return "No se puede agregar dependecia";
		}
		return null;
	}

	// Validar argumento invocado por la accion.
	public String validateAgregar(final Sector sector) {
		if (this.getDependencias().contains(sector)) {
			return "Ya existe la dependecia";
		}
		if (sector == this) {
			return "No se puede agregar esa dependencia";
		}
		return null;
	}

	@Named("Remover")
	public Sector remove(@TypicalLength(20) final Sector sector) {
		this.getDependencias().remove(sector);
		return this;
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
	private SectorRepositorio sectorRepositorio;
}