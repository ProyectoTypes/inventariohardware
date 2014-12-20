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
package servicio.reporte.scripts;

import java.math.BigDecimal;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;
import org.joda.time.LocalDate;

import servicio.reporte.Reporte;
import servicio.reporte.ReporteRepositorio;
import dom.tecnico.Tecnico;

public class PdfModuleAppSetupFixture extends FixtureScript {

    public PdfModuleAppSetupFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Override
    protected void execute(ExecutionContext executionContext) {

        // prereqs
        //executeChild(new PdfModuleAppTeardownFixture(), executionContext);

        // create
        final Reporte order = Create("1234", null, clockService.now().minusDays(5), executionContext);

        order.add("TV", BigDecimal.valueOf(543.21), 1);
        order.add("X-Men", BigDecimal.valueOf(12.34), 1);
        order.add("Battery pack", BigDecimal.valueOf(9.99), 3);
        order.add("LED lamp", BigDecimal.valueOf(2.99), 12);
    }

    // //////////////////////////////////////

    private Reporte Create(
            final String numero,
            final Tecnico tecnico,
            final LocalDate fechaReporte,
            final ExecutionContext executionContext) {
        return executionContext.add(this, orders.create(numero, tecnico, fechaReporte));
    }

    // //////////////////////////////////////

    @javax.inject.Inject
    private ReporteRepositorio orders;

    @javax.inject.Inject
    private ClockService clockService;
}