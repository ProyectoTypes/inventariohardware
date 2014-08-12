package dom.movimiento.estadoComputadora;


public interface IEstado {
	/**
	 * asignarTecnico: - Estado actual: Recibido. - Cambia al nuevo estado:
	 * Reparando. - Otras operaciones: Asignar Tecnico, controlar cantidad de
	 * comp. por Tecnico.
	 * 
	 * @param unM
	 * @return
	 */
	void asignarTecnico();

	/**
	 * esperarRepuestos: - Estado Actual: Reparando. - Nuevo Estado: Esperando.
	 * - Otras Operaciones:
	 * 
	 * @param unM
	 * @return
	 */
	void esperarRepuestos();

	/**
	 * finalizarSoporte: - Estado Actual: Reparando. - Nuevo Estado: Entregado.
	 * - Otras Operaciones:
	 * 
	 * @param unM
	 * @return
	 */
	void finalizarSoporte();

	/**
	 * noHayRepuestos - Estado Actual: Esperando. - Nuevo Estado: Cancelado.
	 * 
	 * @param unM
	 * @return
	 */
     void noHayRepuestos();

	/**
	 * llegaronRepuestos: - Estado Actual: Esperando. - Nuevo Estado: Reparando.
	 * 
	 * @param unM
	 * @return
	 */
	void llegaronRepuestos();

}
