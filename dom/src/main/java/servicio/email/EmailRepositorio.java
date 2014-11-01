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
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;
import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;

@DomainService
public class EmailRepositorio extends AbstractFactoryAndRepository {
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
	@DescribedAs("Finalizado la reparacion del equipo se envia un correo al usuario.")
	public String send(final Computadora unaComputadora) {
		String asunto = "Servicio Tecnico Finalizado.";
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
//		 String fromName = getContainer().getProperty(
//		 PROPERTY_ROOT + "from.name", "No reply");
//		 String fromEmailAddress = getContainer().getProperty(
//		 PROPERTY_ROOT + "from.address", "noreply@domain.com");

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

	@Named("Enviar Correo")
	@DescribedAs("El correo de la empresa es el emisor del mensaje.")
	public String send(
			final @Named("De: ") CorreoEmpresa correo,
			final @Named("Para:") String destino,
			final @Optional @Named("Nombre Destinatario:") String nombreDestinatario,
			final @Named("Asunto") String asunto,
			final @Named("Mensaje") String mensaje) {

		return this.configurarEnvio(correo, destino, nombreDestinatario, asunto, mensaje);

	}

	public List<CorreoEmpresa> choices0Send() {
		return this.listarCorreoEmpresa();
	}

	private String configurarEnvio(final CorreoEmpresa correo,
			final String destino, final @Optional String nombreDestinatario,
			final String asunto, final String mensaje) {

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
				PROPERTY_ROOT + "user", correo.getCorreo());
		//FIXME: HAY QUE DECODIFICAR LA CONTRASEÑA? ??? 
		String authenticationPassword = getContainer().getProperty(
				PROPERTY_ROOT + "password", correo.getPass());
		try {

			SimpleEmail simpleEmail = new SimpleEmail();
			simpleEmail.setHostName(smtpHost);
			simpleEmail.setSmtpPort(port);
			simpleEmail.setSSL(true);
			if (authenticationName != null) {
				simpleEmail.setAuthentication(authenticationName,
						authenticationPassword);
			}
			simpleEmail.addTo(destino, nombreDestinatario);
			simpleEmail.setFrom(correo.getCorreo(), "Soporte Tecnico");
			simpleEmail.setSubject(asunto);
			simpleEmail.setMsg(mensaje);
			return simpleEmail.send();

		} catch (EmailException e) {
			throw new servicio.email.EmailException(e.getMessage(), e);
		}
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
		return this.listarCorreoEmpresa();
	}

	/**
	 * Retorna los emails guardados por el usuario registrado.
	 * 
	 * @return List<Correo>
	 */
	@Named("Mensajes Persistidos")
	@MemberOrder(sequence = "3")
	public List<Correo> listarMensajesPersistidos(
			final CorreoEmpresa correoEmpresa) {

		final List<Correo> listaCorreosPersistidos = this.container
				.allMatches(new QueryDefault<Correo>(Correo.class,
						"buscarCorreo"));
		if (listaCorreosPersistidos.isEmpty()) {
			this.container
					.warnUser("No hay Correos Electronicos guardados en el sistema.");
		}
		return listaCorreosPersistidos;
	}

	@Programmatic
	public List<CorreoEmpresa> choices0ListarMensajesPersistidos() {
		return this.listarCorreoEmpresa();
	}

	private List<Correo> accion(final CorreoEmpresa correoEmpresa)
			throws EncriptaException {
		try {
			String contenidoMail = "";
			this.container.warnUser("ACCION");
			List<Correo> retorno = new ArrayList<Correo>();
			store = session.getStore("pop3");
			// System.out.println(" %%%&& PASS DE LA BD "
			// + correoEmpresa.getPass());
			// String clave = "TODOS LOS SABADOS EN CASA DE EXE";
			// Encripta encripta = new Encripta(clave);
			// String pass = encripta.desencripta(correoEmpresa.getPass());
			// System.out.println("%%%&& PASS DECRYPT " + pass);
			// store.connect("pop.gmail.com", correoEmpresa.getCorreo(), pass);

			store.connect("pop.gmail.com", "inventariohardware@gmail.com",
					"inventario123");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			mensajes = folder.getMessages();
			this.container.warnUser("LLEGA HASTA LA LINEA 270");

			for (Message mensaje : mensajes) {
				try {
					System.out.println("PERROOOO :  : : "
							+ mensaje.getContent().toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				final Correo actual = this.container
						.newTransientInstance(Correo.class);

				actual.setEmail(mensaje.getFrom()[0].toString());
				actual.setAsunto(mensaje.getSubject());
				actual.setFechaActual(mensaje.getSentDate());
				actual.setCorreoEmpresa(correoEmpresa);

				// if (mensaje.isMimeType("multipart/*")) {
				Multipart multi;
				try {
					multi = (Multipart) mensaje.getContent();
					// Extraemos cada una de las partes.
					for (int j = 0; j < multi.getCount(); j++) {
						Part unaParte = multi.getBodyPart(j);

						// Volvemos a analizar cada parte de la MultiParte
						// if (unaParte.isMimeType("text/plain")) {
						contenidoMail = unaParte.getContent().toString();
						System.out.println("&&&&&&&&  &&  Mensaje "
								+ contenidoMail);
						// }
						if (contenidoMail != null)
							actual.setMensaje(contenidoMail);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// }
				this.container.warnUser("LLEGA HASTA LA LINEA 304");

				// analizaParteDeMensaje(mensaje);
				// if (contenidoMail.length() < 255) {
				// }
				this.container.persistIfNotAlready("Mensaje: "
						+ actual.getMensaje());
				retorno.add(actual);
			}
			// Cierre de la sesión
			store.close();
			return retorno;

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// private void analizaParteDeMensaje(Part unaParte) {
	// try {
	//
	// if (mensajes[i].isMimeType("multipart/*")) {
	// // Obtenemos el contenido, que es de tipo MultiPart.
	// Multipart multi;
	// multi = (Multipart) mensajes[i].getContent();
	//
	// // Extraemos cada una de las partes.
	// for (int j = 0; j < multi.getCount(); j++) {
	// Part unaParte = multi.getBodyPart(j);
	//
	// // Volvemos a analizar cada parte de la MultiParte
	// if (unaParte.isMimeType("text/plain")) {
	// contenidoMail = unaParte.getContent().toString();
	// }
	// }
	//
	// // // Si es multipart, se analiza cada una de sus partes
	// // // recursivamente.
	// // if (unaParte.isMimeType("multipart/*")) {
	// // Multipart multi;
	// // multi = (Multipart) unaParte.getContent();
	// //
	// // for (int j = 0; j < multi.getCount(); j++) {
	// // analizaParteDeMensaje(multi.getBodyPart(j));
	// // }
	// // } else {
	// // // Si es texto, se escribe el texto.
	// // if (unaParte.isMimeType("text/plain")) {
	// // contenidoMail = unaParte.getContent().toString();
	// // System.out.println("Texto " + unaParte.getContentType());
	// // System.out.println(unaParte.getContent());
	// // System.out.println("---------------------------------");
	// // } else {
	// // // Si es imagen, se guarda en fichero y se visualiza en
	// // // JFrame
	// // if (unaParte.isMimeType("image/*")) {
	// // System.out.println("Imagen "
	// // + unaParte.getContentType());
	// // System.out.println("Fichero=" + unaParte.getFileName());
	// // System.out.println("---------------------------------");
	// //
	// // // salvaImagenEnFichero(unaParte);
	// // // visualizaImagenEnJFrame(unaParte);
	// // }
	// // }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Crea un nuevo correo de la empresa.
	 * 
	 * @param correo
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws EncriptaException
	 */
	@Named("Configurar Correo")
	public CorreoEmpresa crearCorreoEmpresa(
			@Named("Correo") final String correo,
			@Named("Password") final String password)
			throws NoSuchAlgorithmException, IOException, EncriptaException {

		return configuracionCorreo(correo, password);
	}

	private CorreoEmpresa configuracionCorreo(final String correo,
			final String pass) throws NoSuchAlgorithmException, IOException,
			EncriptaException {

		CorreoEmpresa correoempresa = newTransientInstance(CorreoEmpresa.class);

		String clave = "TODOS LOS SABADOS EN CASA DE EXE";
		Encripta encripta = new Encripta(clave);

		correoempresa.setCorreo(correo);
		correoempresa.setPass(encripta.encriptaCadena(pass));
		this.container.persistIfNotAlready(correoempresa);

		return correoempresa;
	}

	@Named("Listar Correos")
	public List<CorreoEmpresa> listarCorreoEmpresa() {
		return this.container.allMatches(new QueryDefault<CorreoEmpresa>(
				CorreoEmpresa.class, "listar"));
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
