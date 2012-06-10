package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * @author   jjkrol
 */
public class DeleteOccupantEvent extends OccupantsEvent {

	/**
	 * Passed empty mockup with filled id
	 * @uml.property  name="mockup"
	 * @uml.associationEnd  
	 */
	public final OccupantMockup mockup;

	public DeleteOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
