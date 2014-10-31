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
package dom.correo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.isis.applib.DomainObjectContainer;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;

public class Recibe {

	private Properties propiedades = new Properties();
	private Store store;
	private List<Correo> listaMensajes = new ArrayList<Correo>();

	private static String contenidoMail;
	private Message[] mensajes;

	public Message[] getMensajes() {
		return mensajes;
	}

	public void setMensajes(Message[] mensajes) {
		this.mensajes = mensajes;
	}

	private CorreoEmpresa correoEmpresa = new CorreoEmpresa();

	/**
	 * Retorna la lista con los correos electrónicos nuevos.
	 * 
	 * @return List<Correo>
	 */
	public List<Correo> getListaMensajes() {
		return listaMensajes;
	}

	/**
	 * Setea la lista de correos electrónicos nuevos.
	 * 
	 * @param listaMensajes
	 */
	public void setListaMensajes(List<Correo> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

	private Session session;

	/**
	 * 
	 * @return Retorna la sesión actual
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * A la sesión actual le aplica las propiedades de conexión.
	 * 
	 * @param propiedades
	 */
	public void setSession(Properties propiedades) {
		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	/**
	 * Setea las propiedades para crear la sesión de usuario.
	 */
	public void setProperties(CorreoEmpresa ce) {
		correoEmpresa = ce;
		System.out.println("PROPIEDADES%%%%%% " + ce.getCorreo());
		System.out.println("PROPIEDADES%%%%%% " + ce.getPass());
		// Deshabilitamos TLS
		propiedades.setProperty("mail.pop3.starttls.enable", "false");

		// Hay que usar SSL
		propiedades.setProperty("mail.pop3.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		propiedades.setProperty("mail.pop3.socketFactory.fallback", "false");

		// Puerto 995 para conectarse.
		propiedades.setProperty("mail.pop3.port", "995");
		propiedades.setProperty("mail.pop3.socketFactory.port", "995");

		this.setSession(propiedades);
		// Temporal: para obtener info adicional.
		this.getSession().setDebug(true);
	}

	/**
	 * Retorna las propiedades para crear la sesión de usuario.
	 * 
	 * @return Properties
	 */
	public Properties getProperties() {
		return propiedades;
	}

	/**
	 * 
	 * Se encarga de conectarse al buzon y bajar todos los correos. Los adiciona
	 * a una lista, con la misma seran persistidos en la BD.
	 * 
	 * @throws EncriptaException
	 * 
	 */
	public void accion() throws EncriptaException {

		try {

			store = session.getStore("pop3");
			// System.out.println(" %%%&& PASS DE LA BD "
			// + correoEmpresa.getPass());

			String clave = "TODOS LOS SABADOS EN CASA DE EXE";
			Encripta encripta = new Encripta(clave);

			// String pass = encripta.desencripta(correoEmpresa.getPass());
			// System.out.println("%%%&& PASS DECRYPT " + pass);
			// store.connect("pop.gmail.com", correoEmpresa.getCorreo(), pass);

			store.connect("pop.gmail.com", "inventariohardware@gmail.com",
					"inventario123");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			mensajes = folder.getMessages();

			for (Message mensaje : mensajes) {

				System.out.println("MENSAJES");
				System.out.println(mensaje.getContent());
				//FIXME: EL CONTAINER ES NULL!!! ... por eso nunca se persiste....
				if (this.container != null) {
					final Correo actual = this.container
							.newTransientInstance(Correo.class);

					actual.setEmail(mensaje.getFrom()[0].toString());
					actual.setAsunto(mensaje.getSubject());
					actual.setFechaActual(mensaje.getSentDate());
					actual.setCorreoEmpresa(correoEmpresa);
					analizaParteDeMensaje(mensaje);
					if (contenidoMail.length() < 255) {
						actual.setMensaje(contenidoMail);
					}
					this.container.persistIfNotAlready(actual);
					this.getListaMensajes().add(actual);
				} else
					System.out.println("EL CONTAINER ES NULlllllllll");
			}
			// Cierre de la sesión
			store.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analizaParteDeMensaje(Part unaParte) {
		try {
			// Si es multipart, se analiza cada una de sus partes
			// recursivamente.
			if (unaParte.isMimeType("multipart/*")) {
				Multipart multi;
				multi = (Multipart) unaParte.getContent();

				for (int j = 0; j < multi.getCount(); j++) {
					analizaParteDeMensaje(multi.getBodyPart(j));
				}
			} else {
				// Si es texto, se escribe el texto.
				if (unaParte.isMimeType("text/plain")) {
					contenidoMail = unaParte.getContent().toString();
					System.out.println("Texto " + unaParte.getContentType());
					System.out.println(unaParte.getContent());
					System.out.println("---------------------------------");
				} else {
					// Si es imagen, se guarda en fichero y se visualiza en
					// JFrame
					if (unaParte.isMimeType("image/*")) {
						System.out.println("Imagen "
								+ unaParte.getContentType());
						System.out.println("Fichero=" + unaParte.getFileName());
						System.out.println("---------------------------------");

						// salvaImagenEnFichero(unaParte);
						// visualizaImagenEnJFrame(unaParte);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Inject
	private DomainObjectContainer container;
	public void setProperties() {
		// Deshabilitamos TLS
		propiedades.setProperty("mail.pop3.starttls.enable", "false");

		// Hay que usar SSL
		propiedades.setProperty("mail.pop3.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		propiedades.setProperty("mail.pop3.socketFactory.fallback", "false");

		// Puerto 995 para conectarse.
		propiedades.setProperty("mail.pop3.port", "995");
		propiedades.setProperty("mail.pop3.socketFactory.port", "995");

		this.setSession(propiedades);
		// Temporal: para obtener info adicional.
		this.getSession().setDebug(true);
	}

	public void accion(final String nada) throws EncriptaException, IOException {
		try {
			store = session.getStore("pop3");
			store.connect("pop.gmail.com", "inventariohardware@gmail.com",
					"inventario123");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] listamensajes = null;
			if (folder.exists()) {
				System.out.println("Cantidad de mensajes: "
						+ folder.getMessageCount());
				listamensajes = folder.getMessages();
				System.out.println(listamensajes.length);
			}

			for (int i = 0; i < listamensajes.length; i++) {

				System.out.println("Desde:"
						+ listamensajes[i].getFrom()[0].toString());
				System.out.println("Asunto:" + listamensajes[i].getSubject());
				// listamensajes[i].
			}
			// Cierre de la sesión
			for (Message mensaje : listamensajes) {
				System.out.println("*****************");
				analizaParteDeMensaje(mensaje);
			}
			store.close();
			//
			// for (int i=0;i<listamensajes.length;i++)
			// {
			// if (listamensajes[i].isMimeType("text/*"))
			// System.out.println("¬ N°"+i+listamensajes[i].getContent());
			// // mensaje de texto simple
			// if (listamensajes[i].isMimeType("multipart/*"))
			// System.out.println(listamensajes[i].getContent());
			//
			//
			// // mensaje compuesto
			// }

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
