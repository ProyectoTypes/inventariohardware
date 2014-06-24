package dom.impresora;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;

import dom.computadora.ComputadoraRepositorio;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Impresora_nombreImpresora_must_be_unique", members = {
		"creadoPor", "nombreImpresora" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorNombreImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor && nombreImpresora.indexOf(:nombreImpresora) >= 0"),
		@javax.jdo.annotations.Query(name = "todasLasImpresoras", language = "JDOQL", value = "SELECT FROM dom.impresora.Impresora "
				+ " WHERE creadoPor == :creadoPor && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "eliminarImpresoraTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorNombreImpresora", language = "JDOQL", value = "SELECT "
				+ "FROM dom.impresora.Impresora "
				+ "WHERE creadoPor == :creadoPor "
				+ "   && nombreSector.indexOf(:nombreImpresora) >= 0") })


@ObjectType("COMPUTADORA")
@Audited
@AutoComplete(repository = ComputadoraRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Impresora {

	// //////////////////////////////////////
	// Identificacion en la UI. Aparece como item del menu
	// //////////////////////////////////////

	public String title() {
		return this.getNombreImpresora();
	}

	public String iconName() {
		return "Impresora";
	}

	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	private String nombreImpresora;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("Nombre de la Impresora:")
	@MemberOrder(sequence = "10")
	public String getNombreImpresora() {
		return nombreImpresora;
	}

	public void setNombreImprsora(String nombreImpresora) {
		this.nombreImpresora = nombreImpresora;
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

	// //////////////////////////////////////
	// Complete (property),
	// Se utiliza en las acciones add (action), DeshacerAgregar (action)
	// //////////////////////////////////////

	private boolean complete;

	@Disabled
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	// //////////////////////////////////////
	// Habilitado
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(name = "Detalles", sequence = "9")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
	}
}
