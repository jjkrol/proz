package pl.jjkrol.proz.model;

import java.util.*;

import pl.jjkrol.proz.mockups.CounterMockup;

/**
 * The Class Counter.
 */
public class Counter {

	/** The id. */
	private int id;

	/** The measures. */
	private Map<Calendar, Float> measures = new HashMap<Calendar, Float>();

	/** The unit. */
	private String unit;

	/**
	 * Instantiates a new counter.
	 */
	protected Counter() {
	}

	/**
	 * Instantiates a new counter.
	 * 
	 * @param givenUnit
	 *            the given unit
	 */
	Counter(final String givenUnit) {
		unit = givenUnit;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	private int getId() {
		return id;
	}

	/**
	 * Gets the measures.
	 * 
	 * @return the measures
	 */
	private Map<Calendar, Float> getMeasures() {
		return measures;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	String getUnit() {
		return unit;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	private void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the measures.
	 * 
	 * @param measures
	 *            the measures to set
	 */
	private void setMeasures(final Map<Calendar, Float> measures) {
		this.measures = measures;
	}

	/**
	 * Sets the unit.
	 * 
	 * @param unit
	 *            the unit to set
	 */
	private void setUnit(final String unit) {
		this.unit = unit;
	}

	/**
	 * Adds the measure.
	 * 
	 * @param date
	 *            the date
	 * @param measure
	 *            the measure
	 */
	void addMeasure(final Calendar date, final float measure) {
		measures.put(date, measure);
	}

	/**
	 * Gets the dates.
	 * 
	 * @return the dates
	 */
	Set<Calendar> getDates() {
		return measures.keySet();
	}

	/**
	 * Gets the measure.
	 * 
	 * @param date
	 *            the date
	 * @return the measure
	 * @throws NoSuchDate
	 *             the no such date
	 */
	float getMeasure(final Calendar date) throws NoSuchDate {
		if (measures.containsKey(date))
			return measures.get(date);
		else
			throw new NoSuchDate();
	}

	/**
	 * Gets the mockup.
	 * 
	 * @return the mockup
	 */
	CounterMockup getMockup() {
		return new CounterMockup(measures, unit);
	}

	/**
	 * Gets the usage.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the usage
	 */
	float getUsage(final Calendar start, final Calendar end) {
		float startMeasure, endMeasure;

		// TODO change to exceptions
		startMeasure = measures.containsKey(start) ? measures.get(start) : 0;
		endMeasure = measures.containsKey(end) ? measures.get(end) : 0;

		return endMeasure - startMeasure;
	}

	/**
	 * Removes the measure.
	 * 
	 * @param date
	 *            the date
	 */
	public void removeMeasure(final Calendar date) {
		measures.remove(date);
	}

}
