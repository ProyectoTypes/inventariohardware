package dom.tecnico;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.SortedBy;
import org.apache.isis.applib.annotation.TypicalLength;

import dom.persona.Persona;
import dom.todo.ToDoItem.DependenciesComparator;

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
			name = "getAll", language = "JDOQL", 
			value = "SELECT FROM dom.usuario.Usuario WHERE creadoPor == :creadoPor")
})

@ObjectType("TECNICO")
@Audited
@AutoComplete(repository=TecnicoServicio.class, action="autoComplete") //
// default unless overridden by autoCompleteNXxx() method
// @Bounded - if there were a small number of instances only (overrides
// autoComplete functionality)
@Bookmarkable
public class Tecnico extends Persona {
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
	
	
	@javax.jdo.annotations.Persistent(table="TecnicoDependencias")
	@javax.jdo.annotations.Join(column="dependingId")
	@javax.jdo.annotations.Element(column="dependentId")
	private SortedSet<Tecnico> dependencias = new TreeSet<Tecnico>();
	
	
	@SortedBy(DependenciesComparator.class)
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

	
	@javax.inject.Inject
	private TecnicoServicio tecnicoServicio;
}