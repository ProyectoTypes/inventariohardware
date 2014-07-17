package dom.imsumos;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

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

public class Insumos {

}
