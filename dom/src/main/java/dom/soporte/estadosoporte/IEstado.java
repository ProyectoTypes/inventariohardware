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
package dom.soporte.estadosoporte;

import org.apache.isis.applib.annotation.Named;

import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.monitor.Monitor;
import dom.tecnico.Tecnico;

/**
 * Interfaz IEstado
 * Utilizada con el patron State para poder desacoplar el resto de los estados.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */
public interface IEstado {
	void asignarTecnico(final Tecnico tecnico);

	void solicitarInsumos(final int cantidad, final String producto,
			final String marca, final String modelo);

	void finalizarSoporte();

	void asignarNuevoEquipo(
			final @Named("IP") String ip, 
			final @Named("MAC") String mac,
			final @Named("HDD Marca ") String marcaDisco,
			final @Named("HDD Categoria ") CategoriaDisco tipoDisco,
			final @Named("HDD Tamaño ") int tamanoDisco,
			final @Named("CPU Modelo ") String modeloProcesador,
			final @Named("RAM Modelo") String modeloRam,
			final @Named("RAM Tamaño") int tamanoRam,
			final @Named("RAM Marca") String marcaRam,
			final @Named("Modelo Motherboard") String modeloMotherboard,
			final @Named("Fabricante") String fabricante,
			final @Named("Monitor") Monitor monitor,
			final @Named("Impresora") Impresora impresora,final @Named("Rotulo") String rotulo);

	boolean escondeAsignarTecnico();

	boolean escondeFinalizarSoporte();

	boolean escondeSolicitarInsumos();

	boolean escondeLlegaronInsumos();

	boolean escondeNoHayInsumos();

	boolean escondeAsignarNuevoEquipo();

}
