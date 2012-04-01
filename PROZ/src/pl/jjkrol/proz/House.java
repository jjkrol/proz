package pl.jjkrol.proz;

import java.util.*;

public class House implements Measurable {

	String name;
	String address;
	List<Locum> locums = new ArrayList<Locum>();
	Map<Service, Counter> counters = new HashMap<Service, Counter>();

	House(String givenName) {
		name = givenName;
		counters.put(Service.CO_ADM, new Counter("m3"));
		counters.put(Service.GAZ, new Counter("m3"));
		counters.put(Service.POLEWACZKI, new Counter("m3"));
		counters.put(Service.EE, new Counter("kWh"));
		counters.put(Service.WODA_GL, new Counter("m3"));
		counters.put(Service.EE_ADM, new Counter("kWh"));
		counters.put(Service.CIEPLO, new Counter("m3"));
		counters.put(Service.CIEPLO_KWH, new Counter("kWh"));
		counters.put(Service.CO, new Counter("m3"));
		counters.put(Service.CW, new Counter("m3"));

	}

	void listLocums() {
		for (Locum loc : locums) {
			System.out.println(loc.getName());
		}
	}

	void addLocum(Locum loc) {
		locums.add(loc);
	}

	public void addMeasures(Calendar date, Map<Service, Float> measures) {
		for (Service serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	/**
	 * calculates usage for the set period of time
	 */
	public Map<Service, Float> getUsage(Calendar start, Calendar end) {
		Map<Service, Float> usageMap = new HashMap<Service, Float>();

		for (Service serv : counters.keySet()) {
			float usage = counters.get(serv).getUsage(start, end);
			usageMap.put(serv, usage);
		}

		return usageMap;
	}
}
