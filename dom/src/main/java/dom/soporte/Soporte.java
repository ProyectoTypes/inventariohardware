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
package dom.soporte;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.CssClass;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotContributed.As;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import dom.computadora.Computadora;
import dom.computadora.Computadora.CategoriaDisco;
import dom.computadora.ComputadoraRepositorio;
import dom.impresora.Impresora;
import dom.impresora.ImpresoraRepositorio;
import dom.insumo.Insumo;
import dom.soporte.estadosoporte.Cancelado;
import dom.soporte.estadosoporte.Entregando;
import dom.soporte.estadosoporte.Esperando;
import dom.soporte.estadosoporte.IEstado;
import dom.soporte.estadosoporte.Recepcionado;
import dom.soporte.estadosoporte.Reparando;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Soporte_unique", members = { "fecha,creadoPor,observaciones" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorSoporte", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE creadoPor == :creadoPor && "
				+ "tecnico.getNombre().indexOf(:buscarTecnico) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && computadora.getIp().indexOf(:ip) >= 0"), })
@ObjectType("SOPORTE")
@Audited
@AutoComplete(repository = SoporteRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Soporte implements Comparable<Soporte> {

	/**********************************************
	 * CONSTRUCTOR: Utilizado para el patron State.
	 **********************************************/

	public Soporte() {
		this.recepcionado = new Recepcionado(this);
		this.reparando = new Reparando(this);
		this.entregando = new Entregando(this);
		this.esperando = new Esperando(this);
		this.cancelado = new Cancelado(this);

		this.estado = this.recepcionado;
	}

	// ////////////////////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu.
	// ////////////////////////////////////////////////////

	public String title() {
		return "SOPORTE TECNICO";
	}

	public String iconName() {
		return "Soporte";
	}

	// //////////////////////////////////////
	// Obeservaciones (propiedad)
	// //////////////////////////////////////

	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones de la Computadora:")
	@MemberOrder(name = "Detalles", sequence = "20")
	@MultiLine(numberOfLines = 15)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(final String observaciones) {
		this.observaciones = observaciones;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	private boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "40")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	// //////////////////////////////////////
	// fecha (propiedad)
	// //////////////////////////////////////

	private LocalDate fecha;

	@Disabled
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(name = "Datos Generales", sequence = "10")
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	private LocalDateTime time_system;

	@Disabled
	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "20")
	public LocalDateTime getTime_system() {
		return time_system;
	}

	public void setTime_system(LocalDateTime time_system) {
		this.time_system = time_system;
	}

	// //////////////////////////////////////
	// creadoPor (propiedad)
	// //////////////////////////////////////

	private String creadoPor;

	@Hidden
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	/********************************************************
	 * Relacion Tecnico/Soporte.
	 ********************************************************/

	private Tecnico tecnico;

	@Optional
	@MemberOrder(name = "Datos Generales", sequence = "11")
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Disabled
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	/**********************************************************************
	 * Relacion Soporte(Parent)/Insumos(Child).
	 **********************************************************************/

	@Persistent(mappedBy = "soporte", dependentElement = "False")
	@Join
	private List<Insumo> insumos = new ArrayList<Insumo>();

	@MemberOrder(name = "Insumos Solicitados", sequence = "31")
	public List<Insumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(final List<Insumo> insumos) {
		this.insumos = insumos;
	}

	@Programmatic
	public void agregarUnInsumo(final Insumo insumo) {
		if (insumo == null || getInsumos().contains(insumo)) {
			return;
		}
		// insumo.limpiarSoporte();
		insumo.setSoporte(this);
		getInsumos().add(insumo);
	}

	@Programmatic
	public void eliminarInsumos(final Insumo insumo) {
		if (insumo == null || !getInsumos().contains(insumo)) {
			return;
		}
		insumo.setSoporte(null);
		getInsumos().remove(insumo);
	}

	/********************************************************
	 * Relacion Computadora/Soporte.
	 ********************************************************/

	private Computadora computadora;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@MemberOrder(name = "Insumos Solicitados", sequence = "30")
	public Computadora getComputadora() {
		return computadora;
	}

	public void setComputadora(Computadora computadora) {
		this.computadora = computadora;
	}

	@Programmatic
	public void modificarComputadora(final Computadora unaComputadora) {
		Computadora currentComputadora = getComputadora();
		if (unaComputadora == null || unaComputadora.equals(currentComputadora)) {
			return;
		}
		setComputadora(unaComputadora);
	}

	@Programmatic
	public void clearComputadora() {
		Computadora currentComputadora = getComputadora();
		if (currentComputadora == null) {
			return;
		}
		this.getComputadora().setHabilitado(false);
		this.setComputadora(null);
	}

	@Programmatic
	public List<Computadora> autoComplete0ModificarComputadora(
			final String search) {
		return this.computadoraRepositorio.autoComplete(search);
	}

	// //////////////////////////////////////
	// CompareTo
	// //////////////////////////////////////

	@Override
	public int compareTo(final Soporte soporte) {
		return ObjectContracts.compare(this, soporte, "time_system");
	}

	/**********************************************************
	 * PATRON STATE
	 **********************************************************/
	private IEstado estado;

	@Persistent(extensions = {
			@Extension(vendorName = "datanucleus", key = "mapping-strategy", value = "per-implementation"),
			@Extension(vendorName = "datanucleus", key = "implementation-classes", value = "dom.soporte.estadosoporte.Recepcionado"
					+ ",dom.soporte.estadosoporte.Reparando"
					+ ",dom.soporte.estadosoporte.Esperando"
					+ ",dom.soporte.estadosoporte.Cancelado"
					+ ",dom.soporte.estadosoporte.Entregando") }, columns = {
			@javax.jdo.annotations.Column(name = "idrecepcionado"),
			@javax.jdo.annotations.Column(name = "idreparando"),
			@javax.jdo.annotations.Column(name = "idesperando"),
			@javax.jdo.annotations.Column(name = "idcancelado"),
			@javax.jdo.annotations.Column(name = "identregando") })
	@Optional
	@Hidden(where = Where.PARENTED_TABLES)
	@Disabled
	@MemberOrder(name = "Datos Generales", sequence = "12")
	public IEstado getEstado() {
		return estado;
	}

	public void setEstado(IEstado estado) {
		this.estado = estado;
	}

	/* *************************************************** */

	private Recepcionado recepcionado;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Recepcionado getRecepcionado() {
		return this.recepcionado;
	}

	public void setRecepcionado(Recepcionado recepcionado) {
		this.recepcionado = recepcionado;
	}

	/* *************************************************** */

	private Reparando reparando;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Reparando getReparando() {
		return reparando;
	}

	public void setReparando(Reparando reparando) {
		this.reparando = reparando;
	}

	/* *************************************************** */

	private Cancelado cancelado;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Cancelado getCancelado() {
		return this.cancelado;
	}

	public void setCancelado(Cancelado cancelado) {
		this.cancelado = cancelado;
	}

	/* *************************************************** */

	private Entregando entregando;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Entregando getEntregando() {
		return entregando;
	}

	public void setEntregando(final Entregando entregando) {
		this.entregando = entregando;
	}

	/* *************************************************** */

	private Esperando esperando;

	@Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Esperando getEsperando() {
		return esperando;
	}

	public void setEsperando(final Esperando esperando) {
		this.esperando = esperando;
	}

	/* ***************************************************
	 * FIN: Atributos del State.
	 * ***************************************************
	 */

	/* ***************************************************
	 * Operaciones del State.
	 * ***************************************************
	 */
	@Named("Asignar Tecnico")
	@DescribedAs("Seleccionar un Tecnico para comenzar el Soporte.")
	@NotContributed(As.ASSOCIATION)
	@CssClass("x-highlight")
	public Soporte asignarTecnico(final Tecnico tecnico) {
		this.getEstado().asignarTecnico(tecnico);
		return this;
	}

	public List<Tecnico> choices0AsignarTecnico() {
		return this.tecnicoRepositorio.listar();
	}

	/* ************************ */

	@Named("Solicitar Insumos")
	@DescribedAs("Realizar nuevo pedido de Insumos.")
	public Soporte solicitarInsumos(final @Named("Codigo") String codigo,
			final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Optional @Named("Observaciones") String observaciones) {
		this.getEstado().solicitarInsumos(codigo, cantidad, producto, marca,
				observaciones);
		return this;
	}

	/**
	 * En este metodo se observa que el soporte este en estado reparando o en
	 * esperando para ocultar o mostrar el metodo solicitar insumos.
	 * 
	 * @return boolean
	 */
	public boolean hideSolicitarInsumos() {
		return false;
	}

	/* ************************ */

	/**
	 * Reparando -> Entregando. Finalizar el Soporte Tecnico de la computadora.
	 * A partir de aca no puede realizar ninguna accion de soporte sobre la
	 * computadora.
	 * 
	 * @return
	 */
	@Named("Finalizar Soporte")
	@DescribedAs("Soporte finalizado con exito. Enviar email.")
	public Soporte finalizarSoporte() {
		this.getEstado().finalizarSoporte();
		return this;

	}

	public boolean hideFinalizarSoporte() {
		return false;
	}

	/**
	 * Esperando -> Cancelado. No es posible conseguir repuestos para la
	 * computadora.
	 * 
	 * @return
	 */
	/* ************************ */

	@Named("No hay Insumos")
	@DescribedAs("No hay Repuestos disponibles para finalizar el Soporte.")
	public Soporte noHayInsumos(final @Named("Direccion Ip") String ip,
			final @Named("Mother") String mother,
			final @Named("Procesador") String procesador,
			final @Named("Disco") CategoriaDisco disco,
			final @Named("Memoria") String memoria,
			final @Optional @Named("Impresora") Impresora impresora) {
		this.getEstado().noHayInsumos(ip, mother, procesador, disco, memoria,
				impresora);
		return this;

	}

	public boolean hideNoHayInsumos() {
		return false;// this.getEstado().esconde();
	}

	/* ************************ */

	/**
	 * Esperando -> Entregado. Llegaron los repuestos, se ensamblo la maquina y
	 * se finalizo la reparacion.
	 * 
	 * @return
	 */
	@Named("Ensamblar nuevos Insumos")
	@DescribedAs("El equipo es reparado con los respuestos solicitados.")
	public Soporte llegaronRepuestos() {
		this.getEstado().llegaronInsumos();
		return this;
	}

	public boolean hideLlegaronRepuestos() {
		return false;
	}

	/* ************************ */
	@DescribedAs("Ingresando una nueva Computadora al Usuario.")
	public Soporte asignarNuevoEquipo(final @Named("Direccion Ip") String ip,
			final @Named("Mother") String mother,
			final @Named("Procesador") String procesador,
			final @Named("Disco") CategoriaDisco disco,
			final @Named("Memoria") String memoria,
			final @Optional @Named("Impresora") Impresora impresora) {
		this.getEstado().asignarNuevoEquipo(ip, mother, procesador, disco,
				memoria, impresora);
		return this;
	}

	public List<Impresora> autoComplete5AsignarNuevoEquipo(
			final @MinLength(2) String search) {
		return this.impresoraRepositorio.autoComplete(search);
	}

	public boolean hideAsignarNuevoEquipo() {
		return false;
	}

	/* ***************************************************
	 * FIN: Operaciones del State.
	 * ***************************************************
	 */

	// ////////////////////////////////////
	// Injected Services
	// ////////////////////////////////////

	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;

	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;
}