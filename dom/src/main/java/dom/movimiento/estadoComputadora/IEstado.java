package dom.movimiento.estadoComputadora;

import dom.movimiento.Movimiento;

public interface IEstado {
	/**
	 * asignarTecnico: - Estado actual: Recibido. - Cambia al nuevo estado:
	 * Reparando. - Otras operaciones: Asignar Tecnico, controlar cantidad de
	 * comp. por Tecnico.
	 * 
	 * @param unM
	 * @return
	 */
	public abstract IEstado asignarTecnico(Movimiento unM);

	/**
	 * esperarRepuestos: - Estado Actual: Reparando. - Nuevo Estado: Esperando.
	 * - Otras Operaciones:
	 * 
	 * @param unM
	 * @return
	 */
	public abstract IEstado esperarRepuestos(Movimiento unM);

	/**
	 * finalizarSoporte: - Estado Actual: Reparando. - Nuevo Estado: Entregado.
	 * - Otras Operaciones:
	 * 
	 * @param unM
	 * @return
	 */
	public abstract IEstado finalizarSoporte(Movimiento unM);

	/**
	 * noHayRepuestos - Estado Actual: Esperando. - Nuevo Estado: Cancelado.
	 * 
	 * @param unM
	 * @return
	 */
	public abstract IEstado noHayRepuestos(Movimiento unM);

	/**
	 * llegaronRepuestos: - Estado Actual: Esperando. - Nuevo Estado: Reparando.
	 * 
	 * @param unM
	 * @return
	 */
	public abstract IEstado llegaronRepuestos(Movimiento unM);

}
