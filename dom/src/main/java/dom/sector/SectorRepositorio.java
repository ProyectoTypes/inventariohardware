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
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;

/**
 * SectorRepositorio: permite crear, buscar, eliminar y listar los Sectores que pertenecen al Ministerio de Gobierno, Educación y Justicia. 
 * @author ProyectoTypes
 * @since 25/05/2014
 * @version 1.0.0
 */

@DomainService(menuOrder = "30")
@Named("Sector")
public class SectorRepositorio {
	
	/**
	 * Retorna el nombre del icono para el Sector.
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
	 * Listar Sector: permite listar todos los Sectores ingresados al sistema.
	 * @param nombreSector
	 * @return 
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "10")
	@Named("Listar Sector")
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
	 * Create: obtiene los datos cargados del Sector.
	 * @param nombreSector
	 * @return 
	 */
    @MemberOrder(sequence = "20")
    @Named ("Crear Sector")
    public Sector create(
    		final @RegEx(validation = "[A-Za-z]+") @Named("Nombre Sector") String nombreSector){
    	return nuevoSector(nombreSector, this.currentUserName());
    }
    
	/**
	 * Nuevo Sector: toma los datos del Sector y los persiste.
	 * @param nombreSector
	 * @param creadoPor
	 * @return 
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
	 * Buscar: permite realizar la búsqueda de los Sectores cargados.
	 * @param nombreSector
	 * @return
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Named("Buscar Sector")
	@MemberOrder(sequence = "30")
	public List<Sector> buscar(
			final @RegEx(validation = "[A-Za-z]+") @Named("Nombre") @MinLength(2) String nombreSector) {
		final List<Sector> listarSectores = this.container
				.allMatches(new QueryDefault<Sector>(Sector.class,
						"buscarPorNombre", "nombreSector", nombreSector
								.toUpperCase().trim()));
		if (listarSectores.isEmpty())
			this.container
					.warnUser("No se encontraron Sectores cargados en el sistema.");
		return listarSectores;
	}

	/**
	 * Método que contiene la capacidad de autocompletar las propiedades de referencia del Sector y 
	 * permite mostrarlas en un cuadro de lista despegable.
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
	 * Eliminar Sector: permite eliminar un Sector.
	 * @param delSector
	 * @param seguro
	 * @return
	 */
	@Hidden(where = Where.OBJECT_FORMS)    
    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(sequence = "40")
    @Named("Eliminar Sector")    
    public String removeSector(@Named("Eliminar: ") Sector delSector, @Named("¿Está seguro?") Boolean seguro) {
		delSector.setHabilitado('N');
		String remSector = delSector.title();						
		return  remSector + " fue eliminado.";
	}
	
	/**
	 * Lista que devuelve los Sectores ha eliminar.
	 * @return
	 */
	public List<Sector> choices0RemoveSector(){
		return filtroSE(container.allMatches(new QueryDefault<Sector>(Sector.class,
				"ListarSectores")),'S');
	}
	
	/**
	 * Confirma la eliminación del Sector.
	 * @param delSector
	 * @param seguro
	 * @return
	 */
	public String validateRemoveSector(Sector delSector, Boolean seguro) {
		if (!seguro) {
			return "Marque en la opción si está seguro.";
		}
		return null;
	} 

	/**
	 * CurrentUser: devuelve el nombre del Usuario logueado.
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