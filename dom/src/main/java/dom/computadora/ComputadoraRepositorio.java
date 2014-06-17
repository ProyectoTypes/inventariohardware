package dom.computadora;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;


@Named("COMPUTADORA")
public class ComputadoraRepositorio {
	
	public ComputadoraRepositorio() {

	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "computadora";
	}

	public String iconName() {
		return "Computadora";
	}
	
	// //////////////////////////////////////
	// Agregar Computadora
	// //////////////////////////////////////
	
	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Computadora addComputadora(
						final @Named("Direccion Ip") int ip, 
						final @Named("Mother") String mother, 
						final @Named("Procesador")String procesador,
						final @Named("Disco") String disco,
						final @Named("Memoria")String memoria) {
		return nuevaComputadora(ip, mother, procesador, disco, memoria);
	}
	
	@Programmatic
	public Computadora nuevaComputadora(
						final int ip,
						final String mother,
						final String procesador,
						final String disco,
						final String memoria){
		final Computadora unaComputadora = container.newTransientInstance(Computadora.class);
		unaComputadora.setIp(ip);
		unaComputadora.setMother(mother);
		unaComputadora.setProcesador(procesador);
		unaComputadora.setDisco(disco);
		unaComputadora.setMemoria(memoria);
		return unaComputadora;
	}
	
	
	// //////////////////////////////////////
	// Buscar Computadora
	// //////////////////////////////////////
	
	//@Named("Computadora")
	@DescribedAs("Buscar el Computadora en mayuscula")
	public List<Computadora> autoComplete0AddComputadora(final @MinLength(2) String search) {
		return computadoraRepositorio.autoComplete(search);

	}
	
	@Programmatic
	public List<Computadora> autoComplete(final String apellido) {
		return container.allMatches(new QueryDefault<Computadora>(Computadora.class,
				"autoCompletePorComputadora", "creadoPor", this.currentUserName(),
				"apellido", apellido.toUpperCase().trim()));
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
	
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;
}
