package pl.jjkrol.proz;

import java.util.*;

public class House implements Measurable {

	private final String name;
	private final String address;
	List<Locum> locums = new ArrayList<Locum>();
	Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();

	House(String givenName, String givenAddress) {
		name = givenName;
		address = givenAddress;
		counters.put(MeasurableService.GAZ, new Counter("m3"));
		counters.put(MeasurableService.POLEWACZKI, new Counter("m3"));
		counters.put(MeasurableService.EE, new Counter("kWh"));
		counters.put(MeasurableService.WODA_GL, new Counter("m3"));
		counters.put(MeasurableService.EE_ADM, new Counter("kWh"));
		counters.put(MeasurableService.CO, new Counter("m3")); // suma co na
																// liczniku
																// ciep³a
		counters.put(MeasurableService.CIEPLO, new Counter("kWh"));
		counters.put(MeasurableService.CO_ADM, new Counter("m3"));
		counters.put(MeasurableService.CW, new Counter("m3"));

	}

	public void addLocum(Locum loc) {
		locums.add(loc);
	}

	public void addMeasure(Calendar date, MeasurableService serv, float measure) {
		Counter count = counters.get(serv);
		count.addMeasure(date, measure);
	}

	public void addMeasures(Calendar date,
			Map<MeasurableService, Float> measures) {
		for (MeasurableService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	public String getAddress() {
		return address;
	}

	public float getCoSum(Calendar start, Calendar end) {
		float coSum = 0f;
		for (Locum loc : getLocums()) {
			coSum += loc.getUsage(start, end).get(MeasurableService.CO);
		}
		return coSum;
	}

	public Set<Calendar> getDates() {
		Set<Calendar> dates = new TreeSet<Calendar>();
		for (MeasurableService serv : counters.keySet()) {
			Counter count = counters.get(serv);
			dates.addAll(count.getDates());
		}
		return dates;
	}

	public float getHeatFactor(Calendar start, Calendar end) {
		float kwhHeat = getUsage(start, end).get(MeasurableService.CIEPLO);
		float m3Heat = getCoSum(start, end);
		float factor = kwhHeat * 0.0036f / m3Heat;

		return factor;
	}

	public List<Locum> getLocums() {
		return locums;
	}

	public String getName() {
		return name;
	}

	/**
	 * calculates usage for the set period of time
	 */
	public Map<MeasurableService, Float> getUsage(Calendar start, Calendar end) {
		Map<MeasurableService, Float> usageMap = new HashMap<MeasurableService, Float>();

		for (MeasurableService serv : counters.keySet()) {
			float usage = counters.get(serv).getUsage(start, end);
			usageMap.put(serv, usage);
		}

		return usageMap;
	}

	void listLocums() {
		for (Locum loc : locums) {
			System.out.println(loc.getName());
		}
	}
}
