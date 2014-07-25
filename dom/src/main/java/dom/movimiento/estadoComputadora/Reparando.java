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
package dom.movimiento.estadoComputadora;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

public class Reparando implements IEstado{
	Movimiento movimiento;
	public Reparando(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}

	@Override
	public void equipoRecibido() {
		this.container.warnUser("Reparando.java - recepcionando(): El equipo ya ha sido recepcionado");
		
	}
	@Override
	public void equipoReparado() {
		this.movimiento.setEstado(this.movimiento.getCancelado());
		
	}
	@Override
	public void equipoFinalizado() {
		this.container.warnUser("Recepcionado.java - finalizado(): El equipo NO ha sido Recepcionado/Reparado");
		
	}
	
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	public String toString() {
		return "EQUIPO EN REPARACION";
	}
}
