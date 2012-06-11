package pl.jjkrol.proz.events.measurements;

import pl.jjkrol.proz.mockups.LocumMockup;

/**
 * The Class LocumChosenForViewingEvent.
 * 
 * @author jjkrol
 */
public class LocumChosenForViewingEvent extends MeasurementsEvent {

	/** The mockup. */
	public final LocumMockup moc;

	/**
	 * Instantiates a new locum chosen for viewing event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public LocumChosenForViewingEvent(final LocumMockup moc) {
		this.moc = moc;
	}
}
