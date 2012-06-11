package pl.jjkrol.proz.events.measurements;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;

/**
 * The Class SaveMeasurementEvent.
 */
public class SaveMeasurementEvent extends PROZEvent {

	/** The locum mockup. */
	private final LocumMockup locumMockup;

	/** The measurement mockup. */
	private final MeasurementMockup measurementMockup;

	/**
	 * Instantiates a new save measurement event.
	 * 
	 * @param locumMockup
	 *            the locum mockup
	 * @param measurementMockup
	 *            the measurement mockup
	 */
	public SaveMeasurementEvent(final LocumMockup locumMockup,
			final MeasurementMockup measurementMockup) {
		this.locumMockup = locumMockup;
		this.measurementMockup = measurementMockup;
	}

	/**
	 * Gets the locum mockup.
	 * 
	 * @return the locumName
	 */
	public LocumMockup getLocumMockup() {
		return locumMockup;
	}

	/**
	 * Gets the measurement mockup.
	 * 
	 * @return the measurementMockup
	 */
	public MeasurementMockup getMeasurementMockup() {
		return measurementMockup;
	}
}
