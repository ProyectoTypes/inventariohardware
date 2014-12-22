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
package servicio.reporte.templates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.value.Blob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.google.common.io.Resources;
import com.google.inject.name.Named;

import dom.insumo.Insumo;
import dom.soporte.Soporte;

/**
 * Clase CustomerConfirmation.
 */
@DomainService
public class ConfirmarReporte {

    private byte[] pdfAsBytes;

    @PostConstruct
    public void init() throws IOException {
        pdfAsBytes = Resources.toByteArray(Resources.getResource(this.getClass(), "Reporte.pdf"));
    }
    
    /**
     * Descargar el Reporte en formato PDF.
     * @param soporte
     * @return
     * @throws Exception
     */
    @NotContributed(NotContributed.As.ASSOCIATION)
	@NotInServiceMenu
	@ActionSemantics(Of.SAFE)
	@Named("Descargar Pedido")
	@MemberOrder(name = "Insumos Solicitados", sequence = "31")
    public Blob descargarReporte(
            final Soporte soporte) throws Exception {

        try (PDDocument pdfDocument = loadAndPopulateTemplate(soporte)) {

            final ByteArrayOutputStream target = new ByteArrayOutputStream();
            pdfDocument.save(target);

            final String name = "Reporte-" + soporte.getFecha() + ".pdf";
            final String mimeType = "application/pdf";
            final byte[] bytes = target.toByteArray();

            return new Blob(name, mimeType, bytes);
        }
    }

    /**
     * Carga el archivo de plantilla pdf y lo llena con los detalles de la orden.
     * @param soporte
     * @return
     * @throws Exception
     */
	private PDDocument loadAndPopulateTemplate(Soporte soporte) throws Exception {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfAsBytes));

        PDAcroForm pdfForm = pdfDocument.getDocumentCatalog().getAcroForm();

        List<PDField> fields = pdfForm.getFields();
        List<Insumo> orderLines =  soporte.getInsumos();
        for (PDField field : fields) {

            String fullyQualifiedName = field.getFullyQualifiedName();
            if ("Fecha del Reporte".equals(fullyQualifiedName)) {
                field.setValue(soporte.getFecha().toString());
            } else if ("Organizacion".equals(fullyQualifiedName)) {
                field.setValue("Proyect Types");
            } else if ("Nombre del Técnico".equals(fullyQualifiedName)) {
                field.setValue(soporte.getTecnico().getApellido());
            } else if ("Mensaje".equals(fullyQualifiedName)) {
                String message = "Usted ha solicitado los siguientes Insumos '" + orderLines.size() +"' products";
                field.setValue(message);
            }
        }

        int i = 1;
        Iterator<Insumo> orderLineIterator = orderLines.iterator();
        while (i < 7 && orderLineIterator.hasNext()) {
            Insumo orderLine = orderLineIterator.next();

            String descriptionFieldName = "orderLine|"+i+"|desc";
            pdfForm.getField(descriptionFieldName).setValue(orderLine.getProducto());

            String costFieldName = "orderLine|"+i+"|cost";
            pdfForm.getField(costFieldName).setValue(orderLine.getMarca());

            String quantityFieldName = "orderLine|"+i+"|quantity";
            pdfForm.getField(quantityFieldName).setValue(orderLine.getCantidad()+"");
            i++;
        }
        return pdfDocument;
    }
}