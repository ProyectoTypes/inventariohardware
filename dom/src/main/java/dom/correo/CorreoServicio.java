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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

import servicio.encriptar.Encripta;
import servicio.encriptar.EncriptaException;

import com.google.common.base.Objects;

@SuppressWarnings("deprecation")
@DomainService(menuOrder="20")
@Named("CORREO")
public class CorreoServicio extends AbstractFactoryAndRepository {
	
	static SecretKey key;
	@Hidden
	public static SecretKey getKey(){
		return key;
	}
	
	static CorreoEmpresa ce;
	@Hidden
	public static CorreoEmpresa getCorreoEmpresa(){
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
	 *  Identificacion del nombre del icono que aparecera en la UI
	 *  @return String
	 */
	public String iconName() {
		return "config";
	}
	
	
	// //////////////////////////////////////
	// Configuracion
	// //////////////////////////////////////
	/**
	 * Permite configurar una nueva cuenta de correo electronico.
	 * @param correo
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws EncriptaException
	 */
	@Named("Configuracion")
	@MemberOrder(sequence = "1")
	public CorreoEmpresa configuracion(
			@Named("Correo") final String correo,
			@Named("Password") final String password) throws NoSuchAlgorithmException, IOException, EncriptaException{
		
		return configuracionCorreo(correo,password);
	}
	private CorreoEmpresa configuracionCorreo(final String correo,final String pass) throws NoSuchAlgorithmException, IOException, EncriptaException{
		
		CorreoEmpresa ce = newTransientInstance(CorreoEmpresa.class);
		
		//key = KeyGenerator.getInstance("DES").generateKey();
		//EncriptarToString enString=new EncriptarToString();
		String clave="TODOS LOS SABADOS EN CASA DE EXE";
		Encripta encripta=new Encripta(clave);
		
		ce.setCorreo(correo);
		ce.setPass(encripta.encriptaCadena(pass));
		persistIfNotAlready(ce);
		
		List<String> listaStringEncriptada=listaConfiguracion(ce);
	 
		//guarda el archivo.
		File file=new File("Correo.txt");
		OutputStream out = new FileOutputStream(file);
		
		for(String al:listaStringEncriptada){
			out.write((al.toString()+"\n").getBytes(Charset.forName("UTF-8")));
		} 
		out.close();		
				
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
	public List<Correo> bde(@Named("Correo") CorreoEmpresa correoEmpresa) throws EncriptaException {
		System.out.println("ANTES DE LA BUSQUEDA "+correoEmpresa.getCorreo());
		System.out.println("ANTES DE LA BUSQUEDA "+correoEmpresa.getPass());
 
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
				if(existeMensaje(mensaje.getAsunto())==null){
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
	 * @return List<Correo>
	 */
	@Named("Mensajes Persistidos")
	@MemberOrder(sequence = "3")
	public List<Correo> listarMensajesPersistidos(final CorreoEmpresa correoEmpresa) {
		final List<Correo> listaCorreosPersistidos = this.container.allMatches(
				new QueryDefault<Correo>(Correo.class,"buscarCorreo"));
		if (listaCorreosPersistidos.isEmpty()) {
			this.container.warnUser("No hay Correos Electronicos guardados en el sistema.");
		}
		return listaCorreosPersistidos;
	}
	
	//BORRAR ESTE LISTAR
	/**
	@Programmatic
	public List<Correo> listaMensajesPersistidos(final CorreoEmpresa correoEmpresa) {

		return allMatches(Correo.class, new Filter<Correo>() {
			@Override
			public boolean accept(final Correo mensaje) {
				return Objects.equal(mensaje.getCorreoEmpresa(), correoEmpresa);
			}
		});
	}
	/*
	
	/**
	 * Retorna los emails guardados por el usuario registrado
	 * @return List<Correo>
	 */
	@Programmatic
	public List<Correo> listaMensajesPersistidos2() {
		return allMatches(Correo.class, new Filter<Correo>() {
			@Override
			public boolean accept(final Correo mensaje) {
				return Objects.equal(mensaje.getCorreoEmpresa(), tecnicoActual() );
			}
		});
	}	
	
	/**
	 * Corrobora si ya esta persistido el correo en nuestra BD
	 * Busqueda.
	 * @param mail
	 * @return Correo
	 */
	@Programmatic
	public Correo existeMensaje(final String mail) {
		return uniqueMatch(Correo.class, new Filter<Correo>() {
			@Override
			public boolean accept(Correo correo) {
				return correo.getAsunto().equals(mail);
			}
		});
	}	
	
	@Hidden
	public List<String> listaConfiguracion(CorreoEmpresa correoEmpresa){
		List<String> listaConfiguracion=new ArrayList<String>();
		
		if (correoEmpresa != null) {
			
			listaConfiguracion.add(correoEmpresa.getCorreo());
			listaConfiguracion.add(correoEmpresa.getPass().toString());
		}

		// Persistir en el archivo

		return listaConfiguracion;
	}
	
	/**
	 * Busqueda de Configuracion del Correo.
	 * Se retorna un objeto CorreoEmpresa, que contiene el correo y clave.
	 * 
	 * @param ce
	 * 
	 * @return CorreoEmpresa
	 */
	@Hidden
	@MemberOrder(sequence = "2")
	@Named("buscarConfig")
    public CorreoEmpresa buscarConfiguracion(final CorreoEmpresa ce) {
	        return uniqueMatch(CorreoEmpresa.class,new Filter<CorreoEmpresa>() {
	        	public boolean accept(final CorreoEmpresa a){
	        		return a.equals(ce);
	        	}
			});
	    }
	

	/**
	 * Busqueda de Configuracion del Correo.
	 * Se retorna un objeto CorreoEmpresa, que contiene el correo y clave.
	 * Se busca por el correo.
	 * 
	 * @param correo
	 * 
	 * @return CorreoEmpresa
	 */
	@Hidden
	@MemberOrder(sequence = "2")
	@Named("buscarConfig")
    public CorreoEmpresa buscarConfiguracionPorEmail(final String correo) {
	        return uniqueMatch(CorreoEmpresa.class,new Filter<CorreoEmpresa>() {
	        	public boolean accept(final CorreoEmpresa a){
	        		return a.getCorreo().contains(correo);
	        	}
			});
	    }
	
	
	/**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param correo
     * 
     * @return List<CorreoEmpresa>
     */
	@Programmatic
	public List<Correo> autoComplete(final String correo) {
		return container.allMatches(new QueryDefault<Correo>(Correo.class, "buscarCorreo"));
	}
	
	//BORRAR ESTE AUTOCOMPLETE
	/**
	@Hidden    
	public List<CorreoEmpresa> autoComplete(final String correo) {
		return allMatches(CorreoEmpresa.class, new Filter<CorreoEmpresa>() {
		@Override
		public boolean accept(final CorreoEmpresa t) {		
		return t.getCorreo().contains(correo) ; 
		}
	  });				
	}
	*/
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
}
