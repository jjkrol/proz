package pl.jjkrol.proz.controller;

import com.itextpdf.text.pdf.qrcode.Mode;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

	/**
	 * deletes occupant from register
	 */
	class DeleteOccupantDataStrategy extends PROZStrategy {
		public DeleteOccupantDataStrategy(View view, Model model) {
			super(view, model);
		}
		public void execute(final PROZEvent e) {
			OccupantMockup moc = ((DeleteOccupantEvent) e).mockup;
			model.deleteOccupantData(moc.id);
			PROZStrategy s = new DisplayOccupantsStrategy(view, model);
			s.execute(e);
		}
	}
