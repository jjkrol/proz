package pl.jjkrol.proz.controller;

import java.util.Calendar;
import java.util.Map;

public class CounterMockup {
	final Map<Calendar, Float> measures;
	final String unit;
	
	public CounterMockup(Map<Calendar, Float> measures, String unit){
		this.measures = measures;
		this.unit = unit;
	}
}
