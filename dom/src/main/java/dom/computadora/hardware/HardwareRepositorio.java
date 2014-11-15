package dom.computadora.hardware;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

@DomainService
@Named("Hardware")
public class HardwareRepositorio {
	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "hardware";
	}

	public String iconName() {
		return "Hardware";
	}

}
