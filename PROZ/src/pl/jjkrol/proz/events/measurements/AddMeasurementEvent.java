package pl.jjkrol.proz.events.measurements;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;

/**
 * The Class AddMeasurementEvent.
 */
public class AddMeasurementEvent extends PROZEvent {

	/** The locum mockup. */
	private final LocumMockup locumMockup;

	/** The measurement mockup. */
	private final MeasurementMockup measurementMockup;

	/**
	 * Instantiates a new adds the measurement event.
	 * 
	 * @param locumMockup
	 *            the locum mockup
	 * @param measurementMockup
	 *            the measurement mockup
	 */
	public AddMeasurementEvent(final LocumMockup locumMockup,
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
