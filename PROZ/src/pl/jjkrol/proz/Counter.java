package pl.jjkrol.proz;

import java.util.*;

public class Counter {
	String unit;
	Map<Calendar, Float> measures = new HashMap<Calendar, Float>();

	Counter(String givenUnit) {
		unit = givenUnit;
	}

	void addMeasure(Calendar date, float measure) {
		measures.put(date, measure);
	}

	float getUsage(Calendar start, Calendar end) {
		float startMeasure, endMeasure;

		if (measures.get(start) == null)
			startMeasure = 0;
		else
			startMeasure = measures.get(start);

		if (measures.get(end) == null)
			endMeasure = 0;
		else
			endMeasure = measures.get(end);

		return endMeasure - startMeasure;
	}
}
