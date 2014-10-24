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

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;

public class Envio {

	private Session session;
	private Properties propiedades = new Properties();
	private CorreoEmpresa correoEmp=new CorreoEmpresa();
	
	/**
	 * Setea la sesion para poder enviar el correo electronico.
	 * 
	 * @param propiedades
	 */
	public void setSession(Properties propiedades) {

		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	/**
	 * Retorna la sesion creada.
	 * 
	 * @return Session
	 */
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}

	/**
	 * Setea propiedades para poder enviar el correo electronico 
	 * al destinatario.
	 * Puerto, Host, Correo, etc.
	 * 
	 */
	public void setProperties(CorreoEmpresa correoEmpresa) {
		
		correoEmp=correoEmpresa;
		
		// Nombre del host de correo, es smtp.gmail.com
		propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		propiedades.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		propiedades.setProperty("mail.smtp.port", "587");

		// Nombre del usuario
//		propiedades.setProperty("mail.smtp.user", "projecttypes@gmail.com");
		propiedades.setProperty("mail.smtp.user",
				correoEmp.getCorreo());

		// Si requiere o no usuario y password para conectarse.
		propiedades.setProperty("mail.smtp.auth", "true");

		setSession(propiedades);

	}

	/**
	 * Retorna las propiedades seteadas para poder enviar el mail.
	 * 
	 * @return Properties
	 */
	public Properties getProperties() {
		return propiedades;
	}

	/**
	 * Env&iacute;a el mail al cliente, para responder su solicitud.
	 * 
	 * @param mensaje
	 * @param direccion
	 * @throws EncriptaException 
	 */
	public void enviar(String mensaje, String direccion) throws EncriptaException {
		MimeMessage message = new MimeMessage(getSession());
		Multipart multiPart = new MimeMultipart("alternative");
		try {
//			message.setFrom(new InternetAddress(
//					"rentacarPDF@gmail.com"));
			
			message.setFrom(new InternetAddress(
					correoEmp.getCorreo()));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					direccion));
			message.setSubject("RentacarPDF - Consulta");

			String firma="Seguimos viajando a su lado....<b>rentacarPDF.</b>";
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			 			
			Object html = "<a style='font-size:12px;'>"+mensaje.toString()+"</a> " +
			"<p><img src='http://es.gravatar.com/userimage/58275124/6d3364b689fcc936b61fb0b15fcece70.png?size=200' </img></p>"+
			"<p align=center style='font-size:12px;'><font color=#279898>"+firma.toString()+"</font></p>";

			htmlPart.setContent(html, "text/html; charset=utf-8");

			multiPart.addBodyPart(htmlPart);
			message.setContent(multiPart);

			Transport t = session.getTransport("smtp");
			System.out.println("### CORREO  ENVIO::: "+correoEmp.getCorreo());
			System.out.println("### PASS ENCRIPT ENVIO::: "+correoEmp.getPass());
			
			String clave="LAS AVES VUELAN LIBREMENTE";
            Encripta encripta=new Encripta(clave);
            
			String pass=encripta.desencripta(correoEmp.getPass());
			System.out.println("### PASS DESENCRIPT ENVIO::: "+correoEmp.getPass());
			t.connect(correoEmp.getCorreo(),pass);
			//t.connect(correoEmp.getCorreo(), "pepito1234");
			t.sendMessage(message, message.getAllRecipients());
 
			t.close();
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
