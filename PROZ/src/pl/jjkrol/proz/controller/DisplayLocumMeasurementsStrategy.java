package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.view.MeasurementsTab;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.View;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;

import org.apache.log4j.Logger;

/**
 * display single locum measurements on the measurements tab
 */
class DisplayLocumMeasurementsStrategy extends MeasurementsStrategy {
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	DisplayLocumMeasurementsStrategy(View view, Model model) {
		super(view, model);
	}

	public void execute(final PROZEvent e) {
		LocumMockup emptyMockup = ((LocumChosenForViewingEvent) e).moc;
		String locumName = emptyMockup.getName();
		//FIXME: not onmly for Measurements Tab
		try {

			final List<MeasurementMockup> mocs =
					model.getLocumMeasurementMockups(locumName);

			MeasurementsTab v =
					(MeasurementsTab) view
							.getSpecificView(MeasurementsTab.class);
			v.displayMeasurements(mocs);
		} catch (NoSuchLocum exception) {
			// TODO messagebox?
			logger.warn("No such locum: " + locumName + "!");
		} catch (NoSuchTabException exc) {
			logger.warn("No such tab");
		}
	}
}
