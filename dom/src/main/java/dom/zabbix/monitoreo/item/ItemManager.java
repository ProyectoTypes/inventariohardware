package dom.zabbix.monitoreo.item;

import javax.annotation.PostConstruct;

import org.json.JSONException;

import dom.zabbix.Zabbix;
import dom.zabbix.ZabbixManager;
import dom.zabbix.ZabbixRepositorio;

public abstract class ItemManager extends ZabbixManager {

	

	/**
	 * La ip que entra como parametro se refiere a la ip del Host. El metodo
	 * permite obtener los datos de los items generados en zabbix.
	 * 
	 * @param ip
	 * @return
	 * @throws JSONException
	 */
	@PostConstruct
	public String requestItemGet(final String ip, final Zabbix zabbix) throws JSONException {
		System.out.println("RequestItem");
		String token = zabbix.getToken();// Seteado anteriormente.

		this.getParametrosJson().put("output", "extend");
		this.getParametrosJson().put("host", zabbix.getHost());

		this.cargarParametros();

		this.getObjetoJson().put("sortfield", "name");
		this.getObjetoJson().put("params", this.getParametrosJson());
		this.getObjetoJson().put("jsonrpc", "2.0");
		this.getObjetoJson().put("method", "item.get");
		this.getObjetoJson().put("auth", token);
		this.getObjetoJson().put("id", "1");

		return this.obtenerValor(ip);

	}

	/**
	 * Metodo abstracto del template method. Cada subclase implementara este
	 * metodo a su criterio.
	 */
	protected abstract void cargarParametros();

	protected abstract String obtenerValor(String ip) throws JSONException;

}
