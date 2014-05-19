package dom.usuario;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedObject;
import org.apache.isis.applib.util.TitleBuffer;

import dom.persona.Persona;
import dom.todo.ToDoItemChangedPayloadFactory;
import dom.todo.ToDoItems;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")

@ObjectType("USUARIO")
@Audited
@PublishedObject(ToDoItemChangedPayloadFactory.class)
@AutoComplete(repository=ToDoItems.class, action="autoComplete") // default unless overridden by autoCompleteNXxx() method
//@Bounded - if there were a small number of instances only (overrides autoComplete functionality)
@Bookmarkable
public class Usuario extends Persona{
	// //////////////////////////////////////
    // Identification in the UI
    // //////////////////////////////////////

    public String title() {
//        final TitleBuffer buf = new TitleBuffer();
//        buf.append(this.getApellido());
//        if (isComplete()) {
//            buf.append("- Completed!");
//        } else {
//            if (getDueBy() != null) {
//                buf.append(" due by", getDueBy());
//            }
//        }
        return this.getApellido();
    }
    
    public String iconName() {
        return "ToDoItem";
    }

}
