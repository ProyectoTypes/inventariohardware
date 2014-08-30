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
package dom.soporte.estadoSoporte;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.soporte.Soporte;

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

	public Cancelado(Soporte movimiento) {
		this.movimiento = movimiento;
	}

	// {{ Movimiento (property)
	private Soporte movimiento;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(final Soporte movimiento) {
		this.movimiento = movimiento;
	}

	// }}
	@Override
	public void asignarTecnico() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");
	}

	@Override
	public void solicitarInsumos() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void noHayRepuestos() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void finalizarSoporte() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void llegaronRepuestos() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");
	}

	@javax.inject.Inject
	private DomainObjectContainer container;

	@Override
	public void asignarEquipo() {
		// TODO Auto-generated method stub
		
	}
}
