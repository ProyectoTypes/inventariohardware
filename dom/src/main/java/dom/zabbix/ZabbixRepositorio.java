package dom.zabbix;

import java.io.IOException;
import java.net.InetAddress;
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
		if (this.ping(ip)) {
			container.warnUser("Conexion con el SERVIDOR exitosa.");
			return updateZabbix(ip);
		}
		container
				.warnUser("IP: "
						+ ip
						+ " Incorrecta, el SERVIDOR no responde.\n No se han aplicado los cambios.");
		return null;
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
		if (obtenerCuentaZabbix() == null)
			addZabbix("127.0.0.1");
	}

	private boolean ping(final String ip) {
		InetAddress ping;
		try {
			ping = InetAddress.getByName(ip);
			if (ping.isReachable(5000))
				return true;
			
		} catch (IOException ex) {
			System.out.println("ERROR: "+ex);
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void vaciarZabbixTable() {
		isisJdoSupport.executeUpdate("truncate table \"Zabbix\";");
	}

	@javax.inject.Inject
	private IsisJdoSupport isisJdoSupport;
	@javax.inject.Inject
	DomainObjectContainer container;
}
