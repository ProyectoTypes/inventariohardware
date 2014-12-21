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
package dom.sector;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

/**
 * Clase SectorRepositorio.
 */
@DomainService(menuOrder = "50")
@Named("SECTOR")
public class SectorRepositorio {

	public SectorRepositorio() {

	}

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "sector";
	}

	/**
	 * Nombre del Icono.
	 * @return the string
	 */
	public String iconName() {
		return "Sector";
	}

	/**
	 * Obtiene los datos cargados del Sector.
	 * @param nombreSector
	 * @return 
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Named("Agregar")
	@MemberOrder(sequence = "10")
	public Sector create(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombreSector) {
		return nuevoSector(nombreSector, this.currentUserName());
	}

	/**
	 * Nuevo Sector: toma los datos del Sector y lo persiste.
	 * @param nombreSector
	 * @param creadoPor
	 * @return 
	 */
	@Programmatic
	public Sector nuevoSector(final String nombreSector, final String creadoPor) {
		final Sector unSector = this.container
				.newTransientInstance(Sector.class);
		unSector.setNombreSector(nombreSector.toUpperCase().trim());
		unSector.setHabilitado('S');
		unSector.setCreadoPor(creadoPor);
		this.container.persistIfNotAlready(unSector);
		this.container.flush();
		return unSector;

	}

	/**
	 * Buscar Sector.
	 * @param nombreSector
	 * @return 
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "20")
	@Named("--Listar Sector")
	public List<Sector> listAll() {
		return filtroSE(container.allMatches(new QueryDefault<Sector>(
				Sector.class, "ListarSectores")), 'S');
	}

	private List<Sector> filtroSE(List<Sector> Sectores, char S) {
		List<Sector> filtroSE = new ArrayList<Sector>();

		for (Sector sec : Sectores) {
			if (sec.getEstaHabilitado() == S)
				filtroSE.add(sec);
		}
		return filtroSE;
	}

	/**
	 * Buscar
	 * @param nombreSector
	 * @return
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "21")
	public List<Sector> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") @MinLength(2) String nombreSector) {
		final List<Sector> listarSectores = this.container
				.allMatches(new QueryDefault<Sector>(Sector.class,
						"buscarPorNombre", "nombreSector", nombreSector
								.toUpperCase().trim()));
		if (listarSectores.isEmpty())
			this.container
					.warnUser("No se encontraron sectores cargados en el sistema.");
		return listarSectores;
	}

	/**
	 * Auto complete.
	 * @param buscarNombreSector
	 * @return 
	 */
	@Programmatic
	public List<Sector> autoComplete(final String buscarNombreSector) {
		return container.allMatches(new QueryDefault<Sector>(Sector.class,
				"autoCompletePorNombreSector", "nombreSector",
				buscarNombreSector.toUpperCase().trim()));
	}

	/**
	 * Crear sectores.
	 * @return the list
	 */
	@Prototype
	public List<Sector> crearSectores() {
		List<Sector> sectores = new ArrayList<Sector>();
		sectores.add(this.create("Administración"));
		sectores.add(this.create("Informatica"));
		sectores.add(this.create("Ventas"));
		sectores.add(this.create("Recepción"));
		sectores.add(this.create("RRHH"));
		return sectores;
	}

	/**
	 * Devuelve el nombre del Usuario logueado.
	 * @return
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