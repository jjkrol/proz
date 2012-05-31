package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.events.PROZEvent;


public class SaveOccupantEvent extends PROZEvent {

	public final OccupantMockup mockup;

	public SaveOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
