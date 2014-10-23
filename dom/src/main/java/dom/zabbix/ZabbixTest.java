package dom.zabbix;

import org.json.JSONException;

import dom.zabbix.monitoreo.item.CpuItem;
import dom.zabbix.monitoreo.item.ItemManager;
import dom.zabbix.monitoreo.item.RamItem;

public class ZabbixTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			System.out.println("HOST ID "+ host.requestHostId("127.0.0.1"));
			ItemManager cpu = new CpuItem();
			System.out.println("CPU "+cpu.requestItemGet("127.0.0.1"));
//			Alertas alerta = new Alertas();
//			System.out.println("ALERTAS: "+ alerta.requestAlertGet("127.0.0.1"));
			RamItem ram = new RamItem();
			System.out.println("RAM: "+ ram.requestItemGet("127.0.0.1"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: "+e.getMessage());
		}
	}

}
