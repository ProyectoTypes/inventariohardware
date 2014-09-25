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
package servicio.docx.template;

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
	/*
	 * Todo lo que este con este tipo de comentario hay que borrarlo. Tareas:
	 * Cambiar todo lo que sea order u Order, por soporte o Soporte. Cambiar el
	 * nombre del template por algo mas representativo. Cambiar el nombre
	 * DocumentoInsumos por algo mas representativo si es necesario. Crear
	 * el Template de insumos (si se cambia, tmb cambiar el nombre en la linea 64).
	 */

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
	 * Permite descargar el archivo.
	 * 
	 * @param soporte
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 * @throws MergeException
	 */
	@NotContributed(NotContributed.As.ASSOCIATION)
	// ie contributed as action
	@NotInServiceMenu
	@ActionSemantics(Of.SAFE)
	@Named("Descargar")
	@MemberOrder(sequence = "10")
	public Blob downloadCustomerConfirmation(final Soporte soporte)
			throws IOException, JDOMException, MergeException {

		final org.w3c.dom.Document w3cDocument = asInputW3cDocument(soporte);

		final ByteArrayOutputStream docxTarget = new ByteArrayOutputStream();
		docxService.merge(w3cDocument, wordprocessingMLPackage, docxTarget,
				DocxService.MatchingPolicy.LAX);

		// Titulo del archivo de salida, se le puede colocar la ip de la
		// computadora, o el nombre del tecnico que lo solicita, etc.
		final String blobName = "customerConfirmation.docx";

		final String blobMimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		final byte[] blobBytes = docxTarget.toByteArray();

		return new Blob(blobName, blobMimeType, blobBytes);
	}

	/**
	 * Permite Crear el Archivo.
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
	 * <p>
	 * <b>body</b> para los atributos de la entidad.
	 * <b>date</b> para las fechas.
	 * <b>rich</b> para los textos con estilos.
	 * <b>li</b> para generar una lista.
	 * <b>table</b> para las dependencias.
	 * </p>
	 * 
	 * @param soporte
	 * @return
	 */
	private static Document asInputDocument(Soporte soporte) {
		// Se crea el html como documento nuevo, luego se le agrega el body y las tablas.
		Element html = new Element("html");
		Document document = new Document(html);

		Element body = new Element("body");
		html.addContent(body);
		/*
		 * En las siguientes lineas muestro como se pueden pasar los datos al
		 * template. Hay que adaptarlo a nuestras necesidades y modificarlo
		 * todo.
		 */
		// OrdenNum hace referencia a la etiqueta del template, lo mismo con
		// OrderDate, etc.
		addPara(body, "Direccion Ip", "plain", soporte.getComputadora().getIp());
		addPara(body, "Fecha", "date", soporte.getFecha().toString("dd-MMM-yyyy"));
		addPara(body, "Usuario", "plain", soporte.getComputadora().getUsuario().getNombre());
		addPara(body, "Message", "plain", "Mensaje de prueba para mostrar que no solo se puede enviar atributos, tambien texto");
		/*
		 * En la siguientes lineas muestra como hacer una tabla con las
		 * dependencia de insumos, de aca hay que elegir que datos vamos a
		 * mostrar en el documento (Todos o algunos).
		 */
		Element table = addTable(body, "Insumos");
		for (Insumo orderLine : soporte.getInsumos()) {
			addTableRow(table, new String[] { orderLine.getCodigo(), orderLine.getMarca(), "" + orderLine.getCantidad() });
		}

		/*
		 * Creo que no es necesario crear una lista, en caso contratio habilitar
		 * el siguiente codigo y su/s metodo/s.
		 */
		// Element ul = addList(body, "OrderPreferences");
		// for (String preference : preferencesFor(order)) {
		// addListItem(ul, preference);
		// }
		return document;
	}

	// endregion (

	// region > Metodos de Ayuda, para la fusion entre los datos y el template

	/*
	 * private static final Function<String, String> TRIM = new Function<String,
	 * String>() {
	 * 
	 * @Override public String apply(String input) { return input.trim(); } };
	 * 
	 * private static Iterable<String> preferencesFor(Soporte order) { final
	 * String preferences = order.getPreferences(); if (preferences == null) {
	 * return Collections.emptyList(); } return
	 * Iterables.transform(Splitter.on(",").split(preferences), TRIM); }
	 */

	private static void addPara(Element body, String id, String clazz, String text) {
		Element p = new Element("p");
		body.addContent(p);
		p.setAttribute("id", id);
		p.setAttribute("class", clazz);
		p.setText(text);
	}

	/*
	 * private static Element addList(Element body, String id) { Element ul =
	 * new Element("ul"); body.addContent(ul); ul.setAttribute("id", id); return
	 * ul; }
	 * 
	 * private static Element addListItem(Element ul, String... paras) { Element
	 * li = new Element("li"); ul.addContent(li); for (String para : paras) {
	 * addPara(li, para); } return ul; }
	 * 
	 * private static void addPara(Element li, String text) { if (text == null)
	 * { return; } Element p = new Element("p"); li.addContent(p);
	 * p.setText(text); }
	 */
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
