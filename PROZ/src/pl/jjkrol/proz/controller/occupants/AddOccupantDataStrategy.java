package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

	/**
	 * adds data of a new occupant
	 */
	class AddOccupantDataStrategy extends PROZStrategy {
		public AddOccupantDataStrategy (View view, Model model) {
			super(view,model);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void execute(final PROZEvent e) {
			OccupantMockup moc = ((AddOccupantEvent) e).mockup;
			model.addOccupantData(moc);
			PROZStrategy s = new DisplayOccupantsStrategy(view, model);
			s.execute(e);
		}
	}
