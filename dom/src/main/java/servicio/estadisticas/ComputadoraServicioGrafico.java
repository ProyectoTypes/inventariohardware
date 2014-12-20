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
package servicio.estadisticas;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Named;
import org.isisaddons.wicket.wickedcharts.cpt.applib.WickedChart;

import com.google.common.collect.Maps;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.Cursor;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.color.NullColor;
import com.googlecode.wickedcharts.highcharts.options.color.RadialGradient;
import com.googlecode.wickedcharts.highcharts.options.functions.PercentageFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;
import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;
import dom.soporte.Soporte;
import dom.soporte.SoporteRepositorio;

@DomainService
@Named("Estadisticas")
public class ComputadoraServicioGrafico  {


	 @ActionSemantics(Of.SAFE)
	    public WickedChart filtrarPorDiscoRigido() {
	        
	        Map<CategoriaDisco, AtomicInteger> byCategory = Maps.newTreeMap();
	        List<Computadora> allToDos = computadoraRepositorio.listAll();
	        for (Computadora unaComputadora : allToDos) {
	            CategoriaDisco category = unaComputadora.getDisco().getTipo();
	            AtomicInteger integer = byCategory.get(category);
	            if(integer == null) {
	                integer = new AtomicInteger();
	                byCategory.put(category, integer);
	            }
	            integer.incrementAndGet();
	        }
	        
	        return new WickedChart(new PieWithGradientOptions(byCategory));
	    }
    
    public static class PieWithGradientOptions extends Options {
        private static final long serialVersionUID = 1L;

        public PieWithGradientOptions(Map<CategoriaDisco, AtomicInteger> byCategory) {
        
            setChartOptions(new ChartOptions()
                .setPlotBackgroundColor(new NullColor())
                .setPlotBorderWidth(null)
                .setPlotShadow(Boolean.FALSE));
            
            setTitle(new Title("Computadoras: Discos Rigidos."));
        
            PercentageFormatter formatter = new PercentageFormatter();
            setTooltip(
                    new Tooltip()
                        .setFormatter(
                                formatter)
                        .       setPercentageDecimals(1));
        
            setPlotOptions(new PlotOptionsChoice()
                .setPie(new PlotOptions()
                .setAllowPointSelect(Boolean.TRUE)
                .setCursor(Cursor.POINTER)
                .setDataLabels(new DataLabels()
                .setEnabled(Boolean.TRUE)
                .setColor(new HexColor("#000000"))
                .setConnectorColor(new HexColor("#000000"))
                .setFormatter(formatter))));

            Series<Point> series = new PointSeries()
                .setType(SeriesType.PIE);
            int i=0;
            for (Entry<CategoriaDisco, AtomicInteger> entry : byCategory.entrySet()) {
                series
                .addPoint(
                        new Point(entry.getKey().name(), entry.getValue().get()).setColor(
                                new RadialGradient()
                                    .setCx(0.5)
                                    .setCy(0.3)
                                    .setR(0.7)
                                        .addStop(0, new HighchartsColor(i))
                                        .addStop(1, new HighchartsColor(i).brighten(-0.3f))));
                i++;
            }
            addSeries(series);
        }
    }
    public static enum CategoriaSoporte {
		Recibido, Reparando, Otro;
	}
    @Inject 
    private SoporteRepositorio soporteRepositorio;
    /**
     * Todas las computadoras que esten en soporte
     * @return
     */
    @Named("Soporte Tecnico")
    @ActionSemantics(Of.SAFE)
    public WickedChart filtrarPorComputadorasReparacion() {
        
        Map<String, AtomicInteger> byCategory = Maps.newTreeMap();
        List<Soporte> allSoportes = soporteRepositorio.listAll();
        for (Soporte obj : allSoportes) {
        	String category = obj.getEstado().getClass().getSimpleName();//.getDisco();
            AtomicInteger integer = byCategory.get(category);
            if(integer == null) {
                integer = new AtomicInteger();
                byCategory.put(category, integer);
            }
            integer.incrementAndGet();
        }
        
        return new WickedChart(new PieWithGradientOptions2(byCategory));
    }
    
    public static class PieWithGradientOptions2 extends Options {
        private static final long serialVersionUID = 1L;

        public PieWithGradientOptions2(Map<String, AtomicInteger> byCategory) {
        
            setChartOptions(new ChartOptions()
                .setPlotBackgroundColor(new NullColor())
                .setPlotBorderWidth(null)
                .setPlotShadow(Boolean.FALSE));
            
            setTitle(new Title("Estado de las Computadoras"));
        
            PercentageFormatter formatter = new PercentageFormatter();
            setTooltip(
                    new Tooltip()
                        .setFormatter(
                                formatter)
                        .       setPercentageDecimals(1));
        
            setPlotOptions(new PlotOptionsChoice()
                .setPie(new PlotOptions()
                .setAllowPointSelect(Boolean.TRUE)
                .setCursor(Cursor.POINTER)
                .setDataLabels(new DataLabels()
                .setEnabled(Boolean.TRUE)
                .setColor(new HexColor("#FF8C00"))
                .setConnectorColor(new HexColor("#483D8B"))
                .setFormatter(formatter))));

            Series<Point> series = new PointSeries()
                .setType(SeriesType.PIE);
            int i=0;
            for (Map.Entry<String, AtomicInteger> entry : byCategory.entrySet()) {
                series
                .addPoint(
                        new Point(entry.getKey(), entry.getValue().get()).setColor(
                                new RadialGradient()
                                    .setCx(0.5)
                                    .setCy(0.3)
                                    .setR(0.7)
                                        .addStop(0, new HighchartsColor(i))
                                        .addStop(1, new HighchartsColor(i).brighten(-0.3f))));
                i++;
            }
            addSeries(series);
        }
    }

    
    // //////////////////////////////////////
    // Injected services
    // //////////////////////////////////////

    @javax.inject.Inject
    private ComputadoraRepositorio computadoraRepositorio;

}
