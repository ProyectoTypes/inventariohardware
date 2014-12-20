package fixture.datos;

import java.util.Random;

public class DatosFixture {
private static int	Cantidad=10;
	
	private static String Nombre="Exequiel,Nicolas,Daniel,Oscar,Rodrigo,Walter,Fernando,Eduardo,Eduardo";
	private static String Apellido="Wiedermann,Vergara,Sepulveda,Muñoz,Addati,Gadaleta,Buffolo";
	
	public static int ObtenerCantidad()
	{
		return Cantidad;
	}
	
	public static String ObtenerNombre()
	{
		return ObtenerValor(Nombre);
	}
	
	public static String ObtenerApellido()
	{
		return ObtenerValor(Apellido);
	}
	
	public static int Random(int Inicial, int Final)
	{
		Random rnd = new Random();
		int Resul=(int) (rnd.nextDouble() * Final + Inicial);
		return Resul;
	}
	
	private static String ObtenerValor(String valor)
	{
		String[] Partes=Dividir(valor);
		return Partes[Random(0,Partes.length)];
	}
	
	public static String[] Dividir(String palabras)
	{
		String[] partes = palabras.split(",");
		return partes;
	}
}
