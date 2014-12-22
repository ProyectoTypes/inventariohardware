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
package dom.computadora.hardware.impresora;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.hardware.impresora.Impresora.TipoImpresora;

/**
 * Clase ImpresoraRepositorio.
 */
@DomainService(menuOrder = "70")
@Named("IMPRESORA")
public class ImpresoraRepositorio {

	/**
	 * Título de la Clase
	 * @return the string
	 */
	public String title() {
		return "IMPRESORA";
	}

	/**
	 * Título del icono
	 * @return the string
	 */
	public String iconName() {
		return "IMPRESORA";
	}
	
	
	/**
	 * Método para listar las impresoras.
	 * @return the list
	 */
    @MemberOrder(name = "Hardware", sequence = "2")
	@Named("Listar Impresoras")
	public List<Impresora> listAll() {
		final List<Impresora> listaImpresora = this.container
				.allMatches(new QueryDefault<Impresora>(Impresora.class,
						"todasLasImpresoras"));
		return listaImpresora;
	}
	

	/**
	 * Agregar Impresora.
	 * @param modeloImpresora
	 * @param fabricanteImpresora
	 * @param tipoImpresora
	 * @return 
	 */
	@MemberOrder(name = "Hardware", sequence = "10")
	@Named("Agregar Impresora")
	public Impresora create(
			final @Named("Modelo") String modeloImpresora,
			final @Named("Fabricante") String fabricanteImpresora,
			final @Named("Tipo") TipoImpresora tipoImpresora) {
		return nuevaImpresora(modeloImpresora, fabricanteImpresora,
				tipoImpresora, this.currentUserName());
	}

	/**
	 * Nueva impresora.
	 * @param modeloImpresora
	 * @param fabricanteImpresora
	 * @param tipoImpresora
	 * @param creadoPor
	 * @return unImpresora
	 */
	@Programmatic
	public Impresora nuevaImpresora(final String modeloImpresora,
			final String fabricanteImpresora,
			final TipoImpresora tipoImpresora, final String creadoPor) {
		final Impresora unaImpresora = container
				.newTransientInstance(Impresora.class);
		unaImpresora.setModeloImpresora(modeloImpresora.toUpperCase().trim());
		unaImpresora.setFabricanteImpresora(fabricanteImpresora.toUpperCase()
				.trim());
		unaImpresora.setTipo(tipoImpresora);
		unaImpresora.setHabilitado(true);
		unaImpresora.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unaImpresora);
		container.flush();
		return unaImpresora;
	}
	
	
	/**
	 * Buscar Impresora.
	 * @param search
	 * @return
	 */
	@DescribedAs("Buscar Impresora Mayuscula")
	public List<Impresora> autoComplete0Create(
			final @MinLength(2) String search) {
		return impresoraRepositorio.autoComplete(search);
	}

	/**
	 * Buscar Impresora.
	 * @param modeloImpresora
	 * @return
	 */
	@Hidden
	@MemberOrder(name = "Hardware", sequence = "20")
	@Named("Buscar Impresora")
	public List<Impresora> buscar(
			final @Named("Modelo") @MinLength(2) String modeloImpresora) {
		final List<Impresora> listaImpresora = this.container
				.allMatches(new QueryDefault<Impresora>(Impresora.class,
						"buscarPormodeloImpresora", "creadoPor", this
								.currentUserName(), "modeloImpresora",
						modeloImpresora.toUpperCase().trim()));
		if (listaImpresora.isEmpty()) {
			this.container
					.warnUser("No se encontraron Impresoras cargados en el sistema.");
			return null;
		}
		return listaImpresora;
	}

	/**
	 * AutoComplete
	 * @param modelo
	 * @return
	 */
	@Programmatic
	public List<Impresora> autoComplete(final String modelo) {
		return container.allMatches(new QueryDefault<Impresora>(
				Impresora.class, "autoCompletePorModeloImpresora",
				"modeloImpresora", modelo.toUpperCase().trim()));
	}

	/**
	 * Current User.
	 * @return container.getUser().getName();
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	/**
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;

	/**
	 * Inyección del servicio del Sector.
	 */
	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;
}
