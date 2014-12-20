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

// TODO: Auto-generated Javadoc
/**
 * Clase SectorRepositorio.
 */
@DomainService(menuOrder = "50")
@Named("SECTOR")
public class SectorRepositorio {

	/**
	 * Instantiates a new sector repositorio.
	 */
	public SectorRepositorio() {

	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "sector";
	}

	/**
	 * Icon name.
	 * @return the string
	 */
	public String iconName() {
		return "Sector";
	}

	/**
	 * Agregar.
	 * @param nombreSector the nombre sector
	 * @return the sector
	 */
	@Named("Agregar")
	@MemberOrder(sequence = "10")
	public Sector create(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Nombre") String nombreSector) {
		return nuevoSector(nombreSector, this.currentUserName());
	}

	/**
	 * Nuevo sector.
	 *
	 * @param nombreSector the nombre sector
	 * @param creadoPor the creado por
	 * @return the sector
	 */
	@Programmatic
	public Sector nuevoSector(final String nombreSector, final String creadoPor) {
		final Sector unSector = this.container.newTransientInstance(Sector.class);
		unSector.setNombreSector(nombreSector.toUpperCase().trim());
		unSector.setHabilitado('S');
		unSector.setCreadoPor(creadoPor);
		this.container.persistIfNotAlready(unSector);
		this.container.flush();
		return unSector;

	}

	/**
	 * Listar.
	 * @return the list
	 */
	@MemberOrder(sequence = "20")
	public List<Sector> listar() {
		if (this.container.getUser().getName().contentEquals("sven"))
			return this.container.allMatches(new QueryDefault<Sector>(
					Sector.class, "listar"));
		else
			return this.container.allMatches(new QueryDefault<Sector>(
					Sector.class, "listarHabilitados"));
	}

	/**
	 * Buscar Sector.
	 * @param nombreSector the nombre sector
	 * @return the list
	 */	
    @Bookmarkable
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "20")
    @Named ("Listar Sector")
    public List<Sector> listAll() {
        return filtroAL(container.allMatches(new QueryDefault<Sector>(Sector.class,
				"ListarSectores")),'S');
    }
    
	private List<Sector> filtroAL(List<Sector> Sectores, char S)
	{
		List<Sector> filtroAL=new ArrayList<Sector>();
		
		for(Sector sec:Sectores)
		{
			if(sec.getEstaHabilitado()==S)
				filtroAL.add(sec);
		}
		
		return filtroAL;
	}

	/**
	 * Buscar
	 * @param nombreSector
	 * @return
	 */
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

	// //////////////////////////////////////
	// AutoComplete
	// //////////////////////////////////////
	/**
	 * Auto complete.
	 * @param buscarNombreSector
	 * @return the list
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

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	/**
	 * Current user name.
	 *
	 * @return the string
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	/** The container. */
	@javax.inject.Inject
	private DomainObjectContainer container;
}