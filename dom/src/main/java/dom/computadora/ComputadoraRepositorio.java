package dom.computadora;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
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
						final @Named("Direccion Ip") String ip, 
						final @Named("Mother") String mother, 
						final @Named("Procesador")String procesador,
						final @Named("Disco") String disco,
						final @Named("Memoria")String memoria) {
		return nuevaComputadora(ip, mother, procesador, disco, memoria,this.currentUserName());
	}
	
	@Programmatic
	public Computadora nuevaComputadora(
						final String ip,
						final String mother,
						final String procesador,
						final String disco,
						final String memoria,
						final String creadoPor){
		final Computadora unaComputadora = container.newTransientInstance(Computadora.class);
		unaComputadora.setIp(ip);
		unaComputadora.setMother(mother);
		unaComputadora.setProcesador(procesador);
		unaComputadora.setDisco(disco);
		unaComputadora.setMemoria(memoria);
		unaComputadora.setHabilitado(true);
		unaComputadora.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unaComputadora);
		container.flush();
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
	
	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Computadora> listar() {
		final List<Computadora> listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"eliminarComputadoraTrue", "creadoPor", this
								.currentUserName()));
		if (listaComputadoras.isEmpty()) {
			this.container.warnUser("No hay Computadoras cargados en el sistema.");
		}
		return listaComputadoras;
	}
	
	// //////////////////////////////////////
	// Buscar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "30")
	public List<Computadora> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Ip") @MinLength(2) String apellido) {
		final List<Computadora> listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"buscarPorIp", "creadoPor", this
								.currentUserName(), "ip", apellido
								.toUpperCase().trim()));
		if (listaComputadoras.isEmpty())
			this.container
					.warnUser("No se encontraron Computadoras cargados en el sistema.");
		return listaComputadoras;
	}		
		
	@Programmatic
	public List<Computadora> autoComplete(final String ip) {
		return container.allMatches(new QueryDefault<Computadora>(Computadora.class,
				"autoCompletePorComputadora", "creadoPor", this.currentUserName(),
				"ip", ip.toUpperCase().trim()));
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
