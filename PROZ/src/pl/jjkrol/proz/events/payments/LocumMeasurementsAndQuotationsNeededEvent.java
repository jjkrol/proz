package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.LocumMockup;

/**
 * The Class LocumMeasurementsAndQuotationsNeededEvent.
 * 
 * @author jjkrol
 */
public class LocumMeasurementsAndQuotationsNeededEvent extends PaymentsEvent {

	/** The moc. */
	public final LocumMockup moc;

	/**
	 * Instantiates a new locum measurements and quotations needed event.
	 * 
	 * @param moc
	 *            the moc
	 */
	public LocumMeasurementsAndQuotationsNeededEvent(final LocumMockup moc) {
		this.moc = moc;
	}
}
