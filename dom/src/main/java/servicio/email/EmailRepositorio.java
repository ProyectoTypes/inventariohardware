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

import org.apache.isis.applib.annotation.RegEx;

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
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotContributed.As;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.jsoup.Jsoup;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;
import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;

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
@DomainService
@Named("Casilla de Correo")
public class EmailRepositorio extends AbstractFactoryAndRepository {
	private static final String PROPERTY_ROOT = "mail.smtp.";
	private static String contenidoMail;

	public String iconName() {
		return "Correo";
	}
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

	/* ******************** CONFIGURACION SESSION ************************ */

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
	
	/* ******************** ENVIAR EMAIL *********************** */

	/**
	 * Envia un correo despues de haber terminado el soporte tecnico.
	 * 
	 * @param unaComputadora
	 * @return
	 * @throws EncriptaException 
	 */
	@NotContributed(As.ASSOCIATION)
	@NotInServiceMenu
	@Named("Enviar Correo")
	@DescribedAs("Finalizado la reparacion del equipo se envia un correo al usuario.")
	public String send(final Computadora unaComputadora) throws EncriptaException {
		String asunto = "Servicio Tecnico Finalizado.";
		String destino = unaComputadora.getUsuario().getEmail();
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
		 this.configurarEnvio(null, destino, asunto, mensaje);
		 return "Mensaje enviado correctamente.";
	}
	@Named("Computadora")
	@DescribedAs("Buscar el Computadora en mayuscula")
	public List<Computadora> autoComplete0Send(final @MinLength(2) String search) {
		return computadoraRepositorio.autoComplete(search);

	}

	@Named("Enviar Correo")
	@DescribedAs("Envia mensajes personalizados.")
	public List<Correo> send(final @Named("De: ") CorreoEmpresa correo,
			final @Named("Para:") String destino,
			final @Named("Asunto") String asunto,
			final @MultiLine(numberOfLines = 4) @Named("Mensaje") String mensaje) throws EncriptaException {

		return this.configurarEnvio(correo, destino, asunto, mensaje);

	}

	public List<CorreoEmpresa> choices0Send() {
		return this.listarCorreoEmpresa();
	}

	private List<Correo> configurarEnvio(final CorreoEmpresa correo,
			final String destino, final String asunto, final String mensaje) throws EncriptaException {

		String host = "smtp.gmail.com";
		String puertoEntrante = "995";
		String puertoSaliente = "587";//saliente
		String email = "inventariohardware@gmail.com";
		String pass = "inventario123";
		if (correo != null) {
//			host = correo.getHost();
//			puertoSaliente = correo.getPortSaliente();
//			puertoEntrante = correo.getPortEntrante();
			email = correo.getCorreo();
			pass = correo.getPass();
			String clave = "TODOS LOS SABADOS EN CASA DE EXE";
			Encripta encripta = new Encripta(clave);
			pass = encripta.desencripta(correo.getPass());
		}
		System.out.println(host+" - "+puertoSaliente +" - "+ email +" - "+pass);
		/*
		 * Configuracion para enviar email.
		 */
		String smtpHost = getContainer().getProperty(PROPERTY_ROOT + "host",
				host);

		String portValue = getContainer().getProperty(PROPERTY_ROOT + "port",
				puertoSaliente);

		int port = Integer.valueOf(portValue).intValue();

		String authenticationName = getContainer().getProperty(
				PROPERTY_ROOT + "user", email);
		String authenticationPassword = getContainer().getProperty(
				PROPERTY_ROOT + "password", pass);

		try {

			SimpleEmail simpleEmail = new SimpleEmail();
			simpleEmail.setHostName(smtpHost);
			simpleEmail.setSmtpPort(port);
			simpleEmail.setSSL(true);
			if (authenticationName != null) {
				simpleEmail.setAuthentication(authenticationName,
						authenticationPassword);
			}
			simpleEmail.addTo(destino);
			simpleEmail.setFrom(email, "Soporte Tecnico");
			simpleEmail.setSubject(asunto);
			simpleEmail.setMsg(mensaje);
			 simpleEmail.send();
			 return this.listarMensajesPersistidos(correo);
		} catch (EmailException e) {
			throw new servicio.email.EmailException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unused")
	private String desencriptar(final String contrasenia) {
		String clave = "TODOS LOS SABADOS EN CASA DE EXE";
		Encripta encripta = new Encripta(clave);
		String pass = "";
		try {
			pass = encripta.desencripta(contrasenia);
		} catch (EncriptaException e) {
			e.printStackTrace();
		}
		return pass;
	}

	/* ******************** BANDEJA DE ENTRADA ************************ */
	
	/**
	 * 
	 * @return Retorna la lista de correos persistidos
	 * @throws EncriptaException
	 */
	@Named("Bandeja de Correo")
	@MemberOrder(sequence = "2")
	public List<Correo> bandeja(
			final @Named("Correo") CorreoEmpresa correoEmpresa)
			throws EncriptaException {

		this.setProperties();//Se setean las propiedades para recibir los correos desde gmail.

		final List<Correo> listaJavaMail = this.accion(correoEmpresa);
		if(listaJavaMail!=null)
		{
			String mensajeNuevos = listaJavaMail.size() == 1 ? "TIENES UN NUEVO CORREO!"
				: "TIENES " + listaJavaMail.size() + " CORREOS NUEVOS";

			if (listaJavaMail != null && listaJavaMail.size() > 0)
				this.container.warnUser(mensajeNuevos);
			else
				this.container.warnUser("NO HAY NUEVO CORREO");
			return listarMensajesPersistidos(correoEmpresa);
		}
		else {
			this.container.warnUser("ERROR al conectarse a "+correoEmpresa.getCorreo());
			return null;
		}
			
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
	@Programmatic
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
			List<Correo> retorno = new ArrayList<Correo>();
			store = session.getStore("pop3");
			String clave = "TODOS LOS SABADOS EN CASA DE EXE";
			Encripta encripta = new Encripta(clave);
			String pass = encripta.desencripta(correoEmpresa.getPass());
			
			store.connect("pop.gmail.com", correoEmpresa.getCorreo(), pass);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			mensajes = folder.getMessages();

			for (Message mensaje : mensajes) {
				final Correo actual = this.container
						.newTransientInstance(Correo.class);

				actual.setEmail(this.limpiarDireccionCorreo(mensaje.getFrom()[0]
						.toString()));
				actual.setAsunto(mensaje.getSubject());
				actual.setFechaActual(mensaje.getSentDate());
				actual.setCorreoEmpresa(correoEmpresa);
				actual.setTecnico(this.currentUserName());
				analizaParteDeMensaje(mensaje);

				if (contenidoMail.length() < 255) {
					String mje = this.html2text(contenidoMail);
					actual.setMensaje(mje);
				}
				this.container.persistIfNotAlready(actual);
				retorno.add(actual);
			}
			store.close();
			return retorno;

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo recursivo. Si la parte que se pasa es compuesta, se extrae cada
	 * una de las subpartes y el metodo se llama a si mismo con cada una de
	 * ellas. Si la parte es un text, se escribe en pantalla. Si la parte es una
	 * image, se guarda en un fichero y se visualiza en un JFrame. En cualquier
	 * otro caso, simplemente se escribe el tipo recibido, pero se ignora el
	 * mensaje.
	 * 
	 * @param unaParte
	 *            Parte del mensaje a analizar.
	 */
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
				if (unaParte.isMimeType("text/*")) {
					contenidoMail = unaParte.getContent().toString();
				} else {
					// Si es imagen, se guarda en fichero y se visualiza en
					// JFrame
					if (unaParte.isMimeType("image/*")) {

						contenidoMail += unaParte.getContentType().toString();
						// this.salvaImagenEnFichero(unaParte);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Programmatic
	public String html2text(String html) {
		return Jsoup.parse(html).text();
	}

	private String limpiarDireccionCorreo(final String direccion) {
		// =?UTF-8?Q?daniel_mu=C3=B1oz?= <cipoleto@gmail.com>
		String[] arreglo = direccion.split("<");
		return arreglo[1].replace(">", "");
	}

	/* ******************* CORREO EMPRESA ************************ */

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
	@MemberOrder(sequence = "1")
	public CorreoEmpresa crearCorreoEmpresa(
			@Named("Correo") @RegEx(validation ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")final String correo,
			@Named("Password") final String password)
			throws NoSuchAlgorithmException, IOException, EncriptaException {

		return configuracionCorreo(correo, password);
	}

//	public String default2CrearCorreoEmpresa() {
//		return "smtp.gmail.com";
//	}
//
//	public String default3CrearCorreoEmpresa() {
//		return "587";
//	}
//	public String default4CrearCorreoEmpresa() {
//		return "995";
//	}

	private CorreoEmpresa configuracionCorreo(final String correo,
			final String pass)
			throws NoSuchAlgorithmException, IOException, EncriptaException {

		CorreoEmpresa correoempresa = newTransientInstance(CorreoEmpresa.class);

		String clave = "TODOS LOS SABADOS EN CASA DE EXE";
		Encripta encripta = new Encripta(clave);

		correoempresa.setCorreo(correo);
		correoempresa.setPass(encripta.encriptaCadena(pass));
//		correoempresa.setHost(host);
//		correoempresa.setPortSaliente(portSaliente);
//		correoempresa.setPortEntrante(portEntrante);

		this.container.persistIfNotAlready(correoempresa);

		return correoempresa;
	}

	@Named("Listar Correo Electronico")
	@MemberOrder(sequence = "100")
	public List<CorreoEmpresa> listarCorreoEmpresa() {
		return this.container.allMatches(new QueryDefault<CorreoEmpresa>(
				CorreoEmpresa.class, "listar"));
	}

	/* ******************* FIN: CORREO EMPRESA ************************ */

	/* ******************* DASHBOARD ************************ */
	@Programmatic
	public List<Correo> queryBuscarCorreoPorUsuario() {
		return this.container.allMatches(new QueryDefault<Correo>(Correo.class,
				"buscarCorreoPorUsuario", "usuario", this.currentUserName()));
	}

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
