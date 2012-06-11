package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * The Class AddOccupantEvent.
 * 
 * @author jjkrol
 */
public class AddOccupantEvent extends OccupantsEvent {

	/** The mockup. */
	public final OccupantMockup mockup;

	/**
	 * Instantiates a new adds the occupant event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public AddOccupantEvent(final OccupantMockup moc) {
		this.mockup = moc;
	}

}
