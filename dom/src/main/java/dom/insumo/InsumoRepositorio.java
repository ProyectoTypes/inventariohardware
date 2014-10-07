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
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

@DomainService
@Named("INSUMO")
@Hidden
public class InsumoRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String title() {
		return "Insumo";
	}

	public String iconName() {
		return "Insumo";
	}

	// //////////////////////////////////////
	// Agregar Insumo
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Insumo addInsumo(
			final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Named("Modelo") String modelo) {
		return nuevosInsumo(cantidad, producto, marca, modelo,
				this.currentUserName());
	}

	@Programmatic
	public Insumo nuevosInsumo(final int cantidad,
			final String producto, final String marca,
			final String modelo, final String creadoPor) {
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

	// //////////////////////////////////////
	// Listar Insumos
	// //////////////////////////////////////

	@MemberOrder(sequence = "100")
	public List<Insumo> listar() {
		final List<Insumo> listaInsumo = this.container
				.allMatches(new QueryDefault<Insumo>(Insumo.class,
						"listarInsumoTrue"));
		if (listaInsumo.isEmpty()) {
			this.container.warnUser("No hay Insumos cargadas en el sistema.");
		}
		return listaInsumo;
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
}
