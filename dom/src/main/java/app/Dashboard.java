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
package app;

import java.util.List;

import org.apache.isis.applib.AbstractViewModel;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;

import dom.soporte.Soporte;
import dom.soporte.SoporteRepositorio;

@MemberGroupLayout(columnSpans = { 0, 0, 0, 12 })
public class Dashboard extends AbstractViewModel {

	public String title() {
		return "Bandeja de Soporte.";
	}

	public String iconName() {
		return "Dashboard";
	}

	// //////////////////////////////////////
	// ViewModel contract
	// //////////////////////////////////////

	private String memento;

	@Override
	public String viewModelMemento() {
		return memento;
	}

	@Override
	public void viewModelInit(String memento) {
		this.memento = memento;
	}

	// //////////////////////////////////////
	// listar soportes Esperando.
	// //////////////////////////////////////
	@Named("En espera")
	@Render(Type.EAGERLY)
	@Disabled
	@MemberOrder(sequence = "10")
	@MultiLine(numberOfLines = 6)
	public List<Soporte> getAllSoportesEsperando() {
		return SoporteRepositorio.queryBuscarSoportesEnEspera();
	}

	// //////////////////////////////////////
	// listar soportes Reparando.
	// //////////////////////////////////////
	@Named("En Reparacion")
	@Render(Type.EAGERLY)
	@Disabled
	@MemberOrder(sequence = "9")
	@MultiLine(numberOfLines = 6)
	public List<Soporte> getAllSoportesReparando() {
		return SoporteRepositorio.queryBuscarSoportesEnReparacion();
	}

}
