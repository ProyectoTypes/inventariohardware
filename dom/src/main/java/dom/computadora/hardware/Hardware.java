package dom.computadora.hardware;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;

public class Hardware {
	// //////////////////////////////////////
	// Fabricante (propiedad)
	// //////////////////////////////////////

	private String fabricante;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Fabricante del Hardware:")
	@MemberOrder(sequence = "20")
	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(final String fabricante) {
		this.fabricante = fabricante;
	}
}
