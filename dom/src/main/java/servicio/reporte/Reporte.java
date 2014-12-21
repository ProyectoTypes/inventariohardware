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
package servicio.reporte;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

import dom.tecnico.Tecnico;

/**
 * Reporte: permite generar un documento PDF con los Insumos que han sido solicitados por Técnico.
 * @author ProyectoTypes
 * @since 17/12/2014
 * @version 1.0.0
 */

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@ObjectType("REPORTE")
@Bookmarkable
@MemberGroupLayout(columnSpans = {6,0,0,6})
public class Reporte implements Comparable<Reporte> {

	/**
	 * Titulo de la clase
	 * @return 
	 */
	public String title() {
		return "Reporte N° " + this.getNumero();
	}
	
    // numero (propiedad)
    private String numero;

    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name = "Detalle del Reporte", sequence = "1")
    public String getNumero() {
        return numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }
	
	/*****************************************************
	 * Relacion Reporte/Técnico
	 ****************************************************/
	private Tecnico tecnico;

	@MemberOrder(name = "Detalle del Reporte", sequence = "2")
	@Column(allowsNull = "true")
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	public String validateTecnico(final Tecnico tecnico) {
		if (tecnico.getReporte() == null || this.getTecnico() == tecnico)
			return null;
		else
			return "Seleccione otro Técnico.";
	}
	
	// ///////////////////////////////////////////////////
	// Operaciones del Reporte: Agregar/Borrar Técnico
	// ///////////////////////////////////////////////////
	
	@Named("Modificar Técnico")
	public void modifyTecnico(final Tecnico user) {
		Tecnico tecnico = getTecnico();
		if (user == null || user.equals(tecnico)) {
			return;
		}
		this.clearTecnico();
		user.setReporte(this);
		this.setTecnico(user);
	}
	
	@Named("Borrar Técnico")
	public void clearTecnico() {
		Tecnico tecnico = getTecnico();
		if (tecnico == null) {
			return;
		}
		tecnico.setReporte(null);
		this.setTecnico(null);
	}

    // fechaReporte (propiedad)
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate fechaReporte;

    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name = "Detalle del Reporte", sequence = "3")
    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(final LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    // orderLines (collection)
    @javax.jdo.annotations.Persistent(mappedBy = "order")
    private SortedSet<ReporteLine> orderLines = new TreeSet<ReporteLine>();

    @Named("Pedido de Insumo")
    @Render(Render.Type.EAGERLY)
    public SortedSet<ReporteLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(final SortedSet<ReporteLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void addToOrderLines(final ReporteLine orderLine) {
        getOrderLines().add(orderLine);
    }
    
    public void removeFromOrderLines(final ReporteLine orderLine) {
        getOrderLines().remove(orderLine);
    }

    @Named("Agregar")
    @MemberOrder(name = "Insumos", sequence = "1")
    public Reporte add(
            final @Named("Descripción") String description,
            final @Named("Costo") BigDecimal cost,
            final @Named("Cantidad") int quantity) {

        final ReporteLine orderLine = container.newTransientInstance(ReporteLine.class);
        orderLine.setCost(cost);
        orderLine.setDescription(description);
        orderLine.setQuantity(quantity);
        getOrderLines().add(orderLine);

        container.persistIfNotAlready(orderLine);

        return this;
    }

    @Named("Remover")
    @MemberOrder(name = "Insumos", sequence = "2")
    public Reporte remove(final ReporteLine orderLine) {
        removeFromOrderLines(orderLine);
        return this;
    }

    public Collection<ReporteLine> choices0Remove() {
        return getOrderLines();
    }

    // compareTo
    @Override
    public int compareTo(Reporte other) {
        return ObjectContracts.compare(this, other, "number");
    }

    /**
	 * Inyección del contenedor.
	 */
    @javax.inject.Inject
    private DomainObjectContainer container;
}