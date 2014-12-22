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
package dom.computadora;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.util.ObjectContracts;

import dom.computadora.hardware.Hardware;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.computadora.software.Software;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;
import dom.usuario.Usuario;

/**
 * Clase Computadora.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.APPLICATION)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Computadora_ip_must_be_unique", members = { "ip" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorComputadora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE ip.indexOf(:ip) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora WHERE ip.indexOf(:ip) >= 0") })
@ObjectType("COMPUTADORA")
@Audited
@AutoComplete(repository = ComputadoraRepositorio.class, action = "autoComplete")
public class Computadora implements Comparable<Computadora> {

	/**
	 * Título de la clase.
	 * 
	 * @return the string
	 */
	public String title() {
		return getIp();
	}

	/**
	 * Nombre del Icono.
	 * 
	 * @return the string
	 */
	public String iconName() {
		return "Computadora";
	}

	// //////////////////////////////////////
	// Nombre Equipo (propiedad)
	// //////////////////////////////////////

	private String nombreEquipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public String getNombreEquipo() {
		return nombreEquipo;
	}

	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	// //////////////////////////////////////
	// IP (propiedad)
	// //////////////////////////////////////

	@PrimaryKey
	private String ip;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@javax.jdo.annotations.PrimaryKey(column = "id")
	@DescribedAs("Direccion IP de la Computadora:")
	@MemberOrder(sequence = "10")
	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	// //////////////////////////////////////
	// Software (propiedad)
	// //////////////////////////////////////

	private Software software;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@DescribedAs("Sistema Operativo")
	@MemberOrder(sequence = "50")
	public Software getSoftware() {
		return software;
	}

	public void setSoftware(final Software software) {
		this.software = software;
	}

	// //////////////////////////////////////
	// Hardware (propiedad)
	// //////////////////////////////////////

	private Hardware hardware;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Memoria de la Computadora:")
	@MemberOrder(sequence = "50")
	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(final Hardware hardware) {
		this.hardware = hardware;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "40")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	/**
	 * Método que se usa para deshabilitar un Tecnico.
	 * 
	 * @return
	 */
	@Named("Eliminar Computadora")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "6")
	public List<Computadora> eliminar() {
		setHabilitado(false);
		container.flush();
		container.warnUser("Registro eliminado");

		return computadoraRepositorio.listAll();
	}

	// //////////////////////////////////////
	// Impresora (propiedad)
	// //////////////////////////////////////

	// private Impresora impresora;
	//
	// @MemberOrder(sequence = "50")
	// @javax.jdo.annotations.Column(allowsNull = "true")
	// public Impresora getImpresora() {
	// return impresora;
	// }
	//
	// public void setImpresora(final Impresora impresora) {
	// this.impresora = impresora;
	// }
	//
	// /**
	// * Método que permite listar las Impresoras.
	// * @return
	// */
	// public List<Impresora> choicesImpresora() {
	// return this.impresoraRepositorio.listAll();
	//
	// }

	// /**
	// * Método que me permite modificar una Impresora.
	// * @param impresora
	// */
	// public void modifyImpresora(final Impresora impresora) {
	// Impresora currentImpresora = getImpresora();
	// if (impresora == null || impresora.equals(currentImpresora)) {
	// return;
	// }
	// impresora.agregarComputadora(this);
	// return;
	// }
	//
	// /**
	// * Quitar impresora.
	// * @retun
	// */
	// public boolean hideQuitarImpresora() {
	// if (this.getImpresora() == null) {
	// return true;
	// }
	// return false;
	// }

	// /**
	// * Quitar impresora.
	// * @return computadora
	// */
	// @Named("Borrar Impresora")
	// public Computadora quitarImpresora() {
	// Impresora currentImpresora = getImpresora();
	// if (currentImpresora == null) {
	// return this;
	// }
	// currentImpresora.setComputadora(null);
	// setImpresora(null);
	// return this;
	// }

	// /**
	// * Método para quitar una Impresora.
	// */
	// @Hidden
	// public void limpiarImpresora() {
	// Impresora impresora = getImpresora();
	// if (impresora == null) {
	// return;
	// }
	// impresora.limpiarComputadora(this);
	// }
	//
	// /**
	// * Nueva Impresora.
	// * @param modeloImpresora
	// * @param fabricanteImpresora
	// * @param tipoImpresora
	// * @return
	// */
	// @Named("Nueva Impresora")
	// public Impresora addImpresora(
	// final @Named("Modelo") String modeloImpresora,
	// final @Named("Fabricante") String fabricanteImpresora,
	// final @Named("Tipo") TipoImpresora tipoImpresora) {
	// return impresoraRepositorio.nuevaImpresora(modeloImpresora,
	// fabricanteImpresora, tipoImpresora, this.currentUserName());
	// }

	/**
	 * Devuelve el Usuario logueado.
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	private String creadoPor;

	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(final String creadoPor) {
		this.creadoPor = creadoPor;
	}

	/**
	 * Relacion Computadora/Usuario.
	 */
	private Usuario usuario;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Validar los datos de Usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	public String validateUsuario(final Usuario usuario) {
		if (usuario.getComputadora() == null || this.getUsuario() == usuario)
			return null;
		else
			return "El Usuario ya tiene asignado una Computadora. Seleccione otro.";
	}

	// ///////////////////////////////////////////////////
	// Operaciones de USUARIO: Agregar/Borrar
	// ///////////////////////////////////////////////////

	/**
	 * Modificar Usuario.
	 * 
	 * @param user
	 */
	@Named("Modificar Usuario")
	public void modifyUsuario(final Usuario user) {
		Usuario usuario = getUsuario();
		if (user == null || user.equals(usuario)) {
			return;
		}
		this.clearUsuario();
		user.setComputadora(this);
		this.setUsuario(user);
	}

	/**
	 * Borrar Usuario.
	 */
	@Named("Borrar Usuario")
	public void clearUsuario() {
		Usuario usuario = getUsuario();
		if (usuario == null) {
			return;
		}
		usuario.setComputadora(null);
		this.setUsuario(null);
	}

	/**
	 * Relacion Computadora/Tecnico.
	 */
	private Tecnico tecnico;

	@Disabled
	@Named("Tecnico Asignado")
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "True")
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	// ///////////////////////////////////////////////////
	// Operaciones de Tecnico: Agregar/Borrar
	// ///////////////////////////////////////////////////

	/**
	 * Modificar Técnico.
	 * 
	 * @param unTecnico
	 */
	public void modifyTecnico(final Tecnico unTecnico) {
		Tecnico currentTecnico = getTecnico();
		if (unTecnico == null || unTecnico.equals(currentTecnico)) {
			return;
		}
		unTecnico.addToComputadora(this);
	}

	/**
	 * Clear tecnico.
	 */
	public void clearTecnico() {
		Tecnico currentTecnico = getTecnico();
		if (currentTecnico == null) {
			return;
		}
		currentTecnico.removeFromComputadora(this);
	}

	/**************************************************************
	 * Relacion Computadora(Parent)/Soporte(Child).
	 *************************************************************/

	@Persistent(mappedBy = "computadora", dependentElement = "False")
	@Join
	private List<Soporte> soporte;

	public List<Soporte> getSoporte() {
		return soporte;
	}

	public void setSoporte(List<Soporte> soportes) {
		this.soporte = soportes;
	}

	/**
	 * Agregar Soporte
	 * 
	 * @param unSoporte
	 */
	@Hidden
	@Named("Agregar Soporte")
	public void addToSoporte(final Soporte unSoporte) {
		if (unSoporte == null || getSoporte().contains(unSoporte)) {
			return;
		}
		unSoporte.clearComputadora();
		unSoporte.setComputadora(this);
		getSoporte().add(unSoporte);
	}

	/**
	 * Eliminar Recepción.
	 * 
	 * @param unSoporte
	 */
	@Hidden
	@Named("Eliminar de Recepción")
	public void removeFromSoporte(final Soporte unSoporte) {
		if (unSoporte == null || !getSoporte().contains(unSoporte)) {
			return;
		}
		unSoporte.setComputadora(null);
		getSoporte().remove(unSoporte);
	}

	/**
	 * CompareTo
	 * 
	 * @param computadora
	 * @return
	 */
	@Override
	public int compareTo(Computadora computadora) {
		return ObjectContracts.compare(this, computadora, "ip");
	}


	/**
	 * Inyección del Contenedor.
	 */
	@Inject
	private DomainObjectContainer container;

	/**
	 * Inyección del servicio para Impresora.
	 */
	@SuppressWarnings("unused")
	@Inject
	private ImpresoraRepositorio impresoraRepositorio;

	/**
	 * Inyección del servicio para Computadora.
	 */
	@Inject
	private ComputadoraRepositorio computadoraRepositorio;
}