package pl.jjkrol.proz.controller;


public class OccupantChosenForViewingEvent extends PROZEvent {
	public final OccupantMockup moc;
	public OccupantChosenForViewingEvent(OccupantMockup moc) {
		this.moc = moc;
	}
}
