package pl.jjkrol.proz;

import java.util.Map;
import java.util.Calendar;

public interface Measurable {
	Map<Service, Float>getUsage(Calendar start, Calendar end);
}
