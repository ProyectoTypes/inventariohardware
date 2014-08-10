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

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
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
import dom.movimiento.estadoComputadora.Cancelado;
import dom.movimiento.estadoComputadora.Entregado;
import dom.movimiento.estadoComputadora.Esperando;
import dom.movimiento.estadoComputadora.IEstado;
import dom.movimiento.estadoComputadora.Recepcionado;
import dom.movimiento.estadoComputadora.Reparando;
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
		return "Movimiento";
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
	@MemberOrder(sequence = "20")
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
	@MemberOrder(sequence = "20")
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

	@Hidden(where = Where.ALL_TABLES)
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
		Recepcionado recepcion = new Recepcionado();
		Reparando reparacion = null;
		Entregado entregando = null;
		Esperando esperando = null;
		Cancelado cancelacion = null;

		this.recepcionado = recepcion;
		this.reparando = reparacion;
		this.entregando = entregando;
		this.esperando = esperando;
		this.cancelado = cancelacion;

		this.estado = new Recepcionado();
		this.estadoActual = this.estado.toString();

	}

	/**********************************************************
	 * PATRON STATE
	 **********************************************************/
	private IEstado estado;

	// @Hidden
	// @Programmatic
	@javax.jdo.annotations.Column(allowsNull = "true")
	public IEstado getEstado() {
		return estado;
	}

	public void setEstado(IEstado estado) {
		this.estado = estado;
	}

	private String estadoActual;

	@Disabled
	@MemberOrder(sequence = "300")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public String getEstadoActual() {
		return this.estadoActual;
	}

	public void setEstadoActual(String mostrarEstado) {
		this.estadoActual = mostrarEstado;
	}

	/* *************************************************** */

	private Recepcionado recepcionado;

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	// @Programmatic
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
	// @Programmatic
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
	// @Programmatic
	public Cancelado getCancelado() {
		return this.cancelado;
	}

	public void setCancelado(Cancelado cancelado) {
		this.cancelado = cancelado;
	}

	/* *************************************************** */

	// {{ Entregando (property)
	private Entregado entregando;

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	// @Programmatic
	public Entregado getEntregando() {
		return entregando;
	}

	public void setEntregando(final Entregado entregando) {
		this.entregando = entregando;
	}

	// }}
	/* *************************************************** */

	// {{ Esperando (property)
	private Esperando esperando;

	// @Hidden
	@MemberOrder(sequence = "200")
	@javax.jdo.annotations.Column(allowsNull = "true")
	// @Programmatic
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
	 * Permite seleccionar un tecnico desde una lista. El Tecnico es agregado en
	 * la Computadora. Al tecnico se le suma una nueva computadora. Cambio de
	 * estado a Reparando.
	 * 
	 * @param unTecnico
	 * @return
	 */
	@PostConstruct
	public Movimiento asignarTecnico(final Tecnico unTecnico) {
		// Recepcionado -> Reparando.
		this.estadoActivo();
		this.setTecnico(unTecnico);
		unTecnico.addToComputadora(this.getComputadora());
		IEstado estadoReparando = this.getEstado().asignarTecnico(this);
		//Operaciones mantenimiento de estado.
		this.setEstado(estadoReparando);
		this.setRecepcionado(null);
		this.setReparando(new Reparando());
		this.container.flush();
		return this;
	}

	@Programmatic
	private List<Tecnico> choice0AsignarTecnico() {
		return this.tecnicoRepositorio.listar();
	}

	@PostConstruct
	// @Programmatic
	@Named("Solicitar Repuestos")
	public Movimiento esperarRepuestos() {
		// this.estado.esperarRepuestos(this);
		// Reparando -> Esperando
		this.estadoActivo();
		IEstado estadoEsperando = this.getEstado().esperarRepuestos(this);
		this.setEstado(estadoEsperando);
		this.setReparando(null);
		this.setEsperando(new Esperando());
		this.container.flush();
		return this;
	}

	@PostConstruct
	// @Programmatic
	public Movimiento finalizarSoporte() {
		// this.estado.finalizarSoporte(this);
		// Reparando -> Entregando

		this.estadoActivo();
		IEstado estadoEntregado = this.getEstado().finalizarSoporte(this);
		this.setEstado(estadoEntregado);
		this.setReparando(null);
		this.setEsperando(null);
		this.setEntregando(new Entregado());
		this.container.flush();
		return this;

	}

	@PostConstruct
	// @Programmatic
	public Movimiento noHayRepuestos() {
		// this.estado.noHayRepuestos(this);
		// Esperando -> Cancelado
		this.estadoActivo();
		IEstado estadoCancelado = this.getEstado().noHayRepuestos(this);
		this.setEstado(estadoCancelado);
		this.setEsperando(null);
		this.setCancelado(new Cancelado());
		this.container.flush();
		return this;

	}

	@PostConstruct
	// @Programmatic
	public Movimiento llegaronRepuestos() {
		// this.estado.llegaronRepuestos(this);
		// Esperando -> Entregando
		this.estadoActivo();
		IEstado estadoEntregado = this.getEstado().llegaronRepuestos(this);
		this.setEstado(estadoEntregado);
		this.setEsperando(null);
		this.setEntregando(new Entregado());
		this.container.flush();
		return this;
	}

	// *********************************************************************************************
	@Programmatic
	public void estadoActivo() {
		if (this.getRecepcionado() != null)
			this.setEstado(new Recepcionado());
		else if (this.getReparando() != null)
			this.setEstado(new Reparando());
		else if (this.getEntregando() != null)
			this.setEstado(new Entregado());
		else if (this.getEsperando() != null)
			this.setEstado(new Esperando());
		else if (this.getCancelado() != null)
			this.setEstado(new Cancelado());

	}

	// *********************************************************************************************

	@MemberOrder(sequence = "20")
	@Named("MOSTRAR ACTUAL")
	@PostConstruct
	public Movimiento mostrarLaClaseDelEstado() {

		if (this.getEstado() != null) {
			this.setObservaciones(this.getEstado().getClass().getSimpleName());

		} else
			this.setObservaciones("NO: NULL");
		return this;
	}

	@PostConstruct
	public Movimiento nulear() {
		this.setRecepcionado(null);
		this.setReparando(null);
		this.setCancelado(null);
		this.setEntregando(null);
		this.container.flush();
		// this.setIAnimal(new Delfin());
		// this.setIAnimal(new Delfin());
		return this;
	}

	
	/* ***************************************************
	 * FIN: Patron State.
	 * ***************************************************
	 */

	/********************************************************
	 * Relacion Tecnico/Movimiento.
	 ********************************************************/

	private Tecnico tecnico;

	@Optional
	@MemberOrder(sequence = "1")
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
	private SortedSet<Insumo> insumos = new TreeSet<Insumo>();

	public SortedSet<Insumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(final SortedSet<Insumo> insumos) {
		this.insumos = insumos;
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
	private DomainObjectContainer container;

}