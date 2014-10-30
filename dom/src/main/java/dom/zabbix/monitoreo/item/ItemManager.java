package dom.zabbix.monitoreo.item;

import org.json.JSONException;

import dom.zabbix.ZabbixManager;

public abstract class ItemManager extends ZabbixManager {

	public String requestItemGet(final String ip) throws JSONException {

		String token = getZabbix().getToken();

		this.getParametrosJson().put("output", "extend");
		this.getParametrosJson().put("host", getZabbix().getHost());

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
