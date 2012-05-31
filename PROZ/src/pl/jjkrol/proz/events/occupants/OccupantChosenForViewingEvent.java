package pl.jjkrol.proz.events.occupants;

import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.events.PROZEvent;


public class OccupantChosenForViewingEvent extends PROZEvent {
	public final OccupantMockup moc;
	public OccupantChosenForViewingEvent(OccupantMockup moc) {
		this.moc = moc;
	}
}
