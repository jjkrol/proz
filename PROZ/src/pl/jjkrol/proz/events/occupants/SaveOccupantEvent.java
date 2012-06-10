package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;


/**
 * @author   jjkrol
 */
public class SaveOccupantEvent extends OccupantsEvent {

	/**
	 * @uml.property  name="mockup"
	 * @uml.associationEnd  
	 */
	public final OccupantMockup mockup;

	public SaveOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
