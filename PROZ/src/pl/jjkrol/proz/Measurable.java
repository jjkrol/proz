package pl.jjkrol.proz;

import java.util.Map;
import java.util.Calendar;

public interface Measurable {
	Map<MeasurableService, Float>getUsage(Calendar start, Calendar end);
	void addMeasures(Calendar date, Map<MeasurableService, Float> measures);
}
