package dom.zabbix;

import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

public class ZabbixAutenticacion {

	public String obtenerToken() {
		try {

			JSONObject mainJObj = new JSONObject();
			JSONObject paramJObj = new JSONObject();

			mainJObj.put("jsonrpc", "2.0");
			mainJObj.put("method", "user.login");

			paramJObj.put("user", "Admin");
			paramJObj.put("password", "zabbix");

			mainJObj.put("params", paramJObj);
			mainJObj.put("id", "1");

			Webb webb = Webb.create();

			JSONObject result = webb
					.post("http://127.0.0.1/zabbix/api_jsonrpc.php")
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

	public static String obtenerTokenPorIp(final String ip) {
		try {

			JSONObject mainJObj = new JSONObject();
			JSONObject paramJObj = new JSONObject();

			mainJObj.put("jsonrpc", "2.0");
			mainJObj.put("method", "user.login");

			paramJObj.put("user", "Admin");
			paramJObj.put("password", "zabbix");

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
}
