package pl.jjkrol.proz.controller.measurements;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.measurements.AddMeasurementEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class AddMeasurementDataStrategy.
 */
public class AddMeasurementDataStrategy extends PROZStrategy {

	/**
	 * Instantiates a new adds the measurement data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public AddMeasurementDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final PROZEvent e) {
		MeasurementMockup mea =
				((AddMeasurementEvent) e).getMeasurementMockup();
		LocumMockup loc = ((AddMeasurementEvent) e).getLocumMockup();
		model.addMeasurementData(loc, mea);
		PROZStrategy s = new DisplayLocumMeasurementsStrategy(view, model);
		PROZEvent ev = new LocumChosenForViewingEvent(loc);
		s.execute(ev);
	}
}
