package pl.jjkrol.proz.controller;

public class LocumMeasurementsAndQuotationsNeededEvent extends PROZEvent {
	public final LocumMockup moc;
	public LocumMeasurementsAndQuotationsNeededEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
