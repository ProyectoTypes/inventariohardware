package dom.correo;

import java.io.IOException;

import servicio.encriptar.EncriptaException;

public class CorreoTest {

	public static void main(String[] args) throws EncriptaException, IOException {
		Recibe recepcion = new Recibe();
		recepcion.setProperties();
		recepcion.accion();

		
	}

}
