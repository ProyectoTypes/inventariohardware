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

import servicio.email.EmailService;
import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEsperando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "esperandoUnique", members = { "idEsperando" }) })
@ObjectType("ESPERANDO")
@Audited
@Bookmarkable
public class Esperando implements IEstado {
	public String title() {
		return "ESPERANDO";
	}

	public String iconName() {
		return "sector"; // cambiar todos los iconos!!!!!
	}
	public Esperando(Movimiento movimiento) {
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
		this.getMovimiento().setEstadoActual("NO ES EL ESTADO RECEPCIONADO");

	}

	@Override
	public void esperarRepuestos() {
		this.getMovimiento().setEstadoActual(
				"NO ESPERA: NO ES EL ESTADO REPARANDO.");
	}

	@Override
	public void finalizarSoporte() {
		this.getMovimiento().setEstadoActual(
				"NO FINALIZA: NO ES EL ESTADO REPARANDO.");
	}

	@Override
	public void noHayRepuestos() {
		this.getMovimiento()
				.setEstadoActual("CAMBIA AL NUEVO ESTADO CANCELADO");
		emailService.send(this.getMovimiento().getComputadora());
		this.getMovimiento().getTecnico().restaComputadora();
		this.getMovimiento().setEstado(this.getMovimiento().getCancelado());
	}

	@Override
	public void llegaronRepuestos() {
		this.getMovimiento().setEstadoActual(
				"CAMBIA AL NUEVO ESTADO ENTREGANDO");
		this.getMovimiento().setEstado(this.getMovimiento().getEntregando());
	}

	@javax.inject.Inject
	private EmailService emailService;
}