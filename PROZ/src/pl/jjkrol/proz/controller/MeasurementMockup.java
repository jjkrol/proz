package pl.jjkrol.proz.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import pl.jjkrol.proz.model.MeasurableService;

public class MeasurementMockup {
	public final Calendar date;
	public final Map<MeasurableService, Float> values;
	public MeasurementMockup(final Calendar date,
			final Map<MeasurableService, Float> values) {
		this.values = values;
		this.date = date;
	}
	public String toString() {
		if(date != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			return df.format(date.getTime());
		}
		else {
			return "<nowy odczyt>";
		}
	}
}
