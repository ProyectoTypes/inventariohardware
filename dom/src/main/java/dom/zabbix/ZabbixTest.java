package dom.zabbix;

import org.json.JSONException;

import dom.zabbix.monitoreo.item.RamItem;

public class ZabbixTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			System.out.println("HOST ID "+ host.requestHostId("127.0.0.1"));
//			ItemManager cpu = new CpuItem();
//			System.out.println("CPU "+cpu.requestItemGet("127.0.0.1"));
//			Alertas alerta = new Alertas();
//			System.out.println("ALERTAS: "+ alerta.requestAlertGet("127.0.0.1"));
			RamItem ram = new RamItem();
			System.out.println("RAM: "+ ram.requestItemGet("127.0.0.1"));
//			InetAddress ping; 
//			String ip = "13"; // Ip de la máquina remota 
//			try { 
//			ping = InetAddress.getByName(ip);
//			if(ping.isReachable(5000)){ 
//			System.out.println(ip+" - SI responde!"); 
//			}else { 
//			System.out.println(ip+" - no responde!"); 
//			}
//			} catch (IOException ex) { System.out.println(ex); } 
//			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: "+e.getMessage());
		}
	}

}
