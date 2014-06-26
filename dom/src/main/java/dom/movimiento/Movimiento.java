package dom.movimiento;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

import dom.computadora.Computadora;
import dom.tecnico.Tecnico;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "autoCompletePorMovimiento", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE ingresadoPor == :ingresadoPor && " + "ip.indexOf(:ip) >= 0"),
		@javax.jdo.annotations.Query(name = "eliminarMovimientoFalse", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE ingresadoPor == :ingresadoPor "
				+ "   && habilitado == false"),
		@javax.jdo.annotations.Query(name = "eliminarMovimientoTrue", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE ingresadoPor == :ingresadoPor " + "   && habilitado == true"),
		@javax.jdo.annotations.Query(name = "buscarPorIp", language = "JDOQL", value = "SELECT "
				+ "FROM dom.movimiento.Movimiento "
				+ "WHERE ingresadoPor == :ingresadoPor "
				+ "   && ip.indexOf(:ip) >= 0"), })
@ObjectType("MOVIMIENTO")
@Audited
@AutoComplete(repository = MovimientoRepositorio.class, action = "autoCompletePorMovimiento")
@Bookmarkable

public class Movimiento {

}
