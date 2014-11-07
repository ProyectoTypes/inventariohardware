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

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.memoria.Memoria;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;
import dom.computadora.hardware.impresora.Impresora;
import dom.tecnico.Tecnico;

public interface IEstado {
	void asignarTecnico(final Tecnico tecnico);

	void solicitarInsumos(final int cantidad, final String producto,
			final String marca, final String modelo);

	void finalizarSoporte();

	void asignarNuevoEquipo(final PlacaDeRed placaDeRed, final Motherboard motherboard,
			final Procesador procesador, final Disco disco,
			final Memoria memoria, final Impresora impresora);

	boolean escondeAsignarTecnico();

	boolean escondeFinalizarSoporte();

	boolean escondeSolicitarInsumos();

	boolean escondeLlegaronInsumos();

	boolean escondeNoHayInsumos();

	boolean escondeAsignarNuevoEquipo();

}
