package pl.jjkrol.proz.model;

import java.util.*;

public class House implements Measurable {

	private final String name;
	private final String address;
	private List<Locum> locums = new ArrayList<Locum>();
	private Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();

	public House(String givenName, String givenAddress, Map<MeasurableService, Counter> givenCounters) {
		name = givenName;
		address = givenAddress;
		counters = givenCounters;

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
	
	public float getSingleMeasure(Calendar date, MeasurableService service){
		Counter count = counters.get(service);
		return count.getMeasure(date);
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
}
