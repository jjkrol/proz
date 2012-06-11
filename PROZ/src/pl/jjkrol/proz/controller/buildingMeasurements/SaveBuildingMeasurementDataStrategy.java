package pl.jjkrol.proz.controller.buildingMeasurements;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.buildingMeasurements.SaveBuildingMeasurementEvent;
import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class SaveBuildingMeasurementDataStrategy.
 */
public class SaveBuildingMeasurementDataStrategy extends BuildingMeasurementStrategy {
	
	/**
	 * Instantiates a new save building measurement data strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public SaveBuildingMeasurementDataStrategy(final View view, final Model model) {
		super(view, model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		BuildingMeasurementMockup mea = ((SaveBuildingMeasurementEvent) event).getMeasurementMockup();
		model.saveBuildingMeasurementData(mea);
		PROZStrategy s = new DisplayBuildingMeasurementsStrategy(view, model);
		s.execute(event);
	}
}
