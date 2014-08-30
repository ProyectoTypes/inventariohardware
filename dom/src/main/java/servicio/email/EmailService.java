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

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.NotContributed.As;
import org.apache.isis.applib.annotation.NotInServiceMenu;

import dom.computadora.Computadora;
import dom.computadora.ComputadoraRepositorio;

@DomainService
public class EmailService extends AbstractFactoryAndRepository {
	private static final String PROPERTY_ROOT = "mail.smtp.";

	/*
	 * protected void send(String smtpHost, int smtpPort, String from, String
	 * to, String subject, String content) { try { SimpleEmail email = new
	 * SimpleEmail(); email.setHostName(smtpHost);
	 * email.addTo("rmatthews@isis.apache.org", "John Doe");
	 * email.setFrom("me@apache.org", "Me"); email.setSubject(subject);
	 * email.setMsg(content); email.send(); } catch (EmailException e) { throw
	 * new org.apache.isis.service.email.EmailException(e.getMessage(), e); } }
	 */
	
	// public Email createAnEmailMessage() {
	// return newTransientInstance(Email.class);
	// }
	//
	// public Address createAnEmailAddress() {
	// return newTransientInstance(Address.class);
	// }

	/**
	 * send() Envio de mensajes, lo ideal seria crear una clase Email.
	 * Condiciones para enviar un correo: La computadora debera ser creada con
	 * un usuario, y debera tener asignado un tecnico cuando pase por Soporte
	 * (Movimiento).
	 * Se debe chequear que los email tanto de Tecnico como de Usuario no deben ser nulos.
	 * Se debe cambiar de estado (Terminado).
	 */
	@NotContributed(As.ASSOCIATION)
	@NotInServiceMenu
	@Named("Enviar Correo")
	public String send(final Computadora unaComputadora) {
		// Address dir = new Address();
		// dir.setEmailAddress("cipoleto@gmail.com");
		// dir.setName("Daniel");
		// Email email = new Email();
		// email.setMessage("Hola mundo");
		// email.setFrom(dir);
		// email.setSubject("None");
		// Address dir2 = new Address();
		// dir2.setEmailAddress("munozda87@hotmail.com");
		// dir2.setName("Munoz");
		// email.addToTo(dir2);
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
		String asunto = "Servicio Tecnico le informa que ....";
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
				PROPERTY_ROOT + "user", "cipoleto@gmail.com");
		String authenticationPassword = getContainer().getProperty(
				PROPERTY_ROOT + "password", "33239335");

		// EN CASO QUE SEA NULL EL CAMPO; SE PONEN LOS SIGUIENTES POR DEFECTO.
		// String fromName = getContainer().getProperty(
		// PROPERTY_ROOT + "from.name", "No reply");
		// String fromEmailAddress = getContainer().getProperty(
		// PROPERTY_ROOT + "from.address", "noreply@domain.com");

		try {

			SimpleEmail simpleEmail = new SimpleEmail();
			simpleEmail.setHostName(smtpHost);
			simpleEmail.setSmtpPort(port);
			if (authenticationName != null) {
				simpleEmail.setAuthentication(authenticationName,
						authenticationPassword);
			}
			simpleEmail.addTo(correoReceptor, nombreReceptor);
			simpleEmail.setFrom(correoEmisor, nombreEmisor);
			simpleEmail.setSubject(asunto);
			simpleEmail.setMsg(mensaje);
			return simpleEmail.send();
			
			// for (Address address : email.getTo()) {
			// String name = address.getName();
			// if (name == null) {
			// simpleEmail.addTo(address.getEmailAddress());
			// } else {
			// simpleEmail.addTo(address.getEmailAddress(), name);
			// }
			// }
			// Address from = email.getFrom();
			// if (from == null) {
			// simpleEmail.setFrom(fromEmailAddress, fromName);
			// } else {
			// simpleEmail.setFrom(from.getEmailAddress(), from.getName());
			// }
			// simpleEmail.setSubject(email.getSubject());
			// simpleEmail.setMsg(email.getMessage());
			// simpleEmail.send();
		} catch (EmailException e) {
			throw new servicio.email.EmailException(e.getMessage(), e);
		}
	}

	@Named("Computadora")
	@DescribedAs("Buscar el Computadora en mayuscula")
	public List<Computadora> autoComplete0Send(final @MinLength(2) String search) {
		return computadoraRepositorio.autoComplete(search);

	}

	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}
