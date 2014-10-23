package dom.zabbix;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Uniques({ @javax.jdo.annotations.Unique(name = "ZabbixToken", members = {
		"ip", "token" }) })
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name = "obtenerCuentaZabbix", language = "JDOQL", value = "SELECT "
			+ "FROM dom.zabbix.Zabbix ") })
@ObjectType("Servidor Zabbix")
@Audited
@Bookmarkable
public class Zabbix {

	public String title() {
		return this.getIp();
	}

	public String iconName() {
		return "Computadora";
	}

	private String token;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private String ip;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
