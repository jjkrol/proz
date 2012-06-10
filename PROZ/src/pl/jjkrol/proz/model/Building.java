package pl.jjkrol.proz.model;

import java.util.*;
import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * Class represents a bulding with locums and its counters.
 *
 * @author   jjkrol
 */
public class Building implements Measurable {

	/** building's id. */
	private int id;
	
	/** building's name. */
	private String name;
	
	/** building's address. */
	private String address;
	
	/** The locums. */
	private Set<Locum> locums = new HashSet<Locum>();
	
	/** The counters. */
	private Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();
	
	/** The logger. */
	static Logger logger = Logger.getLogger(Building.class);
	
	/**
	 * Instantiates a new building.
	 */
	protected Building() {
		
	}
	
	/**
	 * Instantiates a new building.
	 *
	 * @param givenName the given name
	 * @param givenAddress the given address
	 * @param givenCounters the given counters
	 */
	public Building(String givenName, String givenAddress, Map<MeasurableService, Counter> givenCounters) {
		name = givenName;
		address = givenAddress;
		counters = givenCounters;

	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	private void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	private int getId() {
		return id;
	}

	/**
	 * Adds the locum.
	 *
	 * @param loc the loc
	 */
	public void addLocum(Locum loc) {
		locums.add(loc);
	}

	/**
	 * Adds the measure.
	 *
	 * @param date the date
	 * @param serv the serv
	 * @param measure the measure
	 */
	public void addMeasure(Calendar date, MeasurableService serv, float measure) {
		Counter count = counters.get(serv);
		count.addMeasure(date, measure);
	}

	/* (non-Javadoc)
	 * @see pl.jjkrol.proz.model.Measurable#addMeasures(java.util.Calendar, java.util.Map)
	 */
	public void addMeasures(Calendar date,
			Map<MeasurableService, Float> measures) {
		for (MeasurableService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Calculates the sum of Central Heating in the specified period.
	 *
	 * @param start the start
	 * @param end the end
	 * @return central heating sum
	 * @throws NoSuchDate 
	 */
	public float getCoSum(Calendar start, Calendar end) throws NoSuchDate {
		float coSum = 0f;
		for (Locum loc : locums) {
			coSum += loc.getUsage(start, end).get(MeasurableService.CO);
		}
		return coSum;
	}

	/**
	 * Gets the dates.
	 *
	 * @return the dates
	 */
	public Set<Calendar> getDates() {
		Set<Calendar> dates = new TreeSet<Calendar>();
		for (MeasurableService serv : counters.keySet()) {
			Counter count = counters.get(serv);
			dates.addAll(count.getDates());
		}
		return dates;
	}

	/**
	 * Gets the heat factor.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the heat factor
	 * @throws NoSuchDate 
	 */
	public float getHeatFactor(Calendar start, Calendar end) throws NoSuchDate {
		float kwhHeat = getUsage(start, end).get(MeasurableService.CIEPLO);
		float m3Heat = getCoSum(start, end);
		if(m3Heat > 0)
			return kwhHeat * 0.0036f / m3Heat;
		else
			return 0f;
	}

	/**
	 * Gets the locum by name.
	 *
	 * @param name the name
	 * @return the locum by name
	 * @throws NoSuchLocum the no such locum
	 */
	public Locum getLocumByName(String name) throws NoSuchLocum{
		for(Locum loc : locums) {
			if(loc.getName().equals(name))
				return loc;
		}
		throw new NoSuchLocum();
	}

	/**
	 * Gets the counters.
	 *
	 * @return  the counters
	 */
	Map<MeasurableService, Counter> getCounters() {
		return counters;
	}

	/**
	 * Sets the counters.
	 *
	 * @param counters  the counters to set
	 */
	void setCounters(Map<MeasurableService, Counter> counters) {
		this.counters = counters;
	}

	/**
	 * Gets the logger.
	 *
	 * @return  the logger
	 */
	private static Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger.
	 *
	 * @param logger  the logger to set
	 */
	private static void setLogger(Logger logger) {
		Building.logger = logger;
	}

	/**
	 * Sets the name.
	 *
	 * @param name  the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the address.
	 *
	 * @param address  the address to set
	 */
	private void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the locums.
	 *
	 * @param locums  the locums to set
	 */
	private void setLocums(Set<Locum> locums) {
		this.locums = locums;
	}

	/**
	 * Gets the locums.
	 *
	 * @return  building's set of locums
	 */
	public Set<Locum> getLocums(){
		return locums;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return  building's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the single measure.
	 *
	 * @param date the date
	 * @param service the service
	 * @return the single measure
	 * @throws NoSuchDate 
	 */
	public float getSingleMeasure(Calendar date, MeasurableService service) throws NoSuchDate{
		Counter count = counters.get(service);
		return count.getMeasure(date);
	}
	
	/**
	 * calculates usage for the set period of time.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the usage
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
