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
import org.apache.isis.applib.annotation.CssClass;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
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
import dom.computadora.ComputadoraRepositorio;
import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.computadora.hardware.monitor.Monitor;
import dom.computadora.hardware.monitor.MonitorRepositorio;
import dom.insumo.Insumo;
import dom.soporte.estadosoporte.Cancelado;
import dom.soporte.estadosoporte.Entregando;
import dom.soporte.estadosoporte.Esperando;
import dom.soporte.estadosoporte.IEstado;
import dom.soporte.estadosoporte.Recepcionado;
import dom.soporte.estadosoporte.Reparando;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

/**
 * Clase Soporte.
 * @author ProyectoTypes
 * @since 17/05/2014
 * @version 1.0.0
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Soporte_unique", members = { "fecha,creadoPor,observaciones" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorSoporte", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE  "
				+ "tecnico.getNombre().indexOf(:buscarTecnico) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE computadora.placaDeRed.ip.equals(:ip) "),
		@javax.jdo.annotations.Query(name = "buscarSoportesEnEspera", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte " + "WHERE estado == esperando "),

		@javax.jdo.annotations.Query(name = "seEncuentraEnReparacion", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte "
				+ "WHERE computadora.placaDeRed.ip.equals(:ip) && (estado == reparando || estado == recepcionado || estado == esperando)  "),
		@javax.jdo.annotations.Query(name = "buscarSoportesEnReparacion", language = "JDOQL", value = "SELECT "
				+ "FROM dom.soporte.Soporte " + "WHERE estado == reparando ") })
@ObjectType("SOPORTE")
@Audited
@AutoComplete(repository = SoporteRepositorio.class, action = "autoComplete")
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
		return "SOPORTE "+this.getComputadora().getPlacaDeRed().getIp();
	}

	public String iconName() {
		return "Soporte";
	}

	// //////////////////////////////////////
	// Obeservaciones (propiedad)
	// //////////////////////////////////////

	/** Observaciones. */
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

	/** Habilitado. */
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

	/** Fecha. */
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

	/** Time_system. */
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

	/** Creado por. */
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

	/**
	 * Modify tecnico se ocupa de la modificacion de un tecnico en el soporte ya
	 * que varios tecnicos pueden relizar un mismo soporte.
	 *
	 * @param tecnico
	 * 
	 */
	public void modifyTecnico(final Tecnico tecnico) {
		Tecnico currentTecnico = getTecnico();
		if (tecnico == null || tecnico.equals(currentTecnico)) {
			return;
		}

		this.clearTecnico();
		tecnico.setSoporte(this);
		setTecnico(tecnico);
	}

	/**
	 * Clear tecnico limapiamos el tecnico que tiene el soporte dejandolo nulo,
	 * a la espera de un proximo tecnico
	 */
	public void clearTecnico() {
		Tecnico tecnico = this.getTecnico();
		if (tecnico == null) {
			return;
		}
		tecnico.setSoporte(null);
		this.setTecnico(null);
		// additional business logic
		tecnico.removeFromComputadora(this.getComputadora());
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

	/**
	 * Agregar un insumo, persiste en la bese de datos los datos que fueron
	 * cargados en el ABM.
	 *
	 * @param insumo
	 * 
	 */
	@Programmatic
	public void agregarUnInsumo(final Insumo insumo) {
		if (insumo == null || getInsumos().contains(insumo)) {
			return;
		}
		// insumo.limpiarSoporte();
		insumo.setSoporte(this);
		getInsumos().add(insumo);
	}

	/**
	 * Eliminar insumos, elimina los insumos si fuese que estos hubiesen sido
	 * mal cargados.
	 *
	 * @param insumo
	 * 
	 */
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

	/**
	 * Modificar computadora, utilizado para poder cambiar la computadora que
	 * tiene asignada el usuario por otra.
	 *
	 * @param unaComputadora
	 * 
	 */
	@Programmatic
	public void modificarComputadora(final Computadora unaComputadora) {
		Computadora currentComputadora = getComputadora();
		if (unaComputadora == null || unaComputadora.equals(currentComputadora)) {
			return;
		}
		setComputadora(unaComputadora);
	}

	/**
	 * Clear computadora, este metodo deshabilita la computadora para que ya no
	 * pueda ser asignada a ningun otra usuario, queda en desuso.
	 */
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

	/**
	 * ******************************************************** PATRON STATE
	 * ********************************************************.
	 */
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

	/** Recepcionado. */
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

	/** Reparando. */
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

	/** Cancelado. */
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

	/** Entregando. */
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

	/** Esperando. */
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
	/**
	 * Asignar tecnico, metodo usado para elegir el tecnico que va a realizar el
	 * soporte.
	 *
	 * @param tecnico
	 * 
	 * @return soporte
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
		return this.tecnicoRepositorio.listarDisponibles();
	}

	/**
	 * Hide asignar tecnico, metodo que esconde el boton de asignacion tecnico
	 * dependiendo del estado en el que este el soporte.
	 *
	 * @return true, if successful
	 */
	public boolean hideAsignarTecnico() {
		return this.getEstado().escondeAsignarTecnico();
	}

	/* ************************ */

	/**
	 * Solicitar insumos, metodo utilizado para la carga de insumos que pueden
	 * llegar a necesitarse si algun elemnto esta dañado.
	 *
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param modelo
	 * 
	 * @return soporte
	 */
	@Named("Solicitar Insumos")
	@DescribedAs("Realizar nuevo pedido de Insumos.")
	public Soporte solicitarInsumos(final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Named("Modelo") String modelo) {
		this.getEstado().solicitarInsumos(cantidad, producto, marca, modelo);
		return this;
	}

	/**
	 * En este metodo se observa que el soporte este en estado esperando o
	 * reparando para ocultar o mostrar el metodo solicitar insumos.
	 * 
	 * @return boolean
	 */
	public boolean hideSolicitarInsumos() {
		return this.getEstado().escondeSolicitarInsumos();
	}

	/* ************************ */

	/**
	 * Reparando -> Entregando. Finalizar el Soporte Tecnico de la computadora.
	 * A partir de aca no puede realizar ninguna accion de soporte sobre la
	 * computadora.
	 *
	 * @return the soporte
	 */
	@Named("Finalizar Soporte")
	@DescribedAs("Soporte finalizado con exito. Enviar email.")
	public Soporte finalizarSoporte() {
		this.getEstado().finalizarSoporte();
		return this;

	}

	/**
	 * hideFinalizar soporte, esconde el boton de finalizacion de soporte, solo
	 * se muetsra despues del estado reparando o esperando.
	 *
	 * @return true, if successful
	 */
	public boolean hideFinalizarSoporte() {
		return this.getEstado().escondeFinalizarSoporte();
	}

	/* ************************ */

	/**
	 * Asignar nuevo equipo, en caso de que el equipo este obsoleto o no tenga
	 * arrego se asigna un nuevo equipo cargando todos los datos de la nueva
	 * computadora.
	 *
	 * @param ip
	 * @param mac
	 * @param marcaDisco
	 * @param tipoDisco
	 * @param tamanoDisco
	 * @param modeloProcesador
	 * @param modeloRam
	 * @param tamanoRam
	 * @param marcaRam
	 * @param modeloMotherboard
	 * @param fabricante
	 * @param monitor
	 * @param impresora
	 * @param rotulo
	 * 
	 * @return soporte
	 */
	@DescribedAs("Ingresando una nueva Computadora al Usuario.")
	public Soporte asignarNuevoEquipo(final @Named("Nombre de Equipo") String rotulo,
			final @Named("IP") String ip,
			final @Named("MAC") String mac,
			final @Named("HDD Marca ") String marcaDisco,
			final @Named("HDD Categoria ") CategoriaDisco tipoDisco,
			final @Named("HDD Tamaño ") int tamanoDisco,
			final @Named("CPU Modelo ") String modeloProcesador,
			final @Named("RAM Modelo") String modeloRam,
			final @Named("RAM Tamaño") int tamanoRam,
			final @Named("RAM Marca") String marcaRam,
			final @Named("Modelo Motherboard") String modeloMotherboard,
			final @Named("Fabricante") String fabricante,
			final @Optional @Named("Monitor") Monitor monitor,
			final @Optional @Named("Impresora") Impresora impresora) {
		this.getEstado().asignarNuevoEquipo(rotulo,ip, mac, marcaDisco, tipoDisco,
				tamanoDisco, modeloProcesador, modeloRam, tamanoRam, marcaRam,
				modeloMotherboard, fabricante, monitor, impresora);
		return this;
	}

	// ////////////////////////////////////
	// Listando Impresoras
	// ////////////////////////////////////

	public List<Impresora> choices12AsignarNuevoEquipo() {
		return this.impresoraRepositorio.listAll();
	}

	// ////////////////////////////////////
	// Listando Monitores
	// ////////////////////////////////////

	public List<Monitor> choices11AsignarNuevoEquipo() {
		return this.monitorRepositorio.listAll();
	}

	public boolean hideAsignarNuevoEquipo() {
		return this.getEstado().escondeAsignarNuevoEquipo();
	}

	/* ***************************************************
	 * FIN: Operaciones del State.
	 * ***************************************************
	 */

	/**
	 * Inyección del servicio para Tecnico.
	 */
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	/**
	 * Inyección del servicio para Computadora.
	 */
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;

	/**
	 * Inyección del servicio para Impresora.
	 */
	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;

	/**
	 * Inyección del servicio para Monitor.
	 */
	@javax.inject.Inject
	private MonitorRepositorio monitorRepositorio;
}