package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

public class SaveMeasurementDataStrategy extends PROZStrategy {
	public SaveMeasurementDataStrategy(View view, Model model) {
		super(view, model);
	}
	public void execute(final PROZEvent event) {
		MeasurementMockup mea = ((SaveMeasurementEvent) event).getMeasurementMockup();
		LocumMockup loc = ((SaveMeasurementEvent) event).getLocumMockup();
		model.saveMeasurementData(loc, mea);
		PROZStrategy s = new DisplayLocumMeasurementsStrategy(view, model);
		PROZEvent ev = new LocumChosenForViewingEvent(loc);
		s.execute(ev);
	}
}
