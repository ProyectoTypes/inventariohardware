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
package dom.insumo;

import java.util.List;

import javax.jdo.annotations.IdentityType;
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
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.LocalDate;

import dom.soporte.Soporte;

// TODO: Auto-generated Javadoc
/**
 * Clase Insumo.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "idInsumo")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Insumo_must_be_unique", members = { "idInsumo" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorInsumo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo "
				+ "WHERE creadoPor == :creadoPor && "
				+ "codigo.indexOf(:codigo) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarInsumoFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "listarInsumoTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && codigo.indexOf(:codigo) >= 0"), })
@ObjectType("INSUMO")
@Audited
@AutoComplete(repository = InsumoRepositorio.class, action = "autoComplete")
public class Insumo implements Comparable<Insumo> {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	/**
	 * Titulo de la clase
	 *
	 * @return the string
	 */
	public String title() {

		return this.getMarca() + " - " + this.getProducto();

	}

	/**
	 * Nombre del icono
	 *
	 * @return the string
	 */
	public String iconName() {
		return "Insumos";
	}

	// //////////////////////////////////////
	// Cantidad (Atributo)
	// //////////////////////////////////////

	/** Cantidad. */
	private int cantidad;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Cantidad a solicitar:")
	@MemberOrder(sequence = "10")
	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(final int cantidad) {
		this.cantidad = cantidad;
	}

	// //////////////////////////////////////
	// Producto (Atributo)
	// //////////////////////////////////////

	/** Producto. */
	private String producto;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre del Insumo:")
	@MemberOrder(sequence = "20")
	public String getProducto() {
		return producto;
	}

	public void setProducto(final String producto) {
		this.producto = producto;
	}

	// //////////////////////////////////////
	// Marca (Atributo)
	// //////////////////////////////////////

	/** Marca. */
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del Insumo:")
	@MemberOrder(sequence = "30")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}

	// //////////////////////////////////////
	// Modelo (Atributo)
	// //////////////////////////////////////

	/** Modelo. */
	private String modelo;

	@javax.jdo.annotations.Column(allowsNull = "true")
	@DescribedAs("Modelo del Insumo:")
	@MemberOrder(sequence = "40")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	/** Habilitado. */
	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "50")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	/** Creado por. */
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
	// Fecha (propiedad)
	// //////////////////////////////////////

	/** Fecha. */
	private LocalDate fecha;

	@Disabled
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "100")
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(final LocalDate fecha) {
		this.fecha = fecha;
	}

	// //////////////////////////////////////
	// Relacion Movimiento/Insumos.
	// //////////////////////////////////////

	/** Soporte. */
	private Soporte soporte;

	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(sequence = "80")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Soporte getSoporte() {
		return soporte;
	}

	public void setSoporte(final Soporte soporte) {
		this.soporte = soporte;
	}

	/**
	 * Modifica el soporte agregando un insumo
	 *
	 * @param soporte
	 */
	public void modifySoporte(final Soporte soporte) {
		Soporte currentSoporte = getSoporte();
		if (soporte == null || soporte.equals(currentSoporte)) {
			return;
		}
		soporte.agregarUnInsumo(this);
	}

	/**
	 * Limpieza de insumos en soporte.
	 */
	public void clearSoporte() {
		Soporte currentSoporte = getSoporte();
		if (currentSoporte == null) {
			return;
		}
		currentSoporte.eliminarInsumos(this);
	}

	/**
	 * Método que se utiliza para deshabilitar un Insumo.
	 * 
	 * @return la propiedad habilitado en false.
	 */
	@Named("Eliminar")
	@PublishedAction
	@Bulk
	@MemberOrder(name = "accionEliminar", sequence = "1")
	public List<Insumo> eliminar() {
		if (getEstaHabilitado() == true) {
			setHabilitado(false);
			container.isPersistent(this);
			container.warnUser("Eliminado " + container.titleOf(this));
		}
		return null;
	}

	@Programmatic
	public void limpiarSoporte() {
	}

	@Override
	public int compareTo(Insumo o) {
		return 0;
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	/** Container. */
	@javax.inject.Inject
	private DomainObjectContainer container;
}
