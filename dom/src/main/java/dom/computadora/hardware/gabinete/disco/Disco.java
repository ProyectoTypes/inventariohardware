package dom.computadora.hardware.gabinete.disco;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@ObjectType("DISCO")
@Audited

public class Disco {
	
	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getMarca();
	}

	public String iconName() {
		return "Disco";
	}

	// //////////////////////////////////////
	// tipo (Atributo)
	// //////////////////////////////////////

	public static enum TipoDisco {
		SATA, IDE, SCSI, SAS;
	}

	private TipoDisco tipo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tipo de disco:")
	@MemberOrder(sequence = "10")
	public TipoDisco getTipo() {
		return tipo;
	}

	public void setTipo(final TipoDisco tipo) {
		this.tipo = tipo;
	}

	// //////////////////////////////////////
	// Tamaño (Atributo)
	// //////////////////////////////////////
	private int tamaño;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Tamano del disco:")
	@MemberOrder(sequence = "20")
	public int getTamaño() {
		return tamaño;
	}

	public void setTamaño(final int tamaño) {
		this.tamaño = tamaño;
	}

	// //////////////////////////////////////
	// Marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del disco:")
	@MemberOrder(sequence = "30")
	public String getMarca() {
		return marca;
	}

	public void setMarca(final String marca) {
		this.marca = marca;
	}
}
