package dom.zabbix;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;


public abstract class ZabbixManager {

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

	/*
	 * FIN: Atributos
	 */
	/*
	 * OPERACIONES
	 */

	/**
	 * La ip que ingresa por parametro hace referencia a la ip del host.
	 * 
	 * @return
	 */
	protected JSONObject ejecutarJson(final String ip) {
		Webb webb = Webb.create();
		return webb.post("http://" + ip + "/zabbix/api_jsonrpc.php")
				.header("Content-Type", "application/json").useCaches(false)
				.body(getObjetoJson()).ensureSuccess().asJsonObject().getBody();

	}

	/**
	 * Permite obtener el token a partir de la autenticacion en Zabbix.
	 * 
	 * @param ip
	 * @return
	 */
	protected static String obtenerTokenServer(final String ip) {
		try {

			JSONObject mainJObj = new JSONObject();
			JSONObject paramJObj = new JSONObject();

			mainJObj.put("jsonrpc", "2.0");
			mainJObj.put("method", "user.login");

			paramJObj.put("user", "Admin");// Por Parametro
			paramJObj.put("password", "zabbix");// POr parametro

			mainJObj.put("params", paramJObj);
			mainJObj.put("id", "1");

			Webb webb = Webb.create();
			System.out.println("ip"+ip);
			JSONObject result = webb
					.post("http://" + ip + "/zabbix/api_jsonrpc.php")
					.header("Content-Type", "application/json")
					.useCaches(false).body(mainJObj).ensureSuccess()
					.asJsonObject().getBody();

			return result.getString("result");

		} catch (JSONException je) {

			System.out.println("Error creating JSON request to Zabbix API..."
					+ je.getMessage());

		}
		return null;
	}
	

}
