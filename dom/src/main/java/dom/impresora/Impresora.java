package dom.impresora;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.RegEx;

public class Impresora {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////
	
	public String title(){
		return this.getNombreImpresora();
	}
	
	public String iconName(){
		return "Impresora";
	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	private String nombreImpresora;
	@javax.jdo.annotations.Column(allowsNull= "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Nombre de la Impresora:")
	@MemberOrder(sequence = "10")
	public String getNombreImpresora() {
		return nombreImpresora;
	}

	public void setNombreImprsora(String nombreImpresora) {
		this.nombreImpresora = nombreImpresora;
	}

}
