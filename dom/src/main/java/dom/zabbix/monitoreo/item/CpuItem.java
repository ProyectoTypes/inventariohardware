package dom.zabbix.monitoreo.item;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class CpuItem extends ItemManager {

	@Override
	protected void cargarParametros() {
		Map<String, String> cpu = new HashMap<String, String>();
		cpu.put("key_", "system.cpu.load");
		try {
			this.getParametrosJson().put("search", cpu);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected String obtenerValor(final String ip) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
