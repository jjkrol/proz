package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.View;

/**
 * displays occupants on occupants panel
 */

class DisplayOccupantsStrategy extends PROZStrategy {

	public DisplayOccupantsStrategy(View view, Model model) {
		super(view, model);
	}

	@Override
	void execute(final PROZEvent e) {
		List<OccupantMockup> occMocks = model.getOccupantsMockups();
		OccupantsTab v;
		try {
			v = (OccupantsTab) view.getSpecificView(OccupantsTab.class);
			v.displayOccupantsList(occMocks);
		} catch (NoSuchTabException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
