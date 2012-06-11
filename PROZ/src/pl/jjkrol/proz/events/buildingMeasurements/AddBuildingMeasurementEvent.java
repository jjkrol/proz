package pl.jjkrol.proz.events.buildingMeasurements;

import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;

/**
 * The Class AddBuildingMeasurementEvent.
 */
public class AddBuildingMeasurementEvent extends BuildingMeasurementsEvent {

	/** The measurement mockup. */
	private final BuildingMeasurementMockup measurementMockup;

	/**
	 * Instantiates a new adds the building measurement event.
	 * 
	 * @param measurementMockup
	 *            the measurement mockup
	 */
	public AddBuildingMeasurementEvent(
			final BuildingMeasurementMockup measurementMockup) {
		this.measurementMockup = measurementMockup;
	}

	/**
	 * Gets the measurement mockup.
	 * 
	 * @return the measurementMockup
	 */
	public BuildingMeasurementMockup getMeasurementMockup() {
		return measurementMockup;
	}

}
