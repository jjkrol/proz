package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;


public class LocumMeasurementsAndQuotationsNeededEvent extends PROZEvent {
	public final LocumMockup moc;
	public LocumMeasurementsAndQuotationsNeededEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
