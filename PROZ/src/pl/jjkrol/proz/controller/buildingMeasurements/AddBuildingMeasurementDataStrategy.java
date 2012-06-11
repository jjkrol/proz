package pl.jjkrol.proz.controller.buildingMeasurements;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.buildingMeasurements.AddBuildingMeasurementEvent;
import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class AddBuildingMeasurementDataStrategy.
 */
public class AddBuildingMeasurementDataStrategy extends BuildingMeasurementStrategy {

	/**
	 * Instantiates a new adds the building measurement data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public AddBuildingMeasurementDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final PROZEvent event) {
		BuildingMeasurementMockup mea =
				((AddBuildingMeasurementEvent) event).getMeasurementMockup();
		model.addBuildingMeasurementData(mea);
		PROZStrategy s = new DisplayBuildingMeasurementsStrategy(view, model);
		s.execute(event);
	}
}
