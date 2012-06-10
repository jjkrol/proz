package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;

public class SaveMeasurementEvent extends PROZEvent {
	private final LocumMockup locumMockup;
	private final MeasurementMockup measurementMockup;
	public SaveMeasurementEvent(LocumMockup locumMockup, MeasurementMockup measurementMockup) {
		this.locumMockup = locumMockup;
		this.measurementMockup = measurementMockup;
	}
	/**
	 * @return the locumName
	 */
	public LocumMockup getLocumMockup() {
		return locumMockup;
	}
	/**
	 * @return the measurementMockup
	 */
	public MeasurementMockup getMeasurementMockup() {
		return measurementMockup;
	}
}
