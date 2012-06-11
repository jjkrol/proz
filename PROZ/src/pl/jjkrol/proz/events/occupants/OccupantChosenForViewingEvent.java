package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * The Class OccupantChosenForViewingEvent.
 * 
 * @author jjkrol
 */
public class OccupantChosenForViewingEvent extends OccupantsEvent {

	/** The moc. */
	public final OccupantMockup moc;

	/**
	 * Instantiates a new occupant chosen for viewing event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public OccupantChosenForViewingEvent(final OccupantMockup moc) {
		this.moc = moc;
	}
}
