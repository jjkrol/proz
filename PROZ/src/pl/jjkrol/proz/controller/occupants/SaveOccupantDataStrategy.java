package pl.jjkrol.proz.controller.occupants;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.SaveOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchOccupant;
import pl.jjkrol.proz.view.View;
import org.apache.log4j.Logger;
	/**
	 * saves occupant data
	 */
	public class SaveOccupantDataStrategy extends OccupantsStrategy {
		static Logger logger = Logger.getLogger("strategy");
		public SaveOccupantDataStrategy(View view, Model model) {
			super(view, model);
		}
		public void execute(final PROZEvent event) {
			OccupantMockup moc = ((SaveOccupantEvent) event).mockup;
			try {
				model.saveOccupant(moc.getId(), moc);
			} catch (NoSuchOccupant e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			PROZStrategy s = new DisplayOccupantsStrategy(view, model);
			s.execute(event);
		}
	}
