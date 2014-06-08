package dom.tecnico;

import java.util.Comparator;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.util.ObjectContracts;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Ordering;

import dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="Tecnico_apellido_must_be_unique", 
            members={"creadoPor","apellido"})
})
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name = "autoCompletarPorApellido", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.tecnico.Tecnico "
                    + "WHERE creadoPor == :creadoPor && "
                    + "apellido.indexOf(:apellido) >= 0"),
    @javax.jdo.annotations.Query(
            name = "eliminarTecnicoFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.tecnico.Tecnico "
                    + "WHERE creadoPor == :creadoPor "
                    + "   && habilitado == false"),
    @javax.jdo.annotations.Query(
            name = "eliminarTecnicoTrue", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.tecnico.Tecnico "
                    + "WHERE creadoPor == :creadoPor "
                    + "   && habilitado == true"),
    @javax.jdo.annotations.Query(
    		name = "buscarPorApellido", language = "JDOQL", 
            value = "SELECT "
                    + "FROM dom.tecnio.Tecnico"
                    + "WHERE creadoPor == :creadoPor && "
                    + "apellido.indexOf(:apellido) >= 0"),
	@javax.jdo.annotations.Query(
			name = "getTecnico", language = "JDOQL", 
			value = "SELECT FROM dom.tecnico.Tecnico WHERE creadoPor == :creadoPor")
})

@ObjectType("TECNICO")
@Audited
@AutoComplete(repository=TecnicoRepositorio.class, action="autoComplete") //
@Bookmarkable
public class Tecnico extends Persona implements Comparable<Persona>{
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String title() {
		return this.getApellido() + ", " + this.getNombre();
	}

	public String iconName() {
		return "Tecnico";
	}
	
	// //////////////////////////////////////
	// Borrar Tecnico
	// //////////////////////////////////////
	/**
     * Método que utilizo para deshabilitar un Proveedor.
     * 
     * @return pone el proveedor en false
     */
	//{{ 
	@Named("Eliminar")
	@PublishedAction
	@Bulk
	@MemberOrder(name="accionEliminar", sequence = "1")	
	public List<Tecnico> eliminar() {
		if(getEstaHabilitado()==true){
				setHabilitado(false);    
			    container.isPersistent(this);     
			    container.warnUser("Eliminado " + container.titleOf(this));	
		}
		return null;
	}

	// //////////////////////////////////////
	// Comparador (Ordenar por Apellido)
	// //////////////////////////////////////

	//Overrides el orden natural
    public static class DependenciesComparatorTecnico implements Comparator<Tecnico> {
        @Override
        public int compare(Tecnico t, Tecnico e) {
            Ordering<Tecnico> byApellido = new Ordering<Tecnico>() {
                public int compare(final Tecnico t, final Tecnico e) {
                    return Ordering.natural().nullsFirst().compare(t.getApellido(), e.getApellido());
                }
            };
            return byApellido
                    .compound(Ordering.<Tecnico>natural())
                    .compare(t, e);
        }
    }



    // //////////////////////////////////////
    // Complete (property), 
    // Done (action), Undo (action)
    // //////////////////////////////////////

    private boolean complete;

    @Disabled
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(final boolean complete) {
        this.complete = complete;
    }


	// //////////////////////////////////////
    // Predicates
    // //////////////////////////////////////
	public static class Predicates{

        public static Predicate<Tecnico> thoseCreadoPorBy(final String currentUser) {
            return new Predicate<Tecnico>() {
                @Override
                public boolean apply(final Tecnico tecnico) {
                    return Objects.equal(tecnico.getCreadoPor(), currentUser);
                }
            };
        }
        
        public static Predicate<Tecnico> thoseWithSimilarDescription(final String apellido) {
            return new Predicate<Tecnico>() {
                @Override
                public boolean apply(final Tecnico t) {
                    return t.getApellido().contains(apellido);
                }
            };
        }
	}

	
    // //////////////////////////////////////
    // CompareTo
    // //////////////////////////////////////
	/**
	 * Implementa Comparable<Usuario>
	 * Necesario para ordenar por apellido la clase Usuario.
	 */
    @Override
    public int compareTo(final Persona persona) {
        return ObjectContracts.compare(this,persona, "apellido");
    }
    // //////////////////////////////////////
    // Injected Services
    // //////////////////////////////////////

    @javax.inject.Inject
    private DomainObjectContainer container;
}