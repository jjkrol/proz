package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * @author   jjkrol
 */
public class OccupantChosenForViewingEvent extends OccupantsEvent {
	/**
	 * @uml.property  name="moc"
	 * @uml.associationEnd  
	 */
	public final OccupantMockup moc;
	public OccupantChosenForViewingEvent(OccupantMockup moc) {
		this.moc = moc;
	}
}
