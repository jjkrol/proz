package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.View;
import org.apache.log4j.Logger;
/**
 * displays occupants on occupants panel
 */

class DisplayOccupantsStrategy extends OccupantsStrategy {
static Logger logger = Logger.getLogger("strategy");
	public DisplayOccupantsStrategy(View view, Model model) {
		super(view, model);
	}

	@Override
	void execute(final PROZEvent event) {
		List<OccupantMockup> occMocks = model.getOccupantsMockups();
		OccupantsTab v;
		try {
			v = (OccupantsTab) view.getSpecificView(OccupantsTab.class);
			v.displayOccupantsList(occMocks);
		} catch (NoSuchTabException e) {
			logger.warn("No such tab!");
		}
	}
}
