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

import java.util.List;
import org.joda.time.LocalDate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.ActionSemantics.Of;

@DomainService(menuOrder = "10", repositoryFor = Reporte.class)
@Named("Reporte")
public class ReporteRepositorio {

    //region > identification in the UI
    // //////////////////////////////////////

    public String getId() {
        return "simple";
    }

    public String iconName() {
        return "SimpleObject";
    }

    //endregion

    //region > listAll (action)
    // //////////////////////////////////////

    @Bookmarkable
    @ActionSemantics(Of.SAFE)
    @Named("Listar")
    @MemberOrder(sequence = "1")
    public List<Reporte> listAll() {
        return container.allInstances(Reporte.class);
    }

    //endregion

    //region > create (action)
    // //////////////////////////////////////
    
    @Named("Crear")
    @MemberOrder(sequence = "2")
    public Reporte create(
            final @Named("Order Number") String number,
            final @Named("Customer Name") String customerName,
            final @Named("Order Date") LocalDate date,
            final @Named("Preferences") @Optional String preferences) {
        final Reporte obj = container.newTransientInstance(Reporte.class);
        obj.setNumber(number);
        obj.setDate(date);
        obj.setCustomerName(customerName);
        obj.setPreferences(preferences);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services
    // //////////////////////////////////////

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion
}