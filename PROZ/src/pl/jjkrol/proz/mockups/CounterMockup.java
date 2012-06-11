package pl.jjkrol.proz.mockups;

import java.util.Calendar;
import java.util.Map;

/**
 * The Class CounterMockup.
 * 
 * @TODO use this
 * @author jjkrol
 */
public class CounterMockup {

	/** The measures. */
	private final Map<Calendar, Float> measures;

	/** The unit. */
	private final String unit;

	/**
	 * Instantiates a new counter mockup.
	 * 
	 * @param measures
	 *            the measures
	 * @param unit
	 *            the unit
	 */
	public CounterMockup(final Map<Calendar, Float> measures, final String unit) {
		this.measures = measures;
		this.unit = unit;
	}
}
