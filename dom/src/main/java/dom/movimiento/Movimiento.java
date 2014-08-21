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
package dom.movimiento;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
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
import dom.insumo.Insumo;
import dom.insumo.InsumoRepositorio;
import dom.movimiento.equipo.Cancelado;
import dom.movimiento.equipo.Entregando;
import dom.movimiento.equipo.Esperando;
import dom.movimiento.equipo.IEstado;
import dom.movimiento.equipo.Recepcionado;
import dom.movimiento.equipo.Reparando;
import dom.tecnico.Tecnico;
import dom.tecnico.TecnicoRepositorio;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Movimiento_observaciones_must_be_unique", members = { "fecha,creadoPor,observaciones" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorMovimiento", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor && "
				+ "tecnico.getNombre().indexOf(:buscarTecnico) >= 0"),
		@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && computadora.getIp().indexOf(:ip) >= 0"), })
@ObjectType("MOVIMIENTO")
@Audited
@AutoComplete(repository = MovimientoRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Movimiento implements Comparable<Movimiento> {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	public String title() {
		return "SOPORTE TECNICO";
	}

	public String iconName() {
		return "Movimiento";
	}

	// //////////////////////////////////////
	// Obeservaciones (propiedad)
	// //////////////////////////////////////

	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones de la Computadora:")
	@MemberOrder(name="Detalles",sequence = "20")
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
	@MemberOrder(name= "Datos Generales",sequence = "30")
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

	/****************
	 * CONSTRUCTOR::
	 ****************/
	public Movimiento() {
		this.recepcionado = new Recepcionado(this);
		this.reparando = new Reparando(this);
		this.entregando = new Entregando(this);
		this.esperando = new Esperando(this);
		this.cancelado = new Cancelado(this);

		this.estado = this.recepcionado;
	}

	/**********************************************************
	 * PATRON STATE
	 **********************************************************/
	private IEstado estado;

	@Persistent(extensions = {
			@Extension(vendorName = "datanucleus", key = "mapping-strategy", value = "per-implementation"),
			@Extension(vendorName = "datanucleus", key = "implementation-classes", value = "dom.movimiento.equipo.Recepcionado"
					+ ",dom.movimiento.equipo.Reparando"
					+ ",dom.movimiento.equipo.Esperando"
					+ ",dom.movimiento.equipo.Cancelado"
					+ ",dom.movimiento.equipo.Entregando") }, columns = {
			@javax.jdo.annotations.Column(name = "idrecepcionado"),
			@javax.jdo.annotations.Column(name = "idreparando"),
			@javax.jdo.annotations.Column(name = "idesperando"),
			@javax.jdo.annotations.Column(name = "idcancelado"),
			@javax.jdo.annotations.Column(name = "identregando") })
	@Optional
	@Hidden(where = Where.PARENTED_TABLES)
	@Disabled
	@MemberOrder(name= "Datos Generales",sequence = "0")
	public IEstado getEstado() {
		return estado;
	}

	public void setEstado(IEstado estado) {
		this.estado = estado;
	}

	/* *************************************************** */

	private Recepcionado recepcionado;

	// @Hidden
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

	// @Hidden
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

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Cancelado getCancelado() {
		return this.cancelado;
	}

	public void setCancelado(Cancelado cancelado) {
		this.cancelado = cancelado;
	}

	/* *************************************************** */

	// {{ Entregando (property)
	private Entregando entregando;

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Entregando getEntregando() {
		return entregando;
	}

	public void setEntregando(final Entregando entregando) {
		this.entregando = entregando;
	}

	// }}
	/* *************************************************** */

	// {{ Esperando (property)
	private Esperando esperando;

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Esperando getEsperando() {
		return esperando;
	}

	public void setEsperando(final Esperando esperando) {
		this.esperando = esperando;
	}

	// }}

	/* ***************************************************
	 * FIN: Atributos del State.
	 * ***************************************************
	 */
	/**
	 * Recepcionado -> Reparando. Permite seleccionar un tecnico desde una
	 * lista. El Tecnico es agregado en la Computadora. Al tecnico se le suma
	 * una nueva computadora. Cambio de estado a Reparando.
	 * 
	 * @param unTecnico
	 * @return
	 */
	@Named("Asignar Tecnico")
	@DescribedAs("Comenzar a Reparar.")
	@NotContributed(As.ASSOCIATION)
	public Movimiento asignarTecnico(final Tecnico unTecnico) {
		//En caso que necesite cambiar el tecnico.
		if (this.getTecnico() != null) {
			this.getTecnico().restaComputadora();
			this.setTecnico(null);
		}
		this.setTecnico(unTecnico);
		this.getEstado().asignarTecnico();
		return this;
	}

	public List<Tecnico> choices0AsignarTecnico() {
		return this.tecnicoRepositorio.listar();
	}

	/**
	 * Reparando -> Esperando. Esperar Repuestos: Permite generar un insumo.
	 * 
	 * @param codigo
	 * @param cantidad
	 * @param producto
	 * @param marca
	 * @param observaciones
	 * @return
	 */
	@Named("Solicitar Repuestos")
	public Movimiento esperarRepuestos(final @Named("Codigo") String codigo,
			final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Optional @Named("Observaciones") String observaciones) {
		this.getEstado().esperarRepuestos();
		Insumo uninsumo = null;
		if (this.getEstado().getClass().getSimpleName()
				.contentEquals(this.getEsperando().getClass().getSimpleName())) {
			uninsumo = this.insumoRepositorio.addInsumo(codigo, cantidad,
					producto, marca, observaciones);
			this.agregarAInsumos(uninsumo);
		}
		return this;
	}

	public boolean hideEsperarRepuestos() {
		if (this.getEstado().getClass().getSimpleName()
				.contentEquals(this.getReparando().getClass().getSimpleName()))
			return false;
		else
			return true;
	}

	/**
	 * Reparando -> Entregando. Finalizar el Soporte Tecnico de la computadora.
	 * A partir de aca no puede realizar ninguna accion de soporte sobre la
	 * computadora.
	 * 
	 * @return
	 */
	@Named("Finalizar Soporte")
	@DescribedAs("Envio de email.")
	public Movimiento finalizarSoporte() {
		this.getEstado().finalizarSoporte();
		return this;

	}

	public boolean hideFinalizarSoporte() {
		// TODO: return true if action is hidden, false if visible
		if (this.getEstado().getClass().getSimpleName()
				.contentEquals(this.getReparando().getClass().getSimpleName()))
			return false;
		else
			return true;
	}

	/**
	 * Esperando -> Cancelado. No es posible conseguir repuestos para la
	 * computadora.
	 * 
	 * @return
	 */
	@Named("Cancelar Soporte")
	public Movimiento noHayRepuestos() {
		this.getEstado().noHayRepuestos();
		return this;

	}

	public boolean hideNoHayRepuestos() {
		if (this.getEstado().getClass().getSimpleName()
				.contentEquals(this.getEsperando().getClass().getSimpleName()))
			return false;
		else
			return true;
	}

	/**
	 * Esperando -> Entregado. Llegaron los repuestos, se ensamblo la maquina y
	 * se finalizo la reparacion.
	 * 
	 * @return
	 */
	@Named("Ensamblado/Finalizado")
	@DescribedAs("El equipo es reparado con los respuestos solicitados.")
	public Movimiento llegaronRepuestos() {
		this.getEstado().llegaronRepuestos();
		return this;
	}

	public boolean hideLlegaronRepuestos() {
		if (this.getEstado().getClass().getSimpleName()
				.contentEquals(this.getEsperando().getClass().getSimpleName()))
			return false;
		else
			return true;
	}

	/* ***************************************************
	 * FIN: Patron State.
	 */

	/********************************************************
	 * Relacion Tecnico/Movimiento.
	 ********************************************************/

	private Tecnico tecnico;

	@Optional
	@MemberOrder(name= "Datos Generales",sequence = "10")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	/**********************************************************************
	 * Relacion Moviemiento(Parent)/Insumos(Child).
	 **********************************************************************/

	@Persistent(mappedBy = "movimiento", dependentElement = "trueOrFalse")
	@Join
	private List<Insumo> insumos = new ArrayList<Insumo>();

	public List<Insumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(final List<Insumo> insumos) {
		this.insumos = insumos;
	}

	@Programmatic
	public void agregarAInsumos(final Insumo insumo) {
		if (insumo == null || getInsumos().contains(insumo)) {
			return;
		}
		insumo.limpiarMovimiento();
		insumo.setMovimiento(this);
		getInsumos().add(insumo);
	}

	@Programmatic
	public void eliminarInsumos(final Insumo insumo) {
		if (insumo == null || !getInsumos().contains(insumo)) {
			return;
		}
		insumo.setMovimiento(null);
		getInsumos().remove(insumo);
	}

	/********************************************************
	 * Relacion Computadora/Movimiento.
	 ********************************************************/

	private Computadora computadora;

	@MemberOrder(sequence = "100")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Computadora getComputadora() {
		return computadora;
	}

	public void setComputadora(Computadora computadora) {
		this.computadora = computadora;
	}

	@Programmatic
	@Named("Cambiar Computadora")
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
		setComputadora(null);
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
	public int compareTo(final Movimiento movimiento) {
		return ObjectContracts.compare(this, movimiento, "time_system");
	}

	// ////////////////////////////////////
	// Injected Services
	// ////////////////////////////////////
	@javax.inject.Inject
	private TecnicoRepositorio tecnicoRepositorio;

	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;

	@javax.inject.Inject
	private InsumoRepositorio insumoRepositorio;

	@SuppressWarnings("unused")
	@javax.inject.Inject
	private DomainObjectContainer container;

}