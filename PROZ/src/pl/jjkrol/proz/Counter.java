package pl.jjkrol.proz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
		
		startMeasure = measures.containsKey(start) ? measures.get(start) : 0;
		endMeasure = measures.containsKey(end) ? measures.get(end) : 0;

		return endMeasure - startMeasure;
	}
	Set<Calendar> getDates(){
		return measures.keySet();
	}
}
