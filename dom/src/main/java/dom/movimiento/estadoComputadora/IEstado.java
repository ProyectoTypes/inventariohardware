package dom.movimiento.estadoComputadora;

import dom.movimiento.Movimiento;

public interface IEstado {
	
	public abstract IEstado asignarTecnico(Movimiento unM);

	public abstract IEstado esperarRepuestos(Movimiento unM);

	public abstract IEstado finalizarSoporte(Movimiento unM);

	public abstract IEstado noHayRepuestos(Movimiento unM);

	
	public abstract IEstado llegaronRepuestos(Movimiento unM);

}
