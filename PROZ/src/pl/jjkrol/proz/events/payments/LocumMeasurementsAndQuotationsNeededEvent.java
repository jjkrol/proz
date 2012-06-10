package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.LocumMockup;

/**
 * @author   jjkrol
 */
public class LocumMeasurementsAndQuotationsNeededEvent extends PaymentsEvent {
	/**
	 * @uml.property  name="moc"
	 * @uml.associationEnd  
	 */
	public final LocumMockup moc;
	public LocumMeasurementsAndQuotationsNeededEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
