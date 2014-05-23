package dom.sector;

import org.apache.isis.applib.annotation.Named;

@Named("SECTOR")
public class SectorServicio {
	// //////////////////////////////////////
	// Identification in the UI
	// //////////////////////////////////////

	public String getId() {
		return "sector";
	}

	public String iconName() {
		return "ownedBy";
	}
	
}