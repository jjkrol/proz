package pl.jjkrol.proz.model;

import java.util.Map;
import java.util.Calendar;

/**
 * The Interface Measurable.
 */
interface Measurable {

	/**
	 * Gets the usage.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the usage
	 * @throws NoSuchDate
	 *             the no such date
	 */
	Map<LocumService, Float> getUsage(final Calendar start, final Calendar end)
			throws NoSuchDate;

	/**
	 * Adds the measures.
	 * 
	 * @param date
	 *            the date
	 * @param measures
	 *            the measures
	 */
	void addMeasures(final Calendar date,
			final Map<LocumService, Float> measures);
}
