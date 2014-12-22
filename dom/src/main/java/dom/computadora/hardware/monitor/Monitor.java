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
package dom.computadora.hardware.monitor;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

// TODO: Auto-generated Javadoc
/**
 * Clase Monitor.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Monitor_unique", members = { "tamaño","tipo","marca", }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorMonitor", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.monitor.Monitor "
				+ "WHERE creadoPor == :creadoPor && "
				+ "tamaño.indexOf(:tamaño) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarMonitorFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.monitor.Monitor "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "listarMonitorTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.monitor.Monitor " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorMarca", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.monitor.Monitor "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && marca.indexOf(:marca) >= 0"), })
@ObjectType("MONITOR")
@Audited
@AutoComplete(repository = MonitorRepositorio.class, action = "autoComplete")
public class Monitor {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Titulo de la Clase.
	 *
	 * @return the string
	 */
	public String title() {
		return this.getMarca();
	}

	/**
	 * Nombre del icono.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Monitor";
	}

	// //////////////////////////////////////
	// tipo (Atributo)
	// //////////////////////////////////////

	/**
	 * The Enum TipoMonitor.
	 */
	public static enum TipoMonitor {
	
		CRT,  LCD,  LED;
	}

	/** Tipo de monitor. */
	private TipoMonitor tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tipo de monitor:")
	@MemberOrder(sequence = "10")
	public TipoMonitor getTipo() {
		return tipo;
	}

	public void setTipo(final TipoMonitor tipo) {
		this.tipo = tipo;
	}

	// //////////////////////////////////////
	// tamaño (Atributo)
	// //////////////////////////////////////
	/** Tamaño de monitor. */
	private int tamanio;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre de monitor:")
	@MemberOrder(sequence = "20")
	public int getTamanio() {
		return tamanio;
	}

	public void setTamanio(final int tamanio) {
		this.tamanio = tamanio;
	}

	// //////////////////////////////////////
	// marca (Atributo)
	// //////////////////////////////////////
	/** Marca del monitor. */
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del monitor:")
	@MemberOrder(sequence = "30")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	/** The habilitado. */
	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "40")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	/** Creado por el usuario.... */
	private String creadoPor;

	@Hidden(where = Where.ALL_TABLES)
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(final String creadoPor) {
		this.creadoPor = creadoPor;
	}

	// //////////////////////////////////////
	// Eliminar
	// //////////////////////////////////////
	/**
	 * Método utilizado para deshabilitar un Insumo.
	 * 
	 * @return la propiedad habilitado en false.
	 */
	@Named("Eliminar")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "1")
	public List<Monitor> eliminar() {
		if (getEstaHabilitado() == true) {
			setHabilitado(false);
			container.isPersistent(this);
			container.warnUser("Eliminado " + container.titleOf(this));
		}
		return null;
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	/** The container. */
	@javax.inject.Inject
	private DomainObjectContainer container;
}