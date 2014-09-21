package dom.monitoreo;

public class Monitoreo {
	
	private int computadoraIP;

	public int getComputadoraIP() {
		return computadoraIP;
	}

	public void setComputadoraIP(final int computadoraIP) {
		this.computadoraIP = computadoraIP;
	}
	
	private String usuarioMonitoreo;
	
	public String getUsuarioMonitoreo() {
		return usuarioMonitoreo;
	}

	public void setUsuarioMonitoreo(final String usuarioMonitoreo) {
		this.usuarioMonitoreo = usuarioMonitoreo;
	}
	
	private String claveMonitoreo;

	public String getClaveMonitoreo() {
		return claveMonitoreo;
	}

	public void setClaveMonitoreo(final String claveMonitoreo) {
		this.claveMonitoreo = claveMonitoreo;
	}
	
	private String servidorMonitoreo;

	public String getServidorMonitoreo() {
		return servidorMonitoreo;
	}

	public void setServidorMonitoreo(final String servidorMonitoreo) {
		this.servidorMonitoreo = servidorMonitoreo;
	}
	
	private String informacion;

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}
}