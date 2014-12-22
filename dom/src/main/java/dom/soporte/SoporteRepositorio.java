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
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Bookmarkable;
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

/**
 * SoporteRepositorio: permite crear y listar los Soportes realizados.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */
@DomainService(menuOrder = "50")
@Named("Soporte")
public class SoporteRepositorio {

	/**
	 * Constructor vacío de la clase.
	 */
	public SoporteRepositorio() {}

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "soporte";
	}

	/**
	 * Nombre del Icono.
	 * @return
	 */
	public String iconName() {
		return "Tecnico";
	}
	
	/**
	 * Listar Soporte: permite listar todos los Soportes realizados.
	 * @return
	 */
    @Bookmarkable
    @ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "20")
    @Named("Listar Soporte")
	public List<Soporte> listAll() {
		final List<Soporte> lista = container.allMatches(new QueryDefault<Soporte>(Soporte.class, "listar"));
		if (lista.isEmpty()) {
			container.informUser("No hay Soportes cargados en el sistema.");
		}
		return lista;
	}

    /**
     * Recepción: permite que el Técnico recepcione una Computadora.
     * @param computadora
     * @param observaciones
     * @return
     */
	@Named("Recepción")
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

	/**
	 * Verifica que la Computadora se encuentre en el estado reparación.
	 * @param computadora
	 * @param observaciones
	 * @return
	 */
	public String validateCreate(final Computadora computadora,
			final String observaciones) {

		List<Soporte> soporte = container.allMatches(new QueryDefault<Soporte>(
				Soporte.class, "seEncuentraEnReparacion", "ip", computadora.getIp()));
		if (soporte.isEmpty())
			return null;
		return "La computadora se encuentra en reparacion.";
	}

	public List<Computadora> autoComplete0Create(final @MinLength(2) String search) {
		List<Computadora> listaComputadora = computadoraRepositorio
				.autoComplete(search.toUpperCase().trim());
		return listaComputadora;
	}

	/**
	 * Servicio utilizado por Sector.
	 */
	@Programmatic
	public List<Soporte> autoComplete(final String buscarTecnico) {
		return container.allMatches(new QueryDefault<Soporte>(Soporte.class,
				"autoCompleteSoporte", "buscarTecnico", buscarTecnico
						.toUpperCase().trim()));
	}

	/**
	 * Devuelve una lista de aquellos soportes que se encuentran en espera.
	 * @return
	 */
	@Programmatic
	public static List<Soporte> queryBuscarSoportesEnEspera() {
		final List<Soporte> lista = container
				.allMatches(new QueryDefault<Soporte>(Soporte.class,
						"buscarSoportesEnEspera"));
		if (lista.isEmpty())
			container.informUser("No hay computadoras en espera de soporte.");
		return lista;
	}

	/**
	 * Devuelve una lista de aquellos soportes que se encuentran en Reparación.
	 * @return lista
	 */
	@Programmatic
	public static List<Soporte> queryBuscarSoportesEnReparacion() {
		final List<Soporte> lista = container
				.allMatches(new QueryDefault<Soporte>(Soporte.class,
						"buscarSoportesEnReparacion"));
		if (lista.isEmpty())
			container.informUser("No hay Computadoras en Reparación.");
		return lista;
	}

	/**
	 * Busca todos los Soportes que tuvo una Computadora.
	 * @param computadora
	 * @return
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Named("Buscar")
	@DescribedAs("Busca todos los soportes que tuvo una Computadora.")
	public List<Soporte> buscarPorIp(
			@Named("Computadora") Computadora computadora) {
		return container.allMatches(new QueryDefault<Soporte>(Soporte.class,
				"buscarPorIp", "ip", computadora.getIp()));
	}

	public List<Computadora> choices0BuscarPorIp() {
		return computadoraRepositorio.listAll();
	}

	/**
	 * Devuelve el nombre del Técnico logueado.
	 * @return
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	/**
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	private static DomainObjectContainer container;

	/**
	 * Inyección del servicio Técnico.
	 */
	@SuppressWarnings("unused")
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	/**
	 *Inyección del servicio de Computadora. 
	 */
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}