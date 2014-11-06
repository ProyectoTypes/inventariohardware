package dom.zabbix;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Named;
import org.json.JSONException;

@DomainService(menuOrder = "100")
@Named("Ram Z")
public class RamItem extends ItemManager {

	public RamItem() {
	}

	public String mostrarElMostrar() throws JSONException {
		return super.requestItemGet("127.0.0.1");
	}

	@Override
	protected void cargarParametros() {
		Map<String, String> cpu = new HashMap<String, String>();
		cpu.put("key_", "vm.memory.size[total]");// Renombrar la key para cada
													// caso.
		try {
			parametrosJson.put("search", cpu);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected String obtenerValor(final String ip) throws JSONException {
//		System.out.println("////////// obtner valor: "+ip);
		String resultado =this.ejecutarJson(ip).getString("result");
		if(resultado=="" || resultado.length()==0 || resultado.contentEquals("[]"))
			return "SIN DEFINIR";
		String[] cadena = resultado.split(",");
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
	public Zabbix obtenerZabbix() {
		// TODO Auto-generated method stub
		return this.zabbixRepositorio.obtenerCuentaZabbix();
	}
	public Zabbix mostrarCuentaZabbix() {
		return this.zabbixRepositorio.obtenerCuentaZabbix();
	}
}
