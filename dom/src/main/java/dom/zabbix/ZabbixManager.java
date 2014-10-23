package dom.zabbix;

import org.json.JSONObject;

import com.goebl.david.Webb;

public abstract class ZabbixManager {
	protected static final String HOST = "inventario";
	/**
	 * Constructor
	 */
	public ZabbixManager() {
		this.objetoJson = new JSONObject();
		this.parametrosJson = new JSONObject();
	}
	
	/*
	 * Atributos
	 */
	private JSONObject objetoJson;

	public JSONObject getObjetoJson() {
		return objetoJson;
	}

	public void setObjetoJson(JSONObject objetoJson) {
		this.objetoJson = objetoJson;
	}

	private JSONObject parametrosJson;

	public JSONObject getParametrosJson() {
		return parametrosJson;
	}

	public void setParametrosJson(JSONObject parametrosJson) {
		this.parametrosJson = parametrosJson;
	}
	/* FIN: Atributos
	*/
	/*
	 * OPERACIONES
	 */
	/**
	 * Permite obtener el token a partir de la autenticacion en Zabbix.
	 * @param ip
	 * @return
	 */
	protected String obtenerToken(final String ip) {
		return ZabbixAutenticacion.obtenerTokenPorIp(ip);
	}
	/**
	 * La ip que ingresa por parametro hace referencia a la ip del host.
	 * @return
	 */
	protected JSONObject ejecutarJson(final String ip ) {
		Webb webb = Webb.create();		
		return webb.post("http://"+ip+"/zabbix/api_jsonrpc.php")
				.header("Content-Type", "application/json").useCaches(false)
				.body(getObjetoJson()).ensureSuccess().asJsonObject().getBody();

	}

}
