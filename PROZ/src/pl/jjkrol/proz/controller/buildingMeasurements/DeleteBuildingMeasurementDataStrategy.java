package pl.jjkrol.proz.controller.buildingMeasurements;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.buildingMeasurements.DeleteBuildingMeasurementEvent;
import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class DeleteBuildingMeasurementDataStrategy.
 */
public class DeleteBuildingMeasurementDataStrategy extends BuildingMeasurementStrategy {

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	/**
	 * Instantiates a new delete building measurement data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public DeleteBuildingMeasurementDataStrategy(final View view,
			final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		BuildingMeasurementMockup moc =
				((DeleteBuildingMeasurementEvent) event).getMeasurementMockup();
		model.deleteBuildingMeasurements(moc);
		PROZStrategy s = new DisplayBuildingMeasurementsStrategy(view, model);
		s.execute(event);
	}
}
