package pl.jjkrol.proz.events.buildingMeasurements;

import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;

/**
 * The Class DeleteBuildingMeasurementEvent.
 */
public class DeleteBuildingMeasurementEvent extends BuildingMeasurementsEvent {

	/** The measurement mockup. */
	private final BuildingMeasurementMockup measurementMockup;

	/**
	 * Instantiates a new delete building measurement event.
	 * 
	 * @param measurementMockup
	 *            the measurement mockup
	 */
	public DeleteBuildingMeasurementEvent(
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
