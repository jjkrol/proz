package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.events.PROZEvent;

public class AddOccupantEvent extends PROZEvent {
	public final OccupantMockup mockup;

	public AddOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
