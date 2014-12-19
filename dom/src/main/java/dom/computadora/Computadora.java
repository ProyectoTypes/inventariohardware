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

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.impresora.Impresora.TipoImpresora;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.soporte.Soporte;
import dom.tecnico.Tecnico;
import dom.usuario.Usuario;

// TODO: Auto-generated Javadoc
/**
 * Clase Computadora.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Computadora_ip_must_be_unique", members = { "placaDeRed" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorComputadora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE placaDeRed.ip.indexOf(:ip) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora WHERE placaDeRed.ip.indexOf(:ip) >= 0") })
@ObjectType("COMPUTADORA")
@Audited
@AutoComplete(repository = ComputadoraRepositorio.class, action = "autoComplete")
public class Computadora implements Comparable<Computadora> {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Titutlo de la clase.
	 *
	 * @return the string
	 */
	public String title() {
		return this.getPlacaDeRed().getIp();
	}

	/**
	 * Nombre del icono.
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Computadora";
	}

	// //////////////////////////////////////
	// IP (propiedad)
	// //////////////////////////////////////
	/** Rotulo. */
	private String rotulo;
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	/** Placa de red. */
	private PlacaDeRed placaDeRed;

	@javax.jdo.annotations.Column(allowsNull = "false")
	// @javax.jdo.annotations.PrimaryKey(column = "id")
	@DescribedAs("Direccion IP de la Computadora:")
	@MemberOrder(sequence = "15")
	public PlacaDeRed getPlacaDeRed() {
		return placaDeRed;
	}

	public void setPlacaDeRed(PlacaDeRed placaDeRed) {
		this.placaDeRed = placaDeRed;
	}

	// //////////////////////////////////////
	// Mother (propiedad)
	// //////////////////////////////////////

	/** Motherboard. */
	private Motherboard motherboard;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Mother de la Computadora:")
	@MemberOrder(sequence = "20")
	public Motherboard getMotherboard() {
		return motherboard;
	}

	public void setMotherboard(final Motherboard motherboard) {
		this.motherboard = motherboard;
	}

	// //////////////////////////////////////
	// Procesador (propiedad)
	// //////////////////////////////////////

	/** Procesador. */
	private Procesador procesador;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Procesador de la Computadora:")
	@MemberOrder(sequence = "30")
	public Procesador getProcesador() {
		return procesador;
	}

	public void setProcesador(final Procesador procesador) {
		this.procesador = procesador;
	}

	// //////////////////////////////////////
	// Disco (propiedad)
	// //////////////////////////////////////

	/** Disco. */
	private Disco disco;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Disco de la Computadora:")
	@MemberOrder(sequence = "40")
	public Disco getDisco() {
		return disco;
	}

	public void setDisco(final Disco disco) {
		this.disco = disco;
	}

	// //////////////////////////////////////
	// Memoria (propiedad)
	// //////////////////////////////////////

	/** Memoria. */
	private MemoriaRam memoria;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Memoria de la Computadora:")
	@MemberOrder(sequence = "50")
	public MemoriaRam getMemoria() {
		return memoria;
	}

	public void setMemoria(final MemoriaRam memoria) {
		this.memoria = memoria;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	/** Habilitado. */
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
	 * @return habilitado en false.
	 */
	@Named("Eliminar Computadora")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "6")
	public List<Computadora> eliminar() {
		setHabilitado(false);
		container.flush();
		container.warnUser("Registro eliminado");

		return computadoraRepositorio.listar();
	}

	// //////////////////////////////////////
	// Impresora (propiedad)
	// //////////////////////////////////////

	/** Impresora. */
	private Impresora impresora;

	@MemberOrder(sequence = "50")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(final Impresora impresora) {
		this.impresora = impresora;
	}

	/**
	 * Listado de impresora.
	 *
	 * @return list
	 */
	public List<Impresora> choicesImpresora() {
		return this.impresoraRepositorio.listar();

	}

	/**
	 * Metodo que me permite modificar una Impresora precargada a una
	 * Computadora.
	 *
	 * @param impresora
	 */
	public void modifyImpresora(final Impresora impresora) {
		Impresora currentImpresora = getImpresora();
		if (impresora == null || impresora.equals(currentImpresora)) {
			return;
		}
		impresora.agregarComputadora(this);
		return;
	}

	/**
	 * Hide quitar impresora.
	 *
	 * @return true, if successful
	 */
	public boolean hideQuitarImpresora() {
		if (this.getImpresora() == null) {
			return true;
		}
		return false;
	}

	/**
	 * Quitar impresora el metodo comprueba que no halla impresora, de ser asi
	 * la setea en null.
	 *
	 * @return computadora
	 */
	@Named("Borrar Impresora")
	public Computadora quitarImpresora() {
		Impresora currentImpresora = getImpresora();
		if (currentImpresora == null) {
			return this;
		}
		currentImpresora.setComputadora(null);
		setImpresora(null);
		return this;
	}

	/**
	 * Metodo para quitar una Impresora a una Computadora.
	 */
	@Hidden
	public void limpiarImpresora() {
		Impresora impresora = getImpresora();
		if (impresora == null) {
			return;
		}
		impresora.limpiarComputadora(this);
	}

	/**
	 * Adds the impresora.
	 *
	 * @param modeloImpresora
	 *            the modelo impresora
	 * @param fabricanteImpresora
	 *            the fabricante impresora
	 * @param tipoImpresora
	 *            the tipo impresora
	 * @return the impresora
	 */
	@Named("Nueva Impresora")
	public Impresora addImpresora(
			final @Named("Modelo") String modeloImpresora,
			final @Named("Fabricante") String fabricanteImpresora,
			final @Named("Tipo") TipoImpresora tipoImpresora) {
		return impresoraRepositorio.nuevaImpresora(modeloImpresora,
				fabricanteImpresora, tipoImpresora, this.currentUserName());

	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////
	/**
	 * Current user name.
	 *
	 * @return the string
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	/** The creado por. */
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

	// Usuario (property)
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
	 * Validate usuario.
	 *
	 * @param usuario
	 * 
	 * @return the string
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
	 * Modify usuario.
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
	 * Clear usuario.
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

	// Tecnico (property)
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
	 * Modify tecnico.
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
	 * 
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
	 * Removes the from soporte.
	 *
	 * @param unSoporte
	 * 
	 */
	@Hidden
	@Named("Eliminar de Recepcion")
	public void removeFromSoporte(final Soporte unSoporte) {
		if (unSoporte == null || !getSoporte().contains(unSoporte)) {
			return;
		}
		unSoporte.setComputadora(null);
		getSoporte().remove(unSoporte);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Computadora computadora) {
		return ObjectContracts.compare(this, computadora, "codigo");
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	/** The impresora repositorio. */
	@Inject
	private ImpresoraRepositorio impresoraRepositorio;

	/** The computadora repositorio. */
	@Inject
	private ComputadoraRepositorio computadoraRepositorio;

	/** The container. */
	@Inject
	private DomainObjectContainer container;
}