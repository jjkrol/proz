package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.events.PROZEvent;


public class DeleteOccupantEvent extends PROZEvent {

	public final OccupantMockup mockup;

	public DeleteOccupantEvent(OccupantMockup moc) {
		this.mockup = moc;
	}

}
