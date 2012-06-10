package pl.jjkrol.proz.mockups;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import pl.jjkrol.proz.model.MeasurableService;

/**
 * Responsible for passing information about a measurement
 * @author  jjkrol
 */
public class MeasurementMockup {
	/**
	 * date of the measurement
	 * @uml.property  name="date"
	 */
	private final Calendar date;
	/**
	 * values of the measurement
	 * @uml.property  name="values"
	 */
	private final Map<MeasurableService, Float> values;

	public MeasurementMockup(final Calendar date,
			final Map<MeasurableService, Float> values) {
		this.values = values;
		this.date = date;
	}

	public String toString() {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			return df.format(date.getTime());
		} else {
			return "<nowy odczyt>";
		}
	}

	/**
	 * @return  the date
	 * @uml.property  name="date"
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @return  the values
	 * @uml.property  name="values"
	 */
	public Map<MeasurableService, Float> getValues() {
		return values;
	}
}
