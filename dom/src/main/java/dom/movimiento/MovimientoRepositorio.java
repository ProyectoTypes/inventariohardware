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
package dom.movimiento;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
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

@Named("Movimiento")
public class MovimientoRepositorio {

	public MovimientoRepositorio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "movimiento";
	}

	public String iconName() {
		return "Tecnico";
	}

	// //////////////////////////////////////
	// Insertar un Movimiento.
	// //////////////////////////////////////

	@Named("Recepcion")
	@MemberOrder(sequence = "10")
	@PublishedAction
	public Movimiento add(final @Named("Computadora") Computadora computadora,
			final @Named("Observaciones") String observaciones) {
		return nuevoMovimiento(computadora, observaciones,
				this.currentUserName());
	}

	@Programmatic
	public Movimiento nuevoMovimiento(final Computadora computadora,
			final String observaciones, final String creadoPor) {

		final Movimiento unMovimiento = this.container
				.newTransientInstance(Movimiento.class);
		unMovimiento.setHabilitado(true);
		unMovimiento.setCreadoPor(creadoPor);
		// unMovimiento.setComputadora(computadora);
		unMovimiento.setObservaciones(observaciones);
		unMovimiento.setFecha(LocalDate.now());
		unMovimiento.setTime_system(LocalDateTime.now().withMillisOfSecond(2));
		computadora.addToMovimiento(unMovimiento);
		// Recibido estadoInicial =
		// this.container.newTransientInstance(Recibido.class);
		// unMovimiento.setRecepcionado(estadoInicial);
		this.container.persistIfNotAlready(unMovimiento);
		this.container.flush();
		return unMovimiento;

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
	public List<Movimiento> autoComplete(final String buscarTecnico) {
		return container.allMatches(new QueryDefault<Movimiento>(
				Movimiento.class, "autoCompleteMovimiento", "creadoPor", this
						.currentUserName(), "buscarTecnico", buscarTecnico
						.toUpperCase().trim()));
	}

	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Movimiento> listar() {
		final List<Movimiento> listaMovimientos = this.container
				.allMatches(new QueryDefault<Movimiento>(Movimiento.class,
						"listar"));
		if (listaMovimientos.isEmpty()) {
			this.container
					.warnUser("No hay Movimiento cargados en el sistema.");
		}
		return listaMovimientos;
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