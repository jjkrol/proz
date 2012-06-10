package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.measurements.AddMeasurementEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

public class AddMeasurementDataStrategy extends PROZStrategy {
	public AddMeasurementDataStrategy(View view, Model model) {
		super(view, model);
	}

	@Override
	public void execute(final PROZEvent e) {
		MeasurementMockup mea = ((AddMeasurementEvent) e).getMeasurementMockup();
		LocumMockup loc = ((AddMeasurementEvent) e).getLocumMockup();
		model.addMeasurementData(loc, mea);
		PROZStrategy s = new DisplayLocumMeasurementsStrategy(view, model);
		PROZEvent ev = new LocumChosenForViewingEvent(loc);
		s.execute(ev);
	}
}
