package servicio.reporte;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;

import dom.computadora.ComputadoraRepositorio;

public class ComputadorasPorTecnico {
	
	// //////////////////////////////////////
	// Tecnico
	// //////////////////////////////////////
	private String tecnico;

	@MemberOrder(sequence = "1")
	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(final String tecnico) {
		this.tecnico = tecnico;
	}
	
	// {{ Apellido (property)
	private String apellido;

	@MemberOrder(sequence = "1")
	public String getApellido() {
		return apellido;
	}

	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	// }}

	// {{ Nombre (property)
	private String nombre;

	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
	// }}
	
	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////
	
	@javax.inject.Inject
	DomainObjectContainer container;
	
	@javax.inject.Inject
	ComputadoraRepositorio aluRepositorio;
	
	@javax.inject.Inject
	GenerarReporte reporte;	
}
