package pl.jjkrol.proz.controller.buildingMeasurements;

import java.util.List;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.BuildingMeasurementsTab;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.View;

/**
 * display bulding measurements.
 * 
 * @author jjkrol
 */
public class DisplayBuildingMeasurementsStrategy extends BuildingMeasurementStrategy {

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	/**
	 * Instantiates a new display building measurements strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public DisplayBuildingMeasurementsStrategy(final View view,
			final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent e) {
		try {

			final List<BuildingMeasurementMockup> mocs =
					model.getBuildingMeasurementMockups();

			BuildingMeasurementsTab v =
					(BuildingMeasurementsTab) view
							.getSpecificView(BuildingMeasurementsTab.class);
			v.displayMeasurements(mocs);
		} catch (NoSuchTabException exc) {
			logger.warn("No such tab");
		}
	}
}
