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

	protected JSONObject ejecutarJson() {
		Webb webb = Webb.create();
		// FIXME: Verificar la direccion, hace referencia a la dir del Servidor.
		// El superUser deberia setearla ?
		
		return webb.post("http://127.0.0.1/zabbix/api_jsonrpc.php")
				.header("Content-Type", "application/json").useCaches(false)
				.body(getObjetoJson()).ensureSuccess().asJsonObject().getBody();

	}

}
