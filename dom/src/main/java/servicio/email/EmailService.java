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
package servicio.email;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotContributed.As;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;
import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;

@DomainService
public class EmailService extends AbstractFactoryAndRepository {
	private static final String PROPERTY_ROOT = "mail.smtp.";
	/**
	 * Obtener el Store y el Folder de Inbox
	 */
	private Store store;
	private Message[] mensajes;

	@Programmatic
	public Message[] getMensajes() {
		return mensajes;
	}

	@Programmatic
	public void setMensajes(Message[] mensajes) {
		this.mensajes = mensajes;
	}
	/**
	 * SETEO DE LA SESSION.
	 */
	private Session session;

	@Programmatic
	public Session getSession() {
		return session;
	}

	private Properties propiedades = new Properties();

	@Programmatic
	public void setSession(Properties propiedades) {
		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	@Programmatic
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
	}

	@NotContributed(As.ASSOCIATION)
	@NotInServiceMenu
	@Named("Enviar Correo")
	public String send(final Computadora unaComputadora) {
		String asunto = "Servicio Tecnico le informa que ....";
		// Direccion:
		String correoReceptor = unaComputadora.getUsuario().getEmail();
		String nombreReceptor = unaComputadora.getUsuario().getApellido()
				+ ", " + unaComputadora.getUsuario().getNombre();
		// Email:
		String mensaje = "La Computadora (IP: "
				+ unaComputadora.getIp()
				+ ") correspondiente al usuario "
				+ unaComputadora.getUsuario().getApellido()
				+ ", "
				+ unaComputadora.getUsuario().getNombre()
				+ " ya se encuentra disponible para su retiro. \n Tecnico encargado de su reparación:"
				+ unaComputadora.getTecnico().getApellido() + ", "
				+ unaComputadora.getTecnico().getNombre() + ". \n Email: "
				+ unaComputadora.getTecnico().getEmail();
		String correoEmisor = unaComputadora.getTecnico().getEmail();
		String nombreEmisor = unaComputadora.getTecnico().getApellido() + ", "
				+ unaComputadora.getTecnico().getNombre();
		/*
		 * Configuracion para enviar email.
		 */
		String smtpHost = getContainer().getProperty(PROPERTY_ROOT + "host",
				"smtp.gmail.com");

		String portValue = getContainer().getProperty(PROPERTY_ROOT + "port",
				"587");

		int port = Integer.valueOf(portValue).intValue();
		// Emisor
		String authenticationName = getContainer().getProperty(
				PROPERTY_ROOT + "user", "inventariohardware@gmail.com");
		String authenticationPassword = getContainer().getProperty(
				PROPERTY_ROOT + "password", "inventario123");

		// EN CASO QUE SEA NULL EL CAMPO; SE PONEN LOS SIGUIENTES POR DEFECTO.
		// String fromName = getContainer().getProperty(
		// PROPERTY_ROOT + "from.name", "No reply");
		// String fromEmailAddress = getContainer().getProperty(
		// PROPERTY_ROOT + "from.address", "noreply@domain.com");

		try {

			SimpleEmail simpleEmail = new SimpleEmail();
			simpleEmail.setHostName(smtpHost);
			simpleEmail.setSmtpPort(port);
			simpleEmail.setSSL(true);
			if (authenticationName != null) {
				simpleEmail.setAuthentication(authenticationName,
						authenticationPassword);
			}
			simpleEmail.addTo(correoReceptor, nombreReceptor);
			simpleEmail.setFrom(correoEmisor, nombreEmisor);
			simpleEmail.setSubject(asunto);
			simpleEmail.setMsg(mensaje);
			return simpleEmail.send();
		} catch (EmailException e) {
			throw new servicio.email.EmailException(e.getMessage(), e);
		}
	}

	@Named("Computadora")
	@DescribedAs("Buscar el Computadora en mayuscula")
	public List<Computadora> autoComplete0Send(final @MinLength(2) String search) {
		return computadoraRepositorio.autoComplete(search);

	}

	
	/**
	 * 
	 * @return Retorna la lista de correos persistidos
	 * @throws EncriptaException
	 */
	@Named("Bandeja de Entrada")
	@MemberOrder(sequence = "2")
	public List<Correo> bandeja(
			final @Named("Correo") CorreoEmpresa correoEmpresa)
			throws EncriptaException {
		System.out.println("ANTES DE LA BUSQUEDA " + correoEmpresa.getCorreo());
		System.out.println("ANTES DE LA BUSQUEDA " + correoEmpresa.getPass());

		this.setProperties();
		this.container.warnUser("BANDEJA");

		final List<Correo> listaJavaMail = this.accion(correoEmpresa);

		String mensajeNuevos = listaJavaMail.size() == 1 ? "TIENES UN NUEVO CORREO!"
				: "TIENES " + listaJavaMail.size() + " CORREOS NUEVOS";

		if (listaJavaMail != null && listaJavaMail.size() > 0) {

			this.container.warnUser(mensajeNuevos);

			for (Correo mensaje : listaJavaMail) {

				final Correo correoMensaje = newTransientInstance(Correo.class);
				// if (existeMensaje(mensaje.getAsunto()) == null) {
				correoMensaje.setEmail(mensaje.getEmail());
				correoMensaje.setAsunto(mensaje.getAsunto());
				correoMensaje.setMensaje(mensaje.getMensaje());
				correoMensaje.setTecnico(currentUserName());
				correoMensaje.setCorreoEmpresa(correoEmpresa);
				correoMensaje.setFechaActual(mensaje.getFechaActual());
				this.container.persistIfNotAlready(correoMensaje);
				// }
			}

		}
		return listarMensajesPersistidos(correoEmpresa);
	}
	@Programmatic
	public List<CorreoEmpresa> choices0Bandeja() {
		return this.listar();
	}
	

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	/*
	 * INJECT
	 */
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;

	@javax.inject.Inject
	private DomainObjectContainer container;

}
