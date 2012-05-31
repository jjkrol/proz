package pl.jjkrol.proz.events;

import pl.jjkrol.proz.mockups.LocumMockup;


public class LocumChosenForViewingEvent extends PROZEvent {
	public final LocumMockup moc;
	public LocumChosenForViewingEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
