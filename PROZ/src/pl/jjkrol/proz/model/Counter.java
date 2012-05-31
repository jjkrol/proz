package pl.jjkrol.proz.model;

import java.util.*;

import pl.jjkrol.proz.mockups.CounterMockup;

public class Counter {
	String unit;
	Map<Calendar, Float> measures = new HashMap<Calendar, Float>();

	public Counter(final String givenUnit) {
		unit = givenUnit;
	}

	public void addMeasure(final Calendar date, final float measure) {
		measures.put(date, measure);
	}

	float getUsage(final Calendar start, final Calendar end) {
		float startMeasure, endMeasure;

		startMeasure = measures.containsKey(start) ? measures.get(start) : 0;
		endMeasure = measures.containsKey(end) ? measures.get(end) : 0;

		return endMeasure - startMeasure;
	}

	Set<Calendar> getDates() {
		return measures.keySet();
	}
	
	float getMeasure(final Calendar date){
		return measures.get(date);
	}
	
	public CounterMockup getMockup() {
		return new CounterMockup(measures, unit);
	}
}
