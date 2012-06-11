package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;

/**
 * The Class DeleteOccupantEvent.
 * 
 * @author jjkrol
 */
public class DeleteOccupantEvent extends OccupantsEvent {

	/** Passed empty mockup with filled id. */
	public final OccupantMockup mockup;

	/**
	 * Instantiates a new delete occupant event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public DeleteOccupantEvent(final OccupantMockup moc) {
		this.mockup = moc;
	}

}
