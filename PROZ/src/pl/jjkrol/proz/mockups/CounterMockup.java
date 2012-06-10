package pl.jjkrol.proz.mockups;

import java.util.Calendar;
import java.util.Map;

/**
 * @TODO use this
 * @author jjkrol
 */
public class CounterMockup {
	private final Map<Calendar, Float> measures;
	private final String unit;
	
	public CounterMockup(Map<Calendar, Float> measures, String unit){
		this.measures = measures;
		this.unit = unit;
	}
}
