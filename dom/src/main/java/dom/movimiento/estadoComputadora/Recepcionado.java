package dom.movimiento.estadoComputadora;

import dom.movimiento.Movimiento;

public class Recepcionado implements IEstado {
	Movimiento movimiento;
	public Recepcionado(Movimiento movimiento)
	{
		this.movimiento=movimiento;
	}

	
	@Override
	public void equipoRecibido() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void equipoReparado() {
		
	}
	@Override
	public void equipoFinalizado() {
		
	}
	

}
