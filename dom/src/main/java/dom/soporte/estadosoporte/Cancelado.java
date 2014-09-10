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

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.Computadora.CategoriaDisco;
import dom.impresora.Impresora;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

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
		return "Cancelado";
	}

	public Cancelado(Soporte soporte) {
		this.soporte = soporte;
	}

	// {{ Soporte (property)
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
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");
	}

	@Override
	public void solicitarInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones) {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void noHayInsumos(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void finalizarSoporte() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public void llegaronInsumos() {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");
	}

	@Override
	public void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
		this.container.informUser("EL SOPORTE HA SIDO CANCELADO.");

	}

	@Override
	public boolean escondeAsignarTecnico() {
		return true;
	}

	@Override
	public boolean escondeFinalizarSoporte() {
		return true;
	}

	@Override
	public boolean escondeSolicitarInsumos() {
		return true;
	}

	@Override
	public boolean escondeLlegaronInsumos() {
		return true;
	}

	@Override
	public boolean escondeNoHayInsumos() {
		return true;
	}

	@Override
	public boolean escondeAsignarNuevoEquipo() {
		return true;
	}

	@javax.inject.Inject
	private DomainObjectContainer container;

}
