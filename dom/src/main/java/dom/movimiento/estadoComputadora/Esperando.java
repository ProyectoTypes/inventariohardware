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

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;

import dom.movimiento.Movimiento;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEsperando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "esperandoUnique", members = { "idEsperando" }) })
@ObjectType("ESPERANDO")
@Audited
@Bookmarkable
public class Esperando implements IEstado{
	public String title() {
		return "ESPERANDO";
	}

	public String iconName() {
		return "sector";
	}

	@Override
	public IEstado asignarTecnico(Movimiento unM) {
		unM.setEstadoActual("NO ES EL ESTADO RECEPCIONADO");
		return this;

	}

	@Override
	public IEstado esperarRepuestos(Movimiento unM) {
		unM.setEstadoActual("NO ESPERA: NO ES EL ESTADO REPARANDO.");
		return this;
	}

	@Override
	public IEstado finalizarSoporte(Movimiento unM) {
		unM.setEstadoActual("NO FINALIZA: NO ES EL ESTADO REPARANDO.");
		return this;
	}
	@Override
	public IEstado noHayRepuestos(Movimiento unM) {
		// TODO Auto-generated method stub
		unM.setEstadoActual("CAMBIA AL NUEVO ESTADO CANCELADO");
		return new Cancelado();
	}


	@Override
	public IEstado llegaronRepuestos(Movimiento unM) {
		unM.setEstadoActual("CAMBIA AL NUEVO ESTADO ENTREGANDO");
		return new Entregado();
	}
}
