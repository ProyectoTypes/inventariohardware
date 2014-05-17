package dom.persona;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;

import org.joda.time.LocalDate;

@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)

public abstract class Persona{
	
    // //////////////////////////////////////
    // APELLIDO (propiedad)
    // //////////////////////////////////////
	
	private String apellido;	

	@DescribedAs("Apellido de la Persona:")
	@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@MemberOrder(sequence = "1")
	public String getApellido(){
		return apellido;
	}
	public void setApellido(final String apellido){
		this.apellido = apellido;
	}
	
    // //////////////////////////////////////
    // NOMBRE (propiedad)
    // //////////////////////////////////////
	
	private String nombre;
	
	@DescribedAs("Nombre de la Persona:")
	@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@MemberOrder(sequence = "2")
	public String getNombre(){
		return nombre;
	}
	public void setNombre(final String nombre){
		this.nombre = nombre;
	}
    
    // //////////////////////////////////////
    // DOCUMENTO (propiedad)
    // //////////////////////////////////////
	
	private String documento;
	
	@Hidden(where = Where.ALL_TABLES)
	@RegEx(validation="\\d{6,8}")
	@MemberOrder(sequence = "3")
	public String getDocumento(){
		return documento;
	}
	public void setDocumento(final String documento){
		this.documento = documento;
	}
	
    // //////////////////////////////////////
    // FECHA DE NACIMIENTO (propiedad)
    // //////////////////////////////////////
	
	private LocalDate fechaNacimiento;
	
	@Hidden(where = Where.ALL_TABLES)
	@DescribedAs("Fecha de nacimiento de la Persona:")
	@MemberOrder(sequence = "4")
	public LocalDate getFechaNacimiento(){
		return fechaNacimiento;
	}
	public void setFechaNacimiento(final LocalDate fechaNacimiento){
		this.fechaNacimiento = fechaNacimiento;
	}
	
    // //////////////////////////////////////
    // EMAIL (propiedad)
    // //////////////////////////////////////
	
	private String email;
	
	@RegEx(validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	@MemberOrder(sequence = "5")
	public String getEmail(){
		return email;
	}
	public void setEmail(final String email){
		this.email = email;
	}
	
    // //////////////////////////////////////
    // CELULAR (propiedad)
    // //////////////////////////////////////
	
	private int celular;

	@Hidden(where = Where.ALL_TABLES)
	@DescribedAs("Celular del Cliente:")
	@RegEx(validation = "^[0-9]{2,3}-? ?[0-9]{6,7}$")	
	@MemberOrder(sequence = "6")
	public int getCelular(){
		return celular;
	}	
	public void setCelular(final int celular){
		this.celular = celular;
	}
	
}