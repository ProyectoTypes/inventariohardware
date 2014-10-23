package dom.correo;

import java.util.Date;
import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import servicio.encriptar.EncriptaException;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name = "buscarCorreo", language = "JDOQL", value = "SELECT "
			+ "FROM dom.correo.Correo "
			+ "WHERE habilitado == true")})
@ObjectType("CORREO")
@Immutable
public class Correo implements Comparable<Correo> {
	
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////
	
	public String iconName() {
		return (isRespondido()==false)? "mail": "respondido";
	}

	public String title(){
		return "Correo";
	}
 
	// //////////////////////////////////////
	// Asunto (propiedad)
	// //////////////////////////////////////
	
	private String asunto;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Asunto")
	@MemberOrder(sequence = "1")
	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(final String asunto) {
		this.asunto = asunto;
	}
	
	// //////////////////////////////////////
	// Email (propiedad)
	// //////////////////////////////////////
	
	private String email;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Email")
	@MemberOrder(sequence = "2")
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
	
	// //////////////////////////////////////
	// Fecha (propiedad)
	// //////////////////////////////////////
	
	private Date fechaActual;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Named("Fecha del Correo")
	@MemberOrder(sequence = "3")
	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(final Date fechaActual) {
		this.fechaActual = fechaActual;
	}
	
	// //////////////////////////////////////
	// Respondido (propiedad)
	// //////////////////////////////////////
	
	private boolean respondido;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Hidden(where=Where.ALL_TABLES)
	public boolean isRespondido(){
		return respondido;
	}
	public void setRespondido(boolean respondido){
		this.respondido=respondido;
	}

	// //////////////////////////////////////
	// Mensaje (propiedad)
	// //////////////////////////////////////
	
	private String mensaje;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines = 6)
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}
	
	// //////////////////////////////////////
	// Correo Empresa (propiedad)
	// //////////////////////////////////////
	
	private CorreoEmpresa correoEmpresa;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Hidden
	public CorreoEmpresa getCorreoEmpresa(){
		return correoEmpresa;
	}
	public void setCorreoEmpresa(CorreoEmpresa correoEmpresa){
		this.correoEmpresa=correoEmpresa;
	}
	
	// //////////////////////////////////////
	// Tecnico (propiedad)
	// //////////////////////////////////////
	
	private String usuario;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Hidden
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Se puede responder el correo directamente desde el viewer.
	 * 
	 * @param mensaje
	 *            
	 * @return Correo
	 * @throws EncriptaException 
	 */
	public Correo Responder(
			@MultiLine(numberOfLines = 6) @Named("Mensaje") String mensaje) throws EncriptaException {

		Envio correo = new Envio();
		correo.setProperties(getCorreoEmpresa());
		setRespondido(true);
		correo.enviar(mensaje,this.getEmail());
		container.informUser("El mensaje ha sido enviado con éxito!");
		return this;
	}

	/**
	 * Permite borrar el correo electronico desde la UI
	 * Devuelve la lista de correos.
	 * @return List<Correo>
	 */
	@Named("Borrar")
	@Bulk
	public List<Correo> borrar() {
		// Borramos el/los objeto/s seleccionado/s
		container.removeIfNotAlready(this);
		// Vuelvo a la bandeja de entrada
		return bde.listaMensajesPersistidos2();
	}
	
	/**
	 * Se ordenan los correos por fecha de ingreso.
	 */
	@Override
	public int compareTo(Correo mensaje) { 
		return this.fechaActual.compareTo(mensaje.getFechaActual());
	}
	
	// //////////////////////////////////////
	// Container
	// //////////////////////////////////////
	
	private DomainObjectContainer container;

	public void injectDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}
	
	private CorreoServicio bde; 
	public void injectServicioBandejaDeEntrada(final CorreoServicio bde) {
		this.bde = bde;
	}
}
