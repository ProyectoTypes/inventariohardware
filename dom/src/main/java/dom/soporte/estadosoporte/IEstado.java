package dom.soporte.estadosoporte;

import org.json.JSONException;

import dom.computadora.Computadora.CategoriaDisco;
import dom.impresora.Impresora;
import dom.tecnico.Tecnico;

public interface IEstado {
	void asignarTecnico(final Tecnico tecnico);

	void solicitarInsumos(final int cantidad, final String producto,
			final String marca, final String modelo);

	void finalizarSoporte();

	void asignarNuevoEquipo(final String ip, final String mother,
			final String procesador, final CategoriaDisco disco,
			 final Impresora impresora) throws JSONException;

	boolean escondeAsignarTecnico();

	boolean escondeFinalizarSoporte();

	boolean escondeSolicitarInsumos();

	boolean escondeLlegaronInsumos();

	boolean escondeNoHayInsumos();

	boolean escondeAsignarNuevoEquipo();

}
