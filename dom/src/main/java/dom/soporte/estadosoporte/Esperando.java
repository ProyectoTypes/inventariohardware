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

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import servicio.email.EmailService;
import dom.computadora.Computadora.CategoriaDisco;
import dom.computadora.ComputadoraRepositorio;
import dom.impresora.Impresora;
import dom.insumo.Insumo;
import dom.insumo.InsumoRepositorio;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idEsperando")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "esperandoUnique", members = { "idEsperando" }) })
@ObjectType("ESPERANDO")
public class Esperando implements IEstado {
	public String title() {
		return "ESPERANDO";
	}

	public String iconName() {
		return "Esperando";
	}

	public Esperando(Soporte soporte) {
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
	@Hidden
	public void asignarTecnico(final Tecnico tecnico) {
		if (tecnico.estaDisponible()) {
		this.getSoporte().setTecnico(tecnico);
		this.getSoporte().getTecnico().sumaComputadora();
		this.getSoporte().getTecnico()
				.addToComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getReparando());
	} else {
		this.getSoporte().setTecnico(null);
		this.container
				.informUser("El Tecnico seleccionado no esta disponible.");
	}

	}

	/**
	 * Se podra realizar un nuevo pedido de insumos.
	 * <p>
	 * No hay Cambio de Estado
	 * </p>
	 * 
	 * @param codigo
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param observaciones
	 */
	@Override
	@Hidden
	public void solicitarInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones) {
		Insumo unInsumo = this.insumoRepositorio.addInsumo(codigo, cantidad,
				producto, marca, observaciones);
		this.getSoporte().agregarUnInsumo(unInsumo);
		this.container.informUser("SOLICITANDO NUEVOS INSUMOS");

	}

	@Override
	@Hidden
	public void finalizarSoporte() {
		this.container.informUser("EL EQUIPO NO SE TERMINO DE REPARAR");

	}

	/**
	 * Si los insumos por alguna razon no estan disponibles, el Usuario será
	 * informado via email. Además, el Tecnico será desvinculado de la
	 * Computadora, y estará libre para recibir una nueva.
	 * <p>
	 * Cambio de Estado: Esperando -> Cancelado
	 * </p>
	 */
	@Override
	@Hidden
	public void noHayInsumos(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {
			// Enviando email
			emailService.send(this.getSoporte().getComputadora());

			// Creando nueva Computadora.
			this.computadoraRepositorio.addComputadora(this.getSoporte()
					.getComputadora().getUsuario(), ip, mother, procesador,
					disco, memoria, impresora);

			// Desvinculando
			this.getSoporte().getComputadora().getUsuario().clearComputadora();
			this.getSoporte().getTecnico()
					.removeFromComputadora(this.getSoporte().getComputadora());
			this.getSoporte().getComputadora().limpiarImpresora();
			
			this.getSoporte().getComputadora().setHabilitado(false);
			
			this.getSoporte().setEstado(this.getSoporte().getCancelado());

			this.container
					.informUser("EL EQUIPO NO PUEDE SER REPARADO POR FALTA DE REPUESTOS.");
	}

	/**
	 * El equipo ha sido reparado exitosamente con los repuestos que se habian
	 * solicitado. Al Tecnico se le desvinculará la Computadora.
	 * <p>
	 * Automaticamente el Usuario sera informado via email.
	 * </p>
	 * <p>
	 * Cambio de Estados: Esperando -> Entregando
	 * </p>
	 */
	@Override
	@Hidden
	public void llegaronInsumos() {
		// Enviando email.
		emailService.send(this.getSoporte().getComputadora());
		this.container
		.informUser("paso el envio de email.");
		// Desvinculando tecnico/computadora.
		this.getSoporte().getTecnico()
				.removeFromComputadora(this.getSoporte().getComputadora());
		this.getSoporte().setEstado(this.getSoporte().getEntregando());
		this.container
				.informUser("EL EQUIPO FUE ENSAMBLADO Y ESTA LISTO PARA SER ENTREGADO.");
	}

	@Override
	@Hidden
	public void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			final String memoria, final Impresora impresora) {

	}

	@Override
	@Hidden
	public boolean escondeAsignarTecnico() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeFinalizarSoporte() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeSolicitarInsumos() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeLlegaronInsumos() {
		return true;
	}

	@Override
	@Hidden
	public boolean escondeNoHayInsumos() {
		return false;
	}

	@Override
	@Hidden
	public boolean escondeAsignarNuevoEquipo() {
		return true;
	}

	@Inject
	private EmailService emailService;
	@Inject
	private DomainObjectContainer container;
	@Inject
	private InsumoRepositorio insumoRepositorio;
	@Inject
	private ComputadoraRepositorio computadoraRepositorio;

}
