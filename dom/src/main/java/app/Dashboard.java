package app;

import java.util.List;

import org.apache.isis.applib.AbstractViewModel;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;

import dom.soporte.Soporte;
import dom.soporte.SoporteRepositorio;

@MemberGroupLayout(columnSpans = {0,0,0,12})
public class Dashboard extends AbstractViewModel {

	public String title() {
		return "Bandeja de Soporte.";
	}

	public String iconName() {
		return "Dashboard";
	}

	// //////////////////////////////////////
	// ViewModel contract
	// //////////////////////////////////////

	private String memento;

	@Override
	public String viewModelMemento() {
		return memento;
	}

	@Override
	public void viewModelInit(String memento) {
		this.memento = memento;
	}

	// //////////////////////////////////////
	// listar soportes sin finalizar.
	// //////////////////////////////////////
	@Named("En espera")
	@Render(Type.EAGERLY)
	@Disabled
	@MemberOrder(sequence = "10")
	@MultiLine(numberOfLines = 10)
	public List<Soporte> getAllSoportesEsperando() {
		return  SoporteRepositorio.queryBuscarSoportesEnEspera();
	}

}
