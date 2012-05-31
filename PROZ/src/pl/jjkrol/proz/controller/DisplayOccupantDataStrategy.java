package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.View;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;

/**
 * displays data of a specific occupant
 */
public class DisplayOccupantDataStrategy extends PROZStrategy {
	public DisplayOccupantDataStrategy(View view, Model model) {
		super(view, model);
	}

	@Override
	public void execute(final PROZEvent e) {
		final OccupantMockup moc =
				model.getOccupantMockup(((OccupantChosenForViewingEvent) e).moc);

		OccupantsTab v;
		try {
			v = (OccupantsTab) view.getSpecificView(OccupantsTab.class);
			v.displayOccupantsData(moc);
		} catch (NoSuchTabException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
