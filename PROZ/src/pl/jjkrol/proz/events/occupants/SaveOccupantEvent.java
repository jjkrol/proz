package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * The Class SaveOccupantEvent.
 * 
 * @author jjkrol
 */
public class SaveOccupantEvent extends OccupantsEvent {

	/** The mockup */
	public final OccupantMockup mockup;

	/**
	 * Instantiates a new save occupant event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public SaveOccupantEvent(final OccupantMockup moc) {
		this.mockup = moc;
	}

}
