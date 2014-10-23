package dom.zabbix;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

@SuppressWarnings("deprecation")
@DomainService(menuOrder = "100")
@Named("Servidor")
public class ZabbixRepositorio {

	@Named("Configurar IP")
	public Zabbix configurarIpServidor(final @Named("IP") String ip) {
		return updateZabbix(ip);
	}

	@Programmatic
	public Zabbix updateZabbix(final String ip) {
		Zabbix obj = obtenerCuentaZabbix();
		if (obj == null)
			return null;
		obj.setIp(ip);
		obj.setToken(ZabbixAutenticacion.obtenerTokenPorIp(ip));
		container.flush();
		return obj;
	}

	public Zabbix obtenerCuentaZabbix() {
		List<Zabbix> retorno = container.allMatches(new QueryDefault<Zabbix>(
				Zabbix.class, "obtenerCuenta"));
		if (retorno.isEmpty())
			container.informUser("No se encontraron registros de Zabbix");
		return retorno.get(0);
	}

	@Programmatic
	public Zabbix addZabbix(final String ip) {
		Zabbix obj = container.newTransientInstance(Zabbix.class);
		obj.setIp(ip);
		obj.setToken(ZabbixAutenticacion.obtenerTokenPorIp(ip));
		container.persistIfNotAlready(obj);
		return obj;
	}

	@Programmatic
	@PostConstruct
	public void init() {
		vaciarZabbixTable();
		addZabbix("127.0.0.1");
	}

	private void vaciarZabbixTable() {
		isisJdoSupport.executeUpdate("truncate table \"Zabbix\";");
	}

	@javax.inject.Inject
	private IsisJdoSupport isisJdoSupport;
	@javax.inject.Inject
	DomainObjectContainer container;
}
