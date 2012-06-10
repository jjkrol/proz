package pl.jjkrol.proz.controller.occupants;

import com.itextpdf.text.pdf.qrcode.Mode;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchOccupant;
import pl.jjkrol.proz.view.View;
import org.apache.log4j.Logger;
	/**
	 * deletes occupant from register
	 */
	public class DeleteOccupantDataStrategy extends OccupantsStrategy {
		static Logger logger = Logger.getLogger(PROZStrategy.class);
		public DeleteOccupantDataStrategy(View view, Model model) {
			super(view, model);
		}
		public void execute(final PROZEvent event) {
			OccupantMockup moc = ((DeleteOccupantEvent) event).mockup;
			try {
				model.deleteOccupantData(moc.getId());
			} catch (NoSuchOccupant e) {
				logger.warn(e.getMessage());
			}
			PROZStrategy s = new DisplayOccupantsStrategy(view, model);
			s.execute(event);
		}
	}
