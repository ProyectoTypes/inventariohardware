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
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;

import com.google.common.base.Objects;

@DomainService(menuOrder = "20")
@Named("CORREO")
public class CorreoRepositorio extends AbstractFactoryAndRepository {

	static SecretKey key;

	@Hidden
	public static SecretKey getKey() {
		return key;
	}

	static CorreoEmpresa ce;

	@Hidden
	public static CorreoEmpresa getCorreoEmpresa() {
		return ce;
	}

	// //////////////////////////////////////
	// Tecnico Actual
	// //////////////////////////////////////

	protected boolean creadoPorActualTecnico(final Correo m) {
		return Objects.equal(m.getTecnico(), tecnicoActual());
	}

	protected String tecnicoActual() {
		return getContainer().getUser().getName();
	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */
	public String iconName() {
		return "config";
	}

	// //////////////////////////////////////
	// Configuracion
	// //////////////////////////////////////
	/**
	 * Permite configurar una nueva cuenta de correo electronico.
	 * 
	 * @param correo
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws EncriptaException
	 */
	@Named("Configuracion")
	@MemberOrder(sequence = "1")
	public CorreoEmpresa configuracion(@Named("Correo") final String correo,
			@Named("Password") final String password)
			throws NoSuchAlgorithmException, IOException, EncriptaException {

		return configuracionCorreo(correo, password);
	}

	private CorreoEmpresa configuracionCorreo(final String correo,
			final String pass) throws NoSuchAlgorithmException, IOException,
			EncriptaException {

		CorreoEmpresa ce = newTransientInstance(CorreoEmpresa.class);
		
		// key = KeyGenerator.getInstance("DES").generateKey();
		// EncriptarToString enString=new EncriptarToString();
		String clave = "TODOS LOS SABADOS EN CASA DE EXE";
		Encripta encripta = new Encripta(clave);

		ce.setCorreo(correo);
		ce.setPass(encripta.encriptaCadena(pass));
		persistIfNotAlready(ce);

		return ce;
	}

	// //////////////////////////////////////
	// Bandeja de Entrada
	// //////////////////////////////////////
	/**
	 * @return Retorna la lista de correos persistidos
	 * @throws EncriptaException
	 */
	@Named("Bandeja de Entrada")
	@MemberOrder(sequence = "2")
	public List<Correo> bde(@Named("Correo") CorreoEmpresa correoEmpresa)
			throws EncriptaException {
		System.out.println("ANTES DE LA BUSQUEDA " + correoEmpresa.getCorreo());
		System.out.println("ANTES DE LA BUSQUEDA " + correoEmpresa.getPass());

		Recibe recepcion = new Recibe();
		recepcion.setProperties(correoEmpresa);
		recepcion.accion();

		final List<Correo> listaJavaMail = recepcion.getListaMensajes();

		String mensajeNuevos = listaJavaMail.size() == 1 ? "TIENES UN NUEVO CORREO!"
				: "TIENES " + listaJavaMail.size() + " CORREOS NUEVOS";

		if (listaJavaMail.size() > 0) {

			getContainer().informUser(mensajeNuevos);

			for (Correo mensaje : listaJavaMail) {

				final Correo mensajeTransient = newTransientInstance(Correo.class);
				if (existeMensaje(mensaje.getAsunto()) == null) {
					mensajeTransient.setEmail(mensaje.getEmail());
					mensajeTransient.setAsunto(mensaje.getAsunto());
					mensajeTransient.setMensaje(mensaje.getMensaje());
					mensajeTransient.setTecnico(tecnicoActual());
					mensajeTransient.setCorreoEmpresa(correoEmpresa);
					mensajeTransient.setFechaActual(mensaje.getFechaActual());
					persistIfNotAlready(mensajeTransient);
				}
			}

		}
		return listarMensajesPersistidos(correoEmpresa);
	}

	// //////////////////////////////////////
	// Mensajes Persistidos
	// //////////////////////////////////////
	/**
	 * Retorna los emails guardados por el usuario registrado.
	 * 
	 * @return List<Correo>
	 */
	@Named("Mensajes Persistidos")
	@MemberOrder(sequence = "3")
	public List<Correo> listarMensajesPersistidos(final CorreoEmpresa correoEmpresa) {
		final List<Correo> listaCorreosPersistidos = this.container
				.allMatches(new QueryDefault<Correo>(Correo.class, "buscarCorreo"));
		if (listaCorreosPersistidos.isEmpty()) {
			this.container.warnUser("No hay Correos Electronicos guardados en el sistema.");
		}
		return listaCorreosPersistidos;
	}
	public List<Correo> choices0ListarMensajesPersistidos() {
		return this.listarCorreos();
	}

	private List<Correo> listarCorreos() {
		return container.allMatches(new QueryDefault<Correo>(Correo.class, "buscarCorreo"));
	}

	/**
	 * Corrobora si ya esta persistido el correo en nuestra BD Busqueda.
	 * 
	 * @param mail
	 * @return Correo
	 */
	@Programmatic
	public Correo existeMensaje(final String mail) {
		Correo correo = this.container.firstMatch(new QueryDefault<Correo>(Correo.class, "buscarCorreo", "mail", mail));
		if(correo == null){
			this.container.warnUser("El mensaje no se encuentra guardado.");
		}
		return correo;
	}

	@Hidden
	public CorreoEmpresa listaConfiguracion(final CorreoEmpresa correoEmpresa) {
		return correoEmpresa;
	}
	
	public List<CorreoEmpresa> choices0ListaConfiguracion() {
		return this.listarConfig();
	}
	
	private List<CorreoEmpresa> listarConfig() {
		return container.allMatches(new QueryDefault<CorreoEmpresa>(CorreoEmpresa.class, "listar"));
	}

	/**
	 * Accion de Autocompletado generada por el framework, retorna una lista de
	 * los objetos de la entidad.
	 *
	 * @param correo
	 * 
	 * @return List<CorreoEmpresa>
	 */	
	@Programmatic
	public List<Correo> autoComplete(final String buscarCorreo) {
		return container.allMatches(new QueryDefault<Correo>(Correo.class,
				"buscarCorreo", "creadoPor", this
						.currentUserName(), "nombreSector", buscarCorreo
						.toUpperCase().trim()));
	}
	
	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
}
