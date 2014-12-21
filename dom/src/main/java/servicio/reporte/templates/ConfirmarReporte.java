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
import java.util.SortedSet;

import javax.annotation.PostConstruct;

import com.google.common.io.Resources;
import com.google.inject.name.Named;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import servicio.reporte.Reporte;
import servicio.reporte.ReporteLine;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.value.Blob;

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
     * Descargar el pdf del reporte.
     * 
     * @param reporte
     * @return Las variables del pdf a crear.
     * @throws Excepción si la carga del documento falla
     */
    @NotContributed(NotContributed.As.ASSOCIATION) // ie contributed as action
    @NotInServiceMenu
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
    @Named("Descargar Reporte")
    @MemberOrder(sequence = "10")
    public Blob descargarReporte(
            final Reporte reporte) throws Exception {

        try (PDDocument pdfDocument = loadAndPopulateTemplate(reporte)) {

            final ByteArrayOutputStream target = new ByteArrayOutputStream();
            pdfDocument.save(target);

            final String name = "Reporte-" + reporte.getNumero() + ".pdf";
            final String mimeType = "application/pdf";
            final byte[] bytes = target.toByteArray();

            return new Blob(name, mimeType, bytes);
        }
    }

    /**
     * Carga el archivo de plantilla pdf y lo llena con los detalles de la orden
     *
     * @param order The order with the details for the pdf document
     * @return El documento PDF
     * @throws Excepción si la carga del documento falla.
     */
	private PDDocument loadAndPopulateTemplate(Reporte reporte) throws Exception {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfAsBytes));

        PDAcroForm pdfForm = pdfDocument.getDocumentCatalog().getAcroForm();

        List<PDField> fields = pdfForm.getFields();
        SortedSet<ReporteLine> orderLines = reporte.getOrderLines();
        for (PDField field : fields) {

            String fullyQualifiedName = field.getFullyQualifiedName();
            if ("orderDate".equals(fullyQualifiedName)) {
                field.setValue(reporte.getFechaReporte().toString());
            } else if ("orderNumber".equals(fullyQualifiedName)) {
                field.setValue(reporte.getNumero());
            } else if ("customerName".equals(fullyQualifiedName)) {
                field.setValue(reporte.getTecnico().getApellido());
            } else if ("message".equals(fullyQualifiedName)) {
                String message = "You have ordered '" + orderLines.size() +"' products";
                field.setValue(message);
            }
        }

        int i = 1;
        Iterator<ReporteLine> orderLineIterator = orderLines.iterator();
        while (i < 7 && orderLineIterator.hasNext()) {
            ReporteLine orderLine = orderLineIterator.next();

            String descriptionFieldName = "orderLine|"+i+"|desc";
            pdfForm.getField(descriptionFieldName).setValue(orderLine.getDescription());

            String costFieldName = "orderLine|"+i+"|cost";
            pdfForm.getField(costFieldName).setValue(orderLine.getDescription());

            String quantityFieldName = "orderLine|"+i+"|quantity";
            pdfForm.getField(quantityFieldName).setValue(orderLine.getDescription());
            i++;
        }
        return pdfDocument;
    }
}