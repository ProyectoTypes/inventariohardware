package dom.computadora;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@ObjectType("COMPUTADORA")
@Audited
@AutoComplete(repository=ComputadoraRepositorio.class, action="autoComplete")
@Bookmarkable

public class Computadora {
	
	// //////////////////////////////////////
	// IP (propiedad)
	// //////////////////////////////////////

	private int ip;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Direccion IP de la Computadora:")
	@MemberOrder(sequence = "10")
	public int getIp() {
		return ip;
	}

	public void setIp(final int ip) {
		this.ip = ip;
	}
	
	// //////////////////////////////////////
	// Mother (propiedad)
	// //////////////////////////////////////

	private String mother;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Mother de la Computadora:")
	@MemberOrder(sequence = "20")
	public String getMother() {
		return mother;
	}

	public void setMother(final String mother) {
		this.mother = mother;
	}
	
	// //////////////////////////////////////
	// Procesador (propiedad)
	// //////////////////////////////////////

	private String procesador;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Procesador de la Computadora:")
	@MemberOrder(sequence = "30")
	public String getProcesador() {
		return procesador;
	}

	public void setProcesador(final String procesador) {
		this.procesador = procesador;
	}
	
	// //////////////////////////////////////
	// Disco (propiedad)
	// //////////////////////////////////////

	private String disco;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Disco de la Computadora:")
	@MemberOrder(sequence = "40")
	public String getDisco() {
		return disco;
	}

	public void setDisco(final String disco) {
		this.disco = disco;
	}
	
	// //////////////////////////////////////
	// Memoria (propiedad)
	// //////////////////////////////////////

	private String memoria;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Memoria de la Computadora:")
	@MemberOrder(sequence = "50")
	public String getMemoria() {
		return memoria;
	}

	public void setMemoria(final String memoria) {
		this.memoria = memoria;
	}

}
