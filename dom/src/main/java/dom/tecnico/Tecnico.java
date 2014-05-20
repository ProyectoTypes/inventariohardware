package dom.tecnico;

import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;

import dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")

@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "getAll", language = "JDOQL", value = "SELECT FROM dom.usuario.Usuario WHERE creadoPor == :creadoPor") })

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

}