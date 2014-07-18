package dom.insumos;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "Insumos_must_be_unique", members = {
		"creadoPor", "codigo" }) })
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorInsumo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo"
				+ "WHERE creadoPor == :creadoPor && "
				+ "codigo.indexOf(:codigo) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarInsumoFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo"
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "eliminarInsumoTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo"
				+ "WHERE creadoPor == :creadoPor "
				+ "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM dom.insumo.Insumo"
				+ "WHERE creadoPor == :creadoPor "
				+ "   && codigo.indexOf(:codigo) >= 0"), })
@ObjectType("INSUMOS")
@Audited
@AutoComplete(repository = InsumosRepositorio.class, action = "autoComplete")
@Bookmarkable
public class Insumos {

	// //////////////////////////////////////
	// Identificacion en la UI
	// //////////////////////////////////////

	public String title() {
		return this.getCodigo();
	}

	public String iconName() {
		return "Insumos";
	}

	// //////////////////////////////////////
	// codigo (Atributo)
	// //////////////////////////////////////
	private String codigo;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Codigo numero de insumo:")
	@MemberOrder(sequence = "10")
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	// //////////////////////////////////////
	// cantidad (Atributo)
	// //////////////////////////////////////
	private int cantidad;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Cantidades a solicitar:")
	@MemberOrder(sequence = "20")
	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	// //////////////////////////////////////
	// producto (Atributo)
	// //////////////////////////////////////
	private String producto;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Nombre del insumo:")
	@MemberOrder(sequence = "30")
	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	// //////////////////////////////////////
	// marca (Atributo)
	// //////////////////////////////////////
	private String marca;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Marca del Insumo:")
	@MemberOrder(sequence = "40")
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	// //////////////////////////////////////
	// observaciones (Atributo)
	// //////////////////////////////////////
	private String observaciones;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@DescribedAs("Observaciones del insumo:")
	@MemberOrder(sequence = "50")
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	// //////////////////////////////////////
	// Habilitado (propiedad)
	// //////////////////////////////////////

	public boolean habilitado;

	@Hidden
	@MemberOrder(sequence = "60")
	public boolean getEstaHabilitado() {
		return habilitado;
	}

	public void setHabilitado(final boolean habilitado) {
		this.habilitado = habilitado;
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

}
