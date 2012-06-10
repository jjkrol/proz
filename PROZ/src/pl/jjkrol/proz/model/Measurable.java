package pl.jjkrol.proz.model;

import java.util.Map;
import java.util.Calendar;

interface Measurable {
	Map<MeasurableService, Float>getUsage(Calendar start, Calendar end) throws NoSuchDate;
	void addMeasures(Calendar date, Map<MeasurableService, Float> measures);
}
