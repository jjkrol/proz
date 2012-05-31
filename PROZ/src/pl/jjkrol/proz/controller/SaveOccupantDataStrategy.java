package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.SaveOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

	/**
	 * saves occupant data
	 */
	class SaveOccupantDataStrategy extends PROZStrategy {
		public SaveOccupantDataStrategy(View view, Model model) {
			super(view, model);
		}
		public void execute(final PROZEvent e) {
			OccupantMockup moc = ((SaveOccupantEvent) e).mockup;
			model.saveOccupant(moc.id, moc);
			PROZStrategy s = new DisplayOccupantsStrategy(view, model);
			s.execute(e);
		}
	}
