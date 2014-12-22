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
package servicio.reporte;

import java.util.List;

import org.joda.time.LocalDate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.ActionSemantics.Of;

import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

/**
 * ReporteRepositorio. Permite crear y listar los Reportes solicitados.
 * @author ProyectoTypes
 * @since 17/12/2014
 * @version 1.0.0
 */

@DomainService(menuOrder = "60", repositoryFor = Reporte.class)
@Named("Reporte")
public class ReporteRepositorio {

	/**
	 * Obtiene el Id del Reporte.
	 * @return String
	 */
    public String getId() {
        return "Reportes";
    }

    /**
     * Obtiene el ícono del Reporte.
     * @return
     */
    public String iconName() {
        return "Reportes";
    }

    /**
     * Método que permite crear el Reporte.
     * @param numero
     * @param tecnico
     * @param fechaReporte
     * @return obj
     */
    @Named("Generar Reporte")
    @MemberOrder(sequence = "10")
    public Reporte create(
            final @Named("Número de Reporte") int numero,
            final @Named("Apellido del Técnico") Tecnico tecnico,
            final @Named("Fecha de Reporte") LocalDate fechaReporte) {
        final Reporte obj = container.newTransientInstance(Reporte.class);
        obj.setNumero(numero);
        obj.modifyTecnico(tecnico);
        obj.setFechaReporte(fechaReporte);
        container.persistIfNotAlready(obj);
        return obj;
    }
    
    /**
     * Método que permite la búsqueda del Técnico.
     * @param search
     * @return tecnicoRepositorio.autoComplete(search);
     */
	@Named("Buscar Técnico")
	@DescribedAs("Buscar el Técnico en mayúsculas.")
	public List<Tecnico> autoComplete1Create(
			final @MinLength(2) String search) {
		return tecnicoRepositorio.autoComplete(search);
	}
    
	/**
	 * Método que lista los Reportes.
	 * @return container.allInstances(Reporte.class);
	 */
    @Bookmarkable
    @ActionSemantics(Of.SAFE)
    @Named("Listar Reporte")
    @MemberOrder(sequence = "20")
    public List<Reporte> listAll() {
        return container.allInstances(Reporte.class);
    }

    /**
	 * Inyección del contenedor.
	 */
    @javax.inject.Inject 
    DomainObjectContainer container;
    
	/**
	 * Inyección del servicio del Técnico.
	 */
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;
}