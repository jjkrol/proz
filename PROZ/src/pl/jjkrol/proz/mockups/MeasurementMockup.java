package pl.jjkrol.proz.mockups;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import pl.jjkrol.proz.model.LocumService;

/**
 * Responsible for passing information about a measurement.
 * 
 * @author jjkrol
 */
public class MeasurementMockup {

	/** date of the measurement. */
	private final Calendar date;

	/** values of the measurement. */
	private final Map<LocumService, Float> values;

	/**
	 * Instantiates a new measurement mockup.
	 * 
	 * @param date
	 *            the date
	 * @param values
	 *            the values
	 */
	public MeasurementMockup(final Calendar date,
			final Map<LocumService, Float> values) {
		this.values = values;
		this.date = date;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			return df.format(date.getTime());
		} else {
			return "<nowy odczyt>";
		}
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 * @uml.property name="date"
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 * @uml.property name="values"
	 */
	public Map<LocumService, Float> getValues() {
		return values;
	}
}
