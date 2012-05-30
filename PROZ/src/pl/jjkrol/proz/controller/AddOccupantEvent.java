package pl.jjkrol.proz.controller;

public class AddOccupantEvent extends PROZEvent {
	public final OccupantMockup mockup;

	public AddOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
