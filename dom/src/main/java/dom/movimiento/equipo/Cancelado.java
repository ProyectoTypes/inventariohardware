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
package dom.movimiento.equipo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idCancelado")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "canceladoUnique", members = { "idCancelado" }) })
@ObjectType("CANCELADO")
@Audited
@Bookmarkable
public class Cancelado implements IEstado {
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return "CANCELADO ";
	}

	public String iconName() {
		return "sector";
	}

	public Cancelado(Movimiento movimiento) {
		this.movimiento = movimiento;
	}

	// {{ Movimiento (property)
	private Movimiento movimiento;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Movimiento getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(final Movimiento movimiento) {
		this.movimiento = movimiento;
	}

	// }}
	@Override
	public void asignarTecnico() {
		this.getMovimiento().setEstadoActual("YA SE INICIALIZO.");
	}

	@Override
	public void esperarRepuestos() {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO ESPERANDO.");
	}

	@Override
	public void noHayRepuestos() {
		// TODO Auto-generated method stub
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO ESPERANDO");
	}

	@Override
	public void finalizarSoporte() {
		this.getMovimiento().setEstadoActual("FINALIZACION DEL SOPORTE");
		if (this.getMovimiento().getEstado().getClass().getSimpleName() == "Cancelado")
			this.getMovimiento().setEstadoActual("SOPORTE CANCELADO");

	}

	@Override
	public void llegaronRepuestos() {
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO CANCELADO");
	}
}