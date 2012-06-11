package pl.jjkrol.proz.controller.measurements;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.events.measurements.SaveMeasurementEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class SaveMeasurementDataStrategy.
 */
public class SaveMeasurementDataStrategy extends PROZStrategy {
	
	/**
	 * Instantiates a new save measurement data strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public SaveMeasurementDataStrategy(final View view, final Model model) {
		super(view, model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		MeasurementMockup mea = ((SaveMeasurementEvent) event).getMeasurementMockup();
		LocumMockup loc = ((SaveMeasurementEvent) event).getLocumMockup();
		model.saveMeasurementData(loc, mea);
		PROZStrategy s = new DisplayLocumMeasurementsStrategy(view, model);
		PROZEvent ev = new LocumChosenForViewingEvent(loc);
		s.execute(ev);
	}
}
