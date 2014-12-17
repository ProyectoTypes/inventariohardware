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
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.joda.time.LocalDate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

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

    //region > numero (property)
    // //////////////////////////////////////
    private String numero;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Title(sequence = "1")
    @MemberOrder(sequence = "1")
    public String getNumero() {
        return numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }
    //endregion

    //region > nombreTecnico (property)

    private String nombreTecnico;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Title(prepend= ", ", sequence="2")
    @MemberOrder(sequence = "1")
    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(final String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    //endregion

    //region > fechaReporte (property)

    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate fechaReporte;

    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(sequence = "1")
    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(final LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
    //endregion

    //region > orderLines (collection)

    @javax.jdo.annotations.Persistent(mappedBy = "order")
    private SortedSet<ReporteLine> orderLines = new TreeSet<ReporteLine>();

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

    @MemberOrder(name = "orderLines", sequence = "1")
    public Reporte add(
            final @Named("Description") String description,
            final @Named("Cost") BigDecimal cost,
            final @Named("Quantity") int quantity) {

        final ReporteLine orderLine = container.newTransientInstance(ReporteLine.class);
        orderLine.setCost(cost);
        orderLine.setDescription(description);
        orderLine.setQuantity(quantity);
        getOrderLines().add(orderLine); // will set the parent on the OrderLine

        container.persistIfNotAlready(orderLine);

        return this;
    }

    @MemberOrder(name = "orderLines", sequence = "2")
    public Reporte remove(final ReporteLine orderLine) {
        removeFromOrderLines(orderLine);
        return this;
    }

    public Collection<ReporteLine> choices0Remove() {
        return getOrderLines();
    }


    //endregion

    //region > compareTo

    @Override
    public int compareTo(Reporte other) {
        return ObjectContracts.compare(this, other, "number");
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    private DomainObjectContainer container;

    //endregion
}