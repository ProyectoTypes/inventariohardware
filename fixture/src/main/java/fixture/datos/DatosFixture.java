package fixture.datos;

import java.util.Random;

public class DatosFixture {
private static int Cantidad = 10;
	
	private static String Nombre="Exequiel,Nicolas,Daniel,Oscar,Rodrigo,Walter,Fernando,Eduardo,Eduardo";
	private static String Apellido="Wiedermann,Vergara,Sepulveda,Muñoz,Addati,Gadaleta,Buffolo";
	private static String Sector="Recursos_Humanos,Administración,Ventas,Compras,Tesorerías,Admisión,Despacho,Privada,Secretaría,Prensa";
	private static String Email="nicolasvergara89@gmail.com,EWFWierdermann@gmail.com,oscarsepulveda85@gmail.com,cipoleto@gmail.com";
			
	public static int ObtenerCantidad()
	{
		return Cantidad;
	}
	
	public static String ObtenerNombre() {
		return ObtenerValor(Nombre);
	}
	
	public static String ObtenerApellido() {
		return ObtenerValor(Apellido);
	}
	
	public static String ObtenerSector() {
		return ObtenerValor(Sector);
	}
	
	public static String ObtenerEmail() {
		return ObtenerValor(Email);
	}
	
	public static int Random(int Inicial, int Final) {
		Random rnd = new Random();
		int Resul=(int) (rnd.nextDouble() * Final + Inicial);
		return Resul;
	}
	
	private static String ObtenerValor(String valor) {
		String[] Partes=Dividir(valor);
		return Partes[Random(0,Partes.length)];
	}
	
	public static String[] Dividir(String palabras)	{
		String[] partes = palabras.split(",");
		return partes;
	}
}