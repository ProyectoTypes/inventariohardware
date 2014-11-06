package dom.zabbix;

import java.net.InetAddress;

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

	public ZabbixRepositorio() {

	}

	@Named("Configurar IP")
	public Zabbix configurarIpServidor(final @Named("IP") String ip,
			final @Named("Nombre del HOST") String host) {
		if (this.ping(ip)) {
			return updateZabbix(ip, host);
		}
		container.warnUser("IP: " + ip
				+ " Incorrecta, el SERVIDOR no responde."
				+ " No se han aplicado los cambios.");
		return obtenerCuentaZabbix();
	}

	@Programmatic
	public Zabbix updateZabbix(final String ip, final String host) {
		Zabbix obj = obtenerCuentaZabbix();
		if (obj == null)
			return null;
		obj.setIp(ip);
		obj.setHost(host);
		obj.setToken(ItemManager.obtenerTokenServer(ip));
		// obj.setToken("inventario");
		container.flush();
		return obj;
	}

	public Zabbix obtenerCuentaZabbix() {
		System.out.println("ENTRA");
		Zabbix retorno = container.uniqueMatch(new QueryDefault<Zabbix>(
				Zabbix.class, "obtenerCuenta"));
		if (retorno!=null){
			container.informUser("zABBIX NO null d:");
					
			return retorno;
		}
		container.informUser("No se encontraron registros de Zabbix");
		return null;
	}

	@Programmatic
	public Zabbix addZabbix(final String ip, final String host) {
		Zabbix obj = container.newTransientInstance(Zabbix.class);
		obj.setIp(ip);
		obj.setHost(host);
		obj.setToken(ItemManager.obtenerTokenServer(ip));
		// obj.setToken("inventariohardware");
		container.persistIfNotAlready(obj);
		return obj;
	}

	@Programmatic
	@PostConstruct
	public void init() {
		if (obtenerCuentaZabbix() == null) {
			addZabbix("127.0.0.1", "inventario");
		}
	}

	private boolean ping(final String ip) {
		InetAddress ping = null;
		try {
			ping = InetAddress.getByName(ip);
			if (ping.isReachable(5000))
				return true;
			else
				return false;

		} catch (Exception ex) {
			System.out.println("ERROR: " + ex);
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
	public DomainObjectContainer container;

	public String mostrarRepo() {
		// TODO Auto-generated method stub
		return "El repo esta mostrando datos.";
	}
}