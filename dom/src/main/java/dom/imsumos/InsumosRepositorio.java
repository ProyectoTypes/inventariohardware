package dom.imsumos;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;

public class InsumosRepositorio {

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String title() {
		return "insumos";
	}

	public String iconName() {
		return "Insumos";
	}

	// //////////////////////////////////////
	// Agregar Insumos
	// //////////////////////////////////////

	@MemberOrder(sequence = "10")
	@Named("Agregar")
	public Insumos addInsumos(final @Named("Codigo") String codigo,
			final @Named("Cantidad") int cantidad,
			final @Named("Producto") String producto,
			final @Named("Marca") String marca,
			final @Optional @Named("Observaciones") String observaciones) {
		return nuevosInsumos(codigo, cantidad, producto, marca, observaciones,
				this.currentUserName());
	}

	@Programmatic
	public Insumos nuevosInsumos(final String codigo, final int cantidad,
			final String producto, final String marca,
			final String observaciones, final String creadoPor) {
		final Insumos unInsumo = container.newTransientInstance(Insumos.class);
		unInsumo.setCodigo(codigo.toUpperCase().trim());
		unInsumo.setCantidad(cantidad);
		unInsumo.setProducto(producto.toUpperCase().trim());
		unInsumo.setMarca(marca.toUpperCase().trim());
		unInsumo.setObservaciones(observaciones.toUpperCase().trim());
		unInsumo.setHabilitado(true);
		unInsumo.setCreadoPor(creadoPor);
		container.persistIfNotAlready(unInsumo);
		container.flush();
		return unInsumo;
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;
}
