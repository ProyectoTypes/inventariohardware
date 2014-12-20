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
package dom.insumo;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

/**
 * Clase InsumoRepositorio.
 */
@DomainService
@Named("INSUMO")
@Hidden
public class InsumoRepositorio {

	/**
	 * Titulo de la clase.
	 * @return the string
	 */
	public String title() {
		return "Insumo";
	}

	/**
	 * Nombre del icono
	 * @return the string
	 */
	public String iconName() {
		return "Insumo";
	}

	/**
	 * Adds the insumo.
	 *
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param modelo
	 * @return insumo
	 */
	@NotContributed
	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Insumo create(final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Named("Modelo") String modelo) {
		return nuevosInsumo(cantidad, producto, marca, modelo,
				this.currentUserName());
	}

	/**
	 * Nuevos insumo.
	 *
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param modelo
	 * @param creadoPor
	 * @return insumo
	 */
	@Programmatic
	public Insumo nuevosInsumo(final int cantidad, final String producto,
			final String marca, final String modelo, final String creadoPor) {
		final Insumo unInsumo = container.newTransientInstance(Insumo.class);
		unInsumo.setCantidad(cantidad);
		unInsumo.setProducto(producto.toUpperCase().trim());
		unInsumo.setMarca(marca.toUpperCase().trim());
		unInsumo.setModelo(modelo.toUpperCase().trim());
		unInsumo.setFecha(LocalDate.now());
		unInsumo.setHabilitado(true);
		unInsumo.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unInsumo);
		container.flush();
		return unInsumo;
	}

	/**
	 * Listado de Insumos.
	 * @return the list
	 */
	@MemberOrder(sequence = "100")
	public List<Insumo> listAll() {
		final List<Insumo> listaInsumo = this.container
				.allMatches(new QueryDefault<Insumo>(Insumo.class,
						"listarInsumoTrue"));
		if (listaInsumo.isEmpty()) {
			this.container.warnUser("No hay Insumos cargados en el sistema.");
		}
		return listaInsumo;
	}

	/**
	 * Current user name.
	 * @return string
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	/**
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;
}
