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

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.DomainObjectContainer;

import dom.movimiento.Movimiento;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class Cancelado implements IEstado{
	Movimiento movimiento;
	public Cancelado(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}
	@Override
	public void equipoRecibido() {
		this.container.warnUser("Recepcionado.java - equipoRecibido(): El equipo NO ha sido Reparado/Entregado");
		
	}

	@Override
	public void equipoReparado() {
		this.container.warnUser("Recepcionado.java - equipoReparado(): El equipo NO ha sido Recepcionado/Finalizado");
		
	}

	@Override
	public void equipoFinalizado() {
		this.container.warnUser("Cancelado.java - equipoFinalizado(): FIN :) ");
		
	}
	@javax.inject.Inject
	private DomainObjectContainer container;
	
	public String toString() {
		return "EQUIPO CANCELADO";
	}

}
