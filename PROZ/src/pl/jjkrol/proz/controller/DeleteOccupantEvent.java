package pl.jjkrol.proz.controller;


public class DeleteOccupantEvent extends PROZEvent {

	public final OccupantMockup mockup;

	public DeleteOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
