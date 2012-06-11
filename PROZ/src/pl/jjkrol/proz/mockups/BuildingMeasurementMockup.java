package pl.jjkrol.proz.mockups;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import pl.jjkrol.proz.model.BuildingService;

/**
 * The Class BuildingMeasurementMockup.
 */
public class BuildingMeasurementMockup {

	/** date of the measurement. */
	private final Calendar date;

	/** values of the measurement. */
	private final Map<BuildingService, Float> values;

	/**
	 * Instantiates a new measurement mockup.
	 * 
	 * @param date
	 *            the date
	 * @param values
	 *            the values
	 */
	public BuildingMeasurementMockup(final Calendar date,
			final Map<BuildingService, Float> values) {
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
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public Map<BuildingService, Float> getValues() {
		return values;
	}
}
