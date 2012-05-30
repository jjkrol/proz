package pl.jjkrol.proz.controller;


public class LocumChosenForViewingEvent extends PROZEvent {
	public final LocumMockup moc;
	public LocumChosenForViewingEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
