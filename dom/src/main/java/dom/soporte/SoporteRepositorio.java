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
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;
import dom.tecnico.TecnicoRepositorio;

// TODO: Auto-generated Javadoc
/**
 * Clase SoporteRepositorio.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */
@DomainService(menuOrder = "20")
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
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Soporte> listAll() {
		final List<Soporte> lista = container.allMatches(new QueryDefault<Soporte>(Soporte.class, "listar"));
		if (lista.isEmpty()) {
			container.warnUser("No hay Soportes cargados en el sistema.");
		}
		return lista;
	}

	// //////////////////////////////////////
	// Insertar un Soporte.
	// //////////////////////////////////////

	@Named("Recepcion")
	@MemberOrder(sequence = "10")
	public Soporte create(final @Named("Computadora") Computadora computadora,
			final @Named("Observaciones") String observaciones) {
		return nuevoSoporte(computadora, observaciones, this.currentUserName());
	}

	@Programmatic
	public Soporte nuevoSoporte(final Computadora computadora,
			final String observaciones, final String creadoPor) {

		final Soporte unSoporte = container.newTransientInstance(Soporte.class);
		unSoporte.setHabilitado(true);
		unSoporte.setCreadoPor(creadoPor);
		unSoporte.setObservaciones(observaciones);
		unSoporte.setFecha(LocalDate.now());
		unSoporte.setTime_system(LocalDateTime.now().withMillisOfSecond(2));
		computadora.addToSoporte(unSoporte);
		container.persistIfNotAlready(unSoporte);
		container.flush();
		return unSoporte;

	}

	public String validateCreate(final Computadora computadora,
			final String observaciones) {

		List<Soporte> soporte = container.allMatches(new QueryDefault<Soporte>(
				Soporte.class, "seEncuentraEnReparacion", "ip", computadora.getPlacaDeRed()
						.getIp()));
		if (soporte.isEmpty())
			return null;
		return "La computadora se encuentra en reparacion.";
	}

	public List<Computadora> autoComplete0Create(final @MinLength(2) String search) {
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
		return container.allMatches(new QueryDefault<Soporte>(Soporte.class,
				"autoCompleteSoporte", "buscarTecnico", buscarTecnico
						.toUpperCase().trim()));
	}

	/**
	 * Devuelve una lista de aquellos soportes que se encuentran en espera.
	 * 
	 * @return
	 */
	@Programmatic
	public static List<Soporte> queryBuscarSoportesEnEspera() {
		final List<Soporte> lista = container
				.allMatches(new QueryDefault<Soporte>(Soporte.class,
						"buscarSoportesEnEspera"));
		if (lista.isEmpty())
			container.warnUser("No hay computadoras en espera de soporte.");
		return lista;
	}

	/**
	 * Devuelve una lista de aquellos soportes que se encuentran en reparacion.
	 * 
	 * @return lista
	 */
	@Programmatic
	public static List<Soporte> queryBuscarSoportesEnReparacion() {
		final List<Soporte> lista = container
				.allMatches(new QueryDefault<Soporte>(Soporte.class,
						"buscarSoportesEnReparacion"));
		if (lista.isEmpty())
			container.warnUser("No hay computadoras en Reparacion.");
		return lista;
	}

	@Named("Buscar")
	@DescribedAs("Busca todos los soportes que tuvo una computadora")
	public List<Soporte> buscarPorIp(
			@Named("Computadora") Computadora computadora) {
		return container.allMatches(new QueryDefault<Soporte>(Soporte.class,
				"buscarPorIp", "ip", computadora.getPlacaDeRed().getIp()));
	}

	public List<Computadora> choices0BuscarPorIp() {
		return computadoraRepositorio.listAll();
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

	/** Container. */
	@javax.inject.Inject
	private static DomainObjectContainer container;

	/** Tecnico repositorio. */
	@SuppressWarnings("unused")
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	/** Computadora repositorio. */
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}