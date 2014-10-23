package dom.zabbix.monitoreo.item;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class RamItem extends ItemManager {

	@Override
	protected void cargarParametros() {
		Map<String, String> cpu = new HashMap<String, String>();
		cpu.put("key_", "vm.memory.size[total]");// Renombrar la key para cada
													// caso.
		try {
			this.getParametrosJson().put("search", cpu);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected String obtenerValor() throws JSONException {
		String resultado ="Sin Especificar";
		String[] cadena = this.ejecutarJson().getString("result").split(",");
		boolean encontro =false;
		for(int i =0 ; i<cadena.length && !encontro;i++)
			if(cadena[i].startsWith("\"lastvalue")){
				resultado = cadena[i].split(":")[1];
				encontro =true;
			}
		return resultado;
	}
}
