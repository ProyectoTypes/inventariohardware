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

import servicio.email.EmailService;
import dom.insumo.Insumo;
import dom.insumo.InsumoRepositorio;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

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

	public Esperando(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Movimiento (property)
	private Soporte soporte;

	@MemberOrder(sequence = "1")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	@SuppressWarnings("unused")
	private void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	// }}
	@Override
	public void asignarTecnico(final Tecnico tecnico) {
		this.container.informUser("EL TECNICO YA HA SIDO ASIGNADO.");

	}

	@Override
	public void solicitarInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones) {
		Insumo unInsumo = this.insumoRepositorio.addInsumo(codigo, cantidad,
				producto, marca, observaciones);
		this.getSoporte().agregarUnInsumo(unInsumo);
		this.container.informUser("SOLICITANDO NUEVOS INSUMOS");

	}

	@Override
	public void finalizarSoporte() {
		this.container.informUser("EL EQUIPO NO SE TERMINO DE REPARAR");

	}

	@Override
	public void noHayInsumos() {
		this.container
				.informUser("EL EQUIPO NO PUEDE SER REPARADO POR FALTA DE REPUESTOS.");

		emailService.send(this.getSoporte().getComputadora());
		this.getSoporte().getComputadora().setHabilitado(false);
		this.getSoporte().getTecnico()
				.desvincularComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getCancelado());
	}

	@Override
	public void llegaronInsumos() {
		this.container
				.informUser("EL EQUIPO FUE ENSAMBLADO Y ESTA LISTO PARA SER ENTREGADO.");
		emailService.send(this.getSoporte().getComputadora());
		this.getSoporte().getTecnico()
				.desvincularComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getEntregando());
	}

	@javax.inject.Inject
	private EmailService emailService;
	@javax.inject.Inject
	private DomainObjectContainer container;
	@javax.inject.Inject
	private InsumoRepositorio insumoRepositorio;

	@Override
	public void asignarEquipo() {
		// TODO Auto-generated method stub

	}
}
