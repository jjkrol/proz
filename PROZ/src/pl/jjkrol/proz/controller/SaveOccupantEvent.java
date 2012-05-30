package pl.jjkrol.proz.controller;


public class SaveOccupantEvent extends PROZEvent {

	public final OccupantMockup mockup;

	public SaveOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
