package dom.tecnico;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.SortedBy;
import org.apache.isis.applib.annotation.TypicalLength;
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
            name = "CreadoPorApellido", language = "JDOQL",
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
			name = "getTecnico", language = "JDOQL", 
			value = "SELECT FROM dom.tecnico.Tecnico WHERE creadoPor == :creadoPor")
})

@ObjectType("TECNICO")
@Audited
@AutoComplete(repository=TecnicoServicio.class, action="autoComplete") //
// default unless overridden by autoCompleteNXxx() method
// @Bounded - if there were a small number of instances only (overrides
// autoComplete functionality)
@Bookmarkable
public class Tecnico extends Persona implements Comparable<Tecnico>{
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
	
	
	@javax.jdo.annotations.Persistent(table="TecnicoDependencias")
	@javax.jdo.annotations.Join(column="dependingId")
	@javax.jdo.annotations.Element(column="dependentId")
	private SortedSet<Tecnico> dependencias = new TreeSet<Tecnico>();
	
	
	@SortedBy(DependenciesComparatorTecnico.class)
	public SortedSet<Tecnico> getDependencias(){
		return dependencias;
	}
	public void setDependencias(final SortedSet<Tecnico> dependencias){
		this.dependencias = dependencias;
	}
	
	
	@PublishedAction
	public Tecnico agregar(
			@TypicalLength(20)
			final Tecnico tecnico){
		getDependencias().add(tecnico);
		return this;
	}
	
	
	public List<Tecnico> autoComplete0Agregar(final @MaxLength(2) String apellido){
		final List<Tecnico> lista = tecnicoServicio.autoComplete(apellido);;
		lista.removeAll(getDependencias());
		lista.remove(this);
		return lista;
	}
	
	
	public String disableAgregar(final Tecnico tecnico){
		if(isComplete()){
			return "No se puede agregar la dependencias";
		}
		return null;
	}
	
	
	//Validar argumento invocado por la accion
	public String validateAgregar(final Tecnico tecnico){
		if(getDependencias().contains(tecnico)){
			return "Ya existe la dependencia";
		}
		if(tecnico == this){
			return "No se puede esa dependencia";
		}
		return null;
	}
	
	
	@Named("Remover")
	public Tecnico remove(
			@TypicalLength(20)
			final Tecnico tecnico){
		getDependencias().remove(tecnico);
		return this;
	}
	
	
	
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
	    return tecnicoServicio.noCompletados(); 
	}
	//}}
	
	
	
	//disabled depende del estado del objeto
	public String disableRemove(final Tecnico tecnico){
		if(isComplete()){
			return "No se puede remove la dependencia";
		}
		return getDependencias().isEmpty() ? "No se puede remover la dependencia" : null;
	}
	
	
	//Validar el argumento invocando la accion
	public String validateRemove(final Tecnico tecnico){
		if(!getDependencias().contains(tecnico)){
			return "No se puede";
		}
		return null;
	}
	
	
	//Hace el drop-down
	public Collection<Tecnico> choicesRemove(){
		return getDependencias();
	}

	
    // //////////////////////////////////////
    // Injected Services
    // //////////////////////////////////////

    @javax.inject.Inject
    private DomainObjectContainer container;
	
	@javax.inject.Inject
	private TecnicoServicio tecnicoServicio;
	
	
    @Override
    public int compareTo(final Tecnico tecnico) {
        return ObjectContracts.compare(this, tecnico, "apellido");
    }
}