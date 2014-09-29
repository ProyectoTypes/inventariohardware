/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package servicio.docx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.value.Blob;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.isisaddons.module.docx.dom.DocxService;
import org.isisaddons.module.docx.dom.LoadTemplateException;
import org.isisaddons.module.docx.dom.MergeException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;

import com.google.common.io.Resources;

import dom.insumo.Insumo;
import dom.soporte.Soporte;

@DomainService
public class InsumosServiceDocx {

	// region > init

	private WordprocessingMLPackage wordprocessingMLPackage;

	@PostConstruct
	public void init() throws IOException, LoadTemplateException {
		final byte[] bytes = Resources.toByteArray(Resources.getResource(
				this.getClass(), "Insumos.docx"));
		wordprocessingMLPackage = docxService
				.loadPackage(new ByteArrayInputStream(bytes));
	}

	// endregion

	// region > downloadCustomerConfirmation (action)

	/**
	 * Metodo que permite descargar el archivo.
	 * 
	 * @param soporte
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 * @throws MergeException
	 */
	@NotContributed(NotContributed.As.ASSOCIATION)
	@NotInServiceMenu
	@ActionSemantics(Of.SAFE)
	@Named("Descargar Pedido")
	@MemberOrder(sequence = "10")
	public Blob downloadCustomerConfirmation(final Soporte soporte)
			throws IOException, JDOMException, MergeException {

		final org.w3c.dom.Document w3cDocument = asInputW3cDocument(soporte);

		final ByteArrayOutputStream docxTarget = new ByteArrayOutputStream();
		docxService.merge(w3cDocument, wordprocessingMLPackage, docxTarget,
				DocxService.MatchingPolicy.LAX);

		final String blobName = "Insumo-.docx";
		final String blobMimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		final byte[] blobBytes = docxTarget.toByteArray();

		return new Blob(blobName, blobMimeType, blobBytes);
	}

	/**
	 * Metodo que permite crear el archivo.
	 * 
	 * @param soporte
	 * @return
	 * @throws JDOMException
	 */
	private static org.w3c.dom.Document asInputW3cDocument(Soporte soporte)
			throws JDOMException {
		Document orderAsHtmlJdomDoc = asInputDocument(soporte);

		DOMOutputter domOutputter = new DOMOutputter();
		return domOutputter.output(orderAsHtmlJdomDoc);
	}

	/**
	 * Se encarga de fusionar los datos de la entidad con el template.
	 * 
	 * @param soporte
	 * @return
	 */
	private static Document asInputDocument(Soporte soporte) {
		Element html = new Element("html");
		Document document = new Document(html);

		Element body = new Element("body");
		html.addContent(body);

		//List<Insumo> insumos = new ArrayList<Insumo>();
		//insumos = soporte.getInsumos();
		//addPara(body, "Codigo", "plain", soporte.getCodigo());
		//addPara(body, "Cantidad", "plain", soporte.getCantidad() + "");
		//addPara(body, "Producto", "date", soporte.getProducto());
//		addPara(body, "Marca", "plain", soporte.getMarca());
//		addPara(body, "Observaciones", "plain", soporte.getObservaciones());
//		addPara(body, "Message", "plain", "MENSAJE DE PRUEBA");
		
		addPara(body, "Fecha", "date", soporte.getFecha().toString("dd-MMM-yyyy"));
		
        Element table = addTable(body, "Products");
        for(Insumo insumos: soporte.getInsumos()) {
            addTableRow(table, new String[]{insumos.getCodigo(), insumos.getCantidad() + ""});
        }

		return document;
	}

	// endregion

	// region > helpers
	
    private static void addPara(Element body, String id, String clazz, String text) {
        Element p = new Element("p");
        body.addContent(p);
        p.setAttribute("id", id);
        p.setAttribute("class", clazz);
        p.setText(text);
    }
	
	private static Element addTable(Element body, String id) {
		Element table = new Element("table");
		body.addContent(table);
		table.setAttribute("id", id);
		return table;
	}

	private static void addTableRow(Element table, String[] cells) {
		Element tr = new Element("tr");
		table.addContent(tr);
		for (String columnName : cells) {
			Element td = new Element("td");
			tr.addContent(td);
			td.setText(columnName);
		}
	}

	// endregion

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	@javax.inject.Inject
	private DocxService docxService;

	// endregion
}
