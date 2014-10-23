package dom.zabbix;

import javax.annotation.PostConstruct;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(menuOrder = "100")
@Named("Servidor")
public class ZabbixRepositorio {

	@Named("Configurar IP")
	public Zabbix configurarIpServidor(final @Named("IP") String ip) {
		return updateZabbix(ip);
	}
	@Programmatic
	public Zabbix updateZabbix(final String ip)
	{
		Zabbix obj = obtenerCuentaZabbix();
		if(obj==null)
			return null;
		obj.setIp(ip);
		obj.setToken(ZabbixAutenticacion.obtenerTokenPorIp(ip));
		container.flush();
		return obj;
	}
	
	public  Zabbix obtenerCuentaZabbix()
	{
		Zabbix retorno = container.firstMatch(new QueryDefault<Zabbix>(Zabbix.class, "obtenerCuenta"));
		if (retorno == null)
			container.informUser("No se encontraron registros de Zabbix");
		return retorno;
	}
	@javax.inject.Inject
	DomainObjectContainer container;
}
