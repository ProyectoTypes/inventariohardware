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
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
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
@ObjectType("Pedido de Insumos")
@Bookmarkable
public class ReporteLine implements Comparable<ReporteLine> {

    // order (propiedad)
    private Reporte order;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Hidden(where = Where.REFERENCES_PARENT)
    @MemberOrder(sequence = "1")
    public Reporte getOrder() {
        return order;
    }

    public void setOrder(final Reporte order) {
        this.order = order;
    }

    // name (propiedad)
    private String description;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Title(sequence = "1")
    @MemberOrder(sequence = "2")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    // quantity (propiedad)
    private int quantity;

    @MemberOrder(sequence = "3")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    // cost (propiedad)
    private BigDecimal cost;

    @javax.jdo.annotations.Column(allowsNull="true", scale=2)
    @javax.validation.constraints.Digits(integer=10, fraction=2)
    @MemberOrder(sequence = "4")
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(final BigDecimal cost) {
        this.cost = cost;
    }

    //compareTo
    @Override
    public int compareTo(ReporteLine other) {
        return ObjectContracts.compare(this, other, "description");
    }

    //region > injected services
    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;
}