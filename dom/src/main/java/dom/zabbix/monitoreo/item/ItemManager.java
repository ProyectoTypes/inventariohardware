package dom.zabbix.monitoreo.item;

import org.json.JSONException;

import dom.zabbix.ZabbixManager;

public abstract class ItemManager extends ZabbixManager {

	public String requestItemGet(final String ip) throws JSONException {

		String token = this.obtenerToken(ip);

		this.getParametrosJson().put("output", "extend");
		this.getParametrosJson().put("host", HOST);

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
	 * Metodo abtrascto del template method. Cada subclase implementara este
	 * metodo a su criterio.
	 */
	protected abstract void cargarParametros();
	protected abstract String obtenerValor(String ip) throws JSONException;


}
