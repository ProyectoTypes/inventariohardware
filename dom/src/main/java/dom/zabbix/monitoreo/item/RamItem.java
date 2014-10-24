package dom.zabbix.monitoreo.item;

import java.text.DecimalFormat;
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
	protected String obtenerValor(final String ip) throws JSONException {
		String resultado ="Sin Especificar";
		String[] cadena = this.ejecutarJson(ip).getString("result").split(",");
		boolean encontro =false;
		for(int i =0 ; i<cadena.length && !encontro;i++)
			if(cadena[i].startsWith("\"lastvalue")){
				resultado = cadena[i].split("\"")[3];//7471730688
				encontro =true;
			}
		Double numero = (((Double.parseDouble(resultado) / (1024))/1024)/1024);
		DecimalFormat df= new DecimalFormat("#0.00");
		String numeroConFormato= df.format(numero);
		return numeroConFormato+"GB";
	}
}
