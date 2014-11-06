package dom.zabbix;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Programmatic;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

public abstract class ItemManager {

	/**
	 * Constructor
	 */
	public ItemManager() {
		objetoJson = new JSONObject();
		parametrosJson = new JSONObject();
	}

	/* ************* Atributos ************ */
	/*
	 * Los atributos JSONObject deben tener la anotacion @Programmatic, porque
	 * el framework no pueden mostrarlo a traves del wicket.
	 */

	protected static JSONObject objetoJson;
	protected static JSONObject parametrosJson;
	public static Zabbix zabbix;

	/* ************* Metodos ************ */
	/**
	 * La ip que ingresa por parametro hace referencia a la ip del host.
	 * 
	 * @return
	 * @throws JSONException
	 */
	@Programmatic
	protected JSONObject ejecutarJson(final String ip) throws JSONException {
		Webb webb = Webb.create();
		 System.out.println("&&&&&&&&&&&&&&&&&&&&&&&OBJETOJSON : "+objetoJson.toString()+"<--------");
		return webb
				.post("http://" + ip + "/zabbix/api_jsonrpc.php")
				.header("Content-Type", "application/json").useCaches(false)
				.body(objetoJson).ensureSuccess().asJsonObject().getBody();
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

	/**
	 * La ip que entra como parametro se refiere a la ip del Host. El metodo
	 * permite obtener los datos de los items generados en zabbix.
	 * 
	 * @param ip
	 * @return
	 * @throws JSONException
	 */
	public String requestItemGet(final String ip) throws JSONException {
		// System.out.println("RequestItem");
		zabbix = this.zabbixRepositorio.obtenerCuentaZabbix();
		System.out.println("perrrrrrrrrrooooOoo ..... .. .. . "
				+ zabbix.getToken());
		String token = zabbix.getToken();// Seteado anteriormente.

		parametrosJson.put("output", "extend");
		parametrosJson.put("host", zabbix.getHost());

		this.cargarParametros();

		objetoJson.put("sortfield", "name");
		objetoJson.put("params", parametrosJson);
		objetoJson.put("jsonrpc", "2.0");
		objetoJson.put("method", "item.get");
		objetoJson.put("auth", token);
		objetoJson.put("id", "1");

		return this.obtenerValor(ip);

	}

	/**
	 * Metodo abstracto del template method. Cada subclase implementara este
	 * metodo a su criterio.
	 */
	protected abstract void cargarParametros();

	protected abstract String obtenerValor(String ip) throws JSONException;

	/* ************* Metodos de Prueba ************ */

	@Inject
	public ZabbixRepositorio zabbixRepositorio;
}
