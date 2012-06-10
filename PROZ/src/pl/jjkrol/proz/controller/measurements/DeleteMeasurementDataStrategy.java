package pl.jjkrol.proz.controller.measurements;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.measurements.DeleteMeasurementEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchOccupant;
import pl.jjkrol.proz.view.View;

public class DeleteMeasurementDataStrategy extends PROZStrategy {
		static Logger logger = Logger.getLogger(PROZStrategy.class);
	public DeleteMeasurementDataStrategy(View view, Model model) {
		super(view, model);
	}
	public void execute(final PROZEvent event) {
		MeasurementMockup moc = ((DeleteMeasurementEvent) event).getMeasurementMockup();
		LocumMockup loc= ((DeleteMeasurementEvent) event).getLocumMockup();
		model.deleteMeasurements(loc, moc.getDate());
		PROZStrategy s = new DisplayLocumMeasurementsStrategy(view, model);
		PROZEvent ev = new LocumChosenForViewingEvent(loc);
		s.execute(ev);
	}
}
