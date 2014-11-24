package servicio.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.inject.Named;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;


@Named("Reportes")
@DomainService
public class GenerarReporte {
	
	public static void generarReporte(String jrxml, List<Object> parametros, FormatoEnum formato, String nombreArchivo)  throws JRException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//Levanta el jrxml
		File file = new File(jrxml);
		
		//Almacena el array de datos
		JRBeanArrayDataSource jArray= new JRBeanArrayDataSource(parametros.toArray());
		
		InputStream input = null;
		try{
			input = new FileInputStream(file);

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		//Levanta el modelo del reporte
		JasperDesign jd = JRXmlLoader.load(input);
		
		//Compila el reporte
		JasperReport reporte = JasperCompileManager.compileReport(jd);
		
		//Lo llena con los datos del datasource
		JasperPrint print = JasperFillManager.fillReport(reporte, map, jArray);
		
		//Lo muestra con el jasperviewer
		//JasperViewer.viewReport(print, false);
					
		//nombreArchivo = reportes/(calificaciones o asistencia/(nombre)
		
		if(formato == FormatoEnum.HojadeCálculo){
			try{
				JRXlsExporter exporterXLS = new JRXlsExporter();
				
				exporterXLS.setExporterInput(new SimpleExporterInput(print));
				exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo + ".xls"));
				
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				
				exporterXLS.setConfiguration(configuration);
				exporterXLS.exportReport();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
		}else{
			if(formato == FormatoEnum.PDF){				
				JasperExportManager.exportReportToPdfFile(print, nombreArchivo + ".pdf" );
			}
		}
		//Muestra el reporte en otra ventana
		//JasperExportManager.exportReportToHtmlFile(print, "reportemp/nuevo.html");		
	}	
	

	@javax.inject.Inject	
	public ReportContext reportContext;
	
	@javax.inject.Inject
	DomainObjectContainer container;
}
