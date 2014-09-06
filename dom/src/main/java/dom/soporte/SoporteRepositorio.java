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
package dom.soporte;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;
import dom.tecnico.TecnicoRepositorio;

@DomainService
@Named("Soporte")
public class SoporteRepositorio {

	public SoporteRepositorio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "soporte";
	}

	public String iconName() {
		return "Tecnico";
	}

	// //////////////////////////////////////
	// Insertar un Soporte.
	// //////////////////////////////////////

	@Named("Recepcion")
	@MemberOrder(sequence = "10")
	@PublishedAction
	public Soporte add(final @Named("Computadora") Computadora computadora,
			final @Named("Observaciones") String observaciones) {
		return nuevoSoporte(computadora, observaciones,
				this.currentUserName());
	}

	@Programmatic
	public Soporte nuevoSoporte(final Computadora computadora,
			final String observaciones, final String creadoPor) {

		final Soporte unSoporte = this.container
				.newTransientInstance(Soporte.class);
		unSoporte.setHabilitado(true);
		unSoporte.setCreadoPor(creadoPor);
		unSoporte.setObservaciones(observaciones);
		unSoporte.setFecha(LocalDate.now());
		unSoporte.setTime_system(LocalDateTime.now().withMillisOfSecond(2));
		computadora.addToSoporte(unSoporte);
		this.container.persistIfNotAlready(unSoporte);
		this.container.flush();
		return unSoporte;

	}

	public List<Computadora> autoComplete0Add(final @MinLength(2) String search) {
		List<Computadora> listaComputadora = computadoraRepositorio
				.autoComplete(search.toUpperCase().trim());
		return listaComputadora;
	}

	// ///////////////////////////////////////
	// AutoComplete
	// ///////////////////////////////////////

	/**
	 * Servicio utilizado por Sector
	 * 
	 */
	@Programmatic
	public List<Soporte> autoComplete(final String buscarTecnico) {
		return container.allMatches(new QueryDefault<Soporte>(
				Soporte.class, "autoCompleteSoporte", "creadoPor", this
						.currentUserName(), "buscarTecnico", buscarTecnico
						.toUpperCase().trim()));
	}

	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Soporte> listar() {
		final List<Soporte> lista = this.container
				.allMatches(new QueryDefault<Soporte>(Soporte.class,
						"listar"));
		if (lista.isEmpty()) {
			this.container
					.warnUser("No hay Soportes cargados en el sistema.");
		}
		return lista;
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;

	@SuppressWarnings("unused")
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}