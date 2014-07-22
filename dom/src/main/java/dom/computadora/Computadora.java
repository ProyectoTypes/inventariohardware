package dom.computadora;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import dom.impresora.Impresora;
import dom.impresora.ImpresoraRepositorio;
import dom.movimiento.Movimiento;
import dom.tecnico.Tecnico;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Computadora_ip_must_be_unique", members = {
		"creadoPor", "ip" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorComputadora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE creadoPor == :creadoPor && " + "ip.indexOf(:ip) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarComputadoraFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "eliminarComputadoraTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE creadoPor == :creadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.computadora.Computadora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && ip.indexOf(:ip) >= 0"), })
@ObjectType("COMPUTADORA")
@Audited
@AutoComplete(repository = ComputadoraRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Computadora implements Comparable<Computadora>{

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getIp();
	}

	public String iconName() {
		return "Computadora";
	}

	// //////////////////////////////////////
	// IP (propiedad)
	// //////////////////////////////////////

	private String ip;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Direccion IP de la Computadora:")
	@MemberOrder(sequence = "10")
	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
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

	// private String disco;
	//
	// @javax.jdo.annotations.Column(allowsNull = "false")
	// @DescribedAs("Disco de la Computadora:")
	// @MemberOrder(sequence = "40")
	// public String getDisco() {
	// return disco;
	// }
	//
	// public void setDisco(final String disco) {
	// this.disco = disco;
	// }

	public static enum CategoriaDisco {
		Seagate, Western, Otro;

	}

	private CategoriaDisco disco;

	@javax.jdo.annotations.Column(allowsNull = "false")
	public CategoriaDisco getDisco() {
		return disco;
	}

	public void setDisco(CategoriaDisco disco) {
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

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "40")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}

	// //////////////////////////////////////
	// Impresora (propiedad)
	// //////////////////////////////////////

	private Impresora impresora;

	@MemberOrder(sequence = "50")
	@javax.jdo.annotations.Column(allowsNull = "true")
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	}
	
	@Named ("Cambiar Impresora")
	public void modificarImpresora (final Impresora unaImpresora){
		Impresora currentImpresora = getImpresora();
		if (unaImpresora == null || unaImpresora.equals(currentImpresora)) {
			return;			
		}
		clearImpresora();
		setImpresora(unaImpresora);
	}
	
	
	public List<Impresora> autoComplete0ModificarImpresora(final String search){
		return this.impresoraRepositorio.autoComplete(search);
	}
	
	// //////////////////////////////////////
	// creadoPor
	// //////////////////////////////////////

	private String creadoPor;

	@Hidden(where = Where.ALL_TABLES)
	@javax.jdo.annotations.Column(allowsNull = "false")
	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	/*****************************************************
	 * Relacion Computadora/Usuario
	 */

	// {{ Usuario (property)
	private Usuario usuario;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	// @Persistent(mappedBy = "usuario")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	// }}
	// ///////////////////////////////////////////////////
	// Operaciones de USUARIO: Agregar/Borrar
	// ///////////////////////////////////////////////////
	@Named("Modificar Usuario")
	public void modifyUsuario(final Usuario unUsuario) {
		Usuario currentUsuario = getUsuario();
		// check for no-op
		if (unUsuario == null || unUsuario.equals(currentUsuario)) {
			return;
		}
		// dissociate existing
		clearUsuario();
		// associate new
		unUsuario.setComputadora(this);
		setUsuario(unUsuario);
		// additional business logic
	}

	@Named("Borrar Usuario")
	public void clearUsuario() {
		Usuario currentUsuario = getUsuario();
		// check for no-op
		if (currentUsuario == null) {
			return;
		}
		// dissociate existing
		currentUsuario.setComputadora(null);
		setUsuario(null);
		// additional business logic
	}

	/*****************************************************
	 * Relacion Computadora/Tecnico
	 */

	// {{ Tecnico (property)
	private Tecnico tecnico;

	@Named("Tecnico Asignado")
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "True")
	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(final Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	// }}

	// ///////////////////////////////////////////////////
	// Operaciones de Tecnico: Agregar/Borrar
	// ///////////////////////////////////////////////////
	public void modifyTecnico(final Tecnico unTecnico) {
		Tecnico currentTecnico = getTecnico();
		// check for no-op
		if (unTecnico == null || unTecnico.equals(currentTecnico)) {
			return;
		}
		// delegate to parent to associate
		unTecnico.addToComputadora(this);
		// additional business logic
	}

	public void clearTecnico() {
		Tecnico currentTecnico = getTecnico();
		// check for no-op
		if (currentTecnico == null) {
			return;
		}
		// delegate to parent to dissociate
		currentTecnico.removeFromComputadora(this);
		// additional business logic
	}

	/**************************************************************
	 * Relacion Computadora(Parent)/Movimiento(Child).
	 */

	@Persistent(mappedBy = "computadora", dependentElement = "False")
	@Join
	private SortedSet<Movimiento> movimientos = new TreeSet<Movimiento>();

	public SortedSet<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(SortedSet<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	@Named("Agregar Movimiento")
	public void addToMovimiento(final Movimiento unMovimiento) {
		// check for no-op
		if (unMovimiento == null || getMovimientos().contains(unMovimiento)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		unMovimiento.clearComputadora();
		// associate arg
		unMovimiento.setComputadora(this);
		getMovimientos().add(unMovimiento);
	}

	@Named("Eliminar de Recepcion")
	public void removeFromMovimiento(final Movimiento unMovimiento) {
		// check for no-op
		if (unMovimiento == null || !getMovimientos().contains(unMovimiento)) {
			return;
		}
		// dissociate arg
		unMovimiento.setComputadora(null);
		getMovimientos().remove(unMovimiento);
	}
	
	public void clearImpresora() {
		Impresora currentImpresora = getImpresora();
		// check for no-op
		if (currentImpresora == null) {
			return;
		}
		// dissociate existing
		setImpresora(null);
		// additional business logic
		// onClearComputadora(currentComputadora);
	}
	
	

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@SuppressWarnings("unused")
	@javax.inject.Inject
	private UsuarioRepositorio usuarioRepositorio;

	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;
	
	@Override
	public int compareTo(Computadora computadora) {
		// TODO Auto-generated method stub
		return ObjectContracts.compare(this, computadora, "ip");
	}

}
