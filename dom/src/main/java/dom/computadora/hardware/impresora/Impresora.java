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
package dom.computadora.hardware.impresora;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;

import dom.computadora.hardware.Hardware;

// TODO: Auto-generated Javadoc
/**
 * Clase Impresora.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Impresora_modeloImpresora_must_be_unique", members = {
		"creadoPor", "modeloImpresora" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorModeloImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.impresora.Impresora "
				+ "modeloImpresora.indexOf(:modeloImpresora) >= 0"),
		@javax.jdo.annotations.Query(name = "todasLasImpresoras", language = "JDOQL", value = "SELECT FROM dom.computadora.hardware.impresora.Impresora "
				+ "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.impresora.Impresora " + "WHERE habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPormodeloImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.hardware.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && modeloImpresora.indexOf(:modeloImpresora) >= 0") })
@ObjectType("IMPRESORA")
@Audited
@AutoComplete(repository = ImpresoraRepositorio.class, action = "autoComplete")
public class Impresora {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	/**
	 * Titulo de la clase.
	 *
	 * @return the string
	 */
	public String title() {
		return this.getModeloImpresora() + this.getFabricanteImpresora();
	}

	/**
	 * Nombre del icono
	 *
	 * @return the string
	 */
	public String iconName() {
		return "IMPRESORA";
	}

	// //////////////////////////////////////
	// Modelo Impresora
	// //////////////////////////////////////

	/**
	 * Modelo impresora.
	 */
	private String modeloImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Nombre de la Impresora")
	@MemberOrder(sequence = "10")
	public String getModeloImpresora() {
		return modeloImpresora;
	}

	public void setModeloImpresora(final String modeloImpresora) {
		this.modeloImpresora = modeloImpresora;
	}

	// //////////////////////////////////////
	// Fabricante Impresora
	// //////////////////////////////////////

	/**
	 * Fabricante impresora.
	 */
	private String fabricanteImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Fabricante de la Impresora:")
	@MemberOrder(sequence = "20")
	public String getFabricanteImpresora() {
		return fabricanteImpresora;
	}

	public void setFabricanteImpresora(final String fabricanteImpresora) {
		this.fabricanteImpresora = fabricanteImpresora;
	}

	// //////////////////////////////////////
	// Tipo de Impresora
	// //////////////////////////////////////

	/**
	 * Enumerador de la impresora.
	 */
	public static enum TipoImpresora {
		
		LASER, CHORRO_DE_TINTA, MATRIZ_DE_PUNTO;
	}

	/**
	 * Tipo de impresora.
	 */
	private TipoImpresora tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	public TipoImpresora getTipo() {
		return tipo;
	}

	public void setTipo(TipoImpresora tipo) {
		this.tipo = tipo;
	}

	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	/**
	 * Atributo que especifica que usuario creo la impresora.
	 */
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
	// Relacion Impresora/Computadora
	// //////////////////////////////////////

	/**
	 * Relacion entre impreso y computadora. Una impresora puede ser usada por
	 * varias computadoras.
	 */
	@Persistent(mappedBy = "impresora", dependentElement = "False")
	@Join
	private SortedSet<Hardware> hardware = new TreeSet<Hardware>();

	public SortedSet<Hardware> getHardware() {
		return hardware;
	}

	public void setHardware(final SortedSet<Hardware> hardware) {
		this.hardware = hardware;
	}

	// /**
	// * Agregar computadora.
	// *
	// * @param unaComputadora the una computadora
	// */
	// @Named("Agregar Impresora")
	// public void agregarComputadora(final Computadora unaComputadora) {
	// if (unaComputadora == null || getComputadora().contains(unaComputadora))
	// {
	// return;
	// }
	// unaComputadora.limpiarImpresora();
	// unaComputadora.setImpresora(this);
	// getComputadora().add(unaComputadora);
	// }
	//
	 /**
	 * Limpiar computadora.
	 * Metodo que elimina la computadora que tiene asociada la impresora.
	 *
	 * @param unHardware
	 */
	 @Named("Eliminar de Computadora")
	 public void limpiarHardware(final Hardware unHardware) {
	 if (unHardware == null || !getHardware().contains(unHardware))
	 {
	 return;
	 }
	 unHardware.setImpresora(null);
	 this.getHardware().remove(unHardware);
	 return;
	 }

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	/**
	 * Habilitado.
	 */
	public boolean habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}
}