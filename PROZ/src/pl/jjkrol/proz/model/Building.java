package pl.jjkrol.proz.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;

/**
 * Class represents a bulding with locums and its counters.
 * 
 * @author jjkrol
 */
public class Building {

	/** building's id. */
	private int id;

	/** building's name. */
	private String name;

	/** building's address. */
	private String address;

	/** The locums. */
	private Set<Locum> locums = new HashSet<Locum>();

	/** The counters. */
	private Map<BuildingService, Counter> counters =
			new HashMap<BuildingService, Counter>();

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
	 * @param givenName
	 *            the given name
	 * @param givenAddress
	 *            the given address
	 * @param givenCounters
	 *            the given counters
	 */
	Building(final String givenName, final String givenAddress,
			final Map<BuildingService, Counter> givenCounters) {
		name = givenName;
		address = givenAddress;
		counters = givenCounters;

	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	private void setId(final int id) {
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
	 * @param loc
	 *            the loc
	 */
	public void addLocum(final Locum loc) {
		locums.add(loc);
	}

	/**
	 * Adds the measure.
	 * 
	 * @param date
	 *            the date
	 * @param serv
	 *            the serv
	 * @param measure
	 *            the measure
	 */
	public void addMeasure(final Calendar date, final BuildingService serv,
			final float measure) {
		Counter count = counters.get(serv);
		count.addMeasure(date, measure);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addMeasures(final Calendar date,
			final Map<BuildingService, Float> measures) {
		for (BuildingService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			logger.debug(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	String getAddress() {
		return address;
	}

	/**
	 * Calculates the sum of Central Heating in the specified period.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return central heating sum
	 * @throws NoSuchDate
	 *             the no such date
	 */
	float getCoSum(final Calendar start, final Calendar end) throws NoSuchDate {
		float coSum = 0f;
		for (Locum loc : locums) {
			coSum += loc.getUsage(start, end).get(LocumService.CO);
		}
		return coSum;
	}

	/**
	 * Gets the dates.
	 * 
	 * @return the dates
	 */
	Set<Calendar> getDates() {
		Set<Calendar> dates = new TreeSet<Calendar>();
		for (BuildingService serv : counters.keySet()) {
			Counter count = counters.get(serv);
			dates.addAll(count.getDates());
		}
		return dates;
	}

	/**
	 * Gets the heat factor.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the heat factor
	 * @throws NoSuchDate
	 *             the no such date
	 */
	float getHeatFactor(final Calendar start, final Calendar end)
			throws NoSuchDate {
		float kwhHeat = getUsage(start, end).get(BuildingService.CIEPLO);
		float m3Heat = getCoSum(start, end);
		if (m3Heat > 0)
			return kwhHeat * 0.0036f / m3Heat;
		else
			return 0f;
	}

	/**
	 * Gets the locum by name.
	 * 
	 * @param name
	 *            the name
	 * @return the locum by name
	 * @throws NoSuchLocum
	 *             the no such locum
	 */
	Locum getLocumByName(final String name) throws NoSuchLocum {
		for (Locum loc : locums) {
			if (loc.getName().equals(name))
				return loc;
		}
		throw new NoSuchLocum();
	}

	/**
	 * Gets the counters.
	 * 
	 * @return the counters
	 */
	Map<BuildingService, Counter> getCounters() {
		return counters;
	}

	/**
	 * Sets the counters.
	 * 
	 * @param counters
	 *            the counters to set
	 */
	void setCounters(final Map<BuildingService, Counter> counters) {
		this.counters = counters;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	private void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address
	 *            the address to set
	 */
	private void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * Sets the locums.
	 * 
	 * @param locums
	 *            the locums to set
	 */
	private void setLocums(final Set<Locum> locums) {
		this.locums = locums;
	}

	/**
	 * Gets the locums.
	 * 
	 * @return building's set of locums
	 */
	Set<Locum> getLocums() {
		return locums;
	}

	/**
	 * Gets the name.
	 * 
	 * @return building's name
	 */
	String getName() {
		return name;
	}

	/**
	 * Gets the single measure.
	 * 
	 * @param date
	 *            the date
	 * @param service
	 *            the service
	 * @return the single measure
	 * @throws NoSuchDate
	 *             the no such date
	 */
	float getSingleMeasure(final Calendar date, final BuildingService service)
			throws NoSuchDate {
		Counter count = counters.get(service);
		return count.getMeasure(date);
	}

	/**
	 * calculates usage for the set period of time.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the usage
	 */
	Map<BuildingService, Float> getUsage(final Calendar start,
			final Calendar end) {
		Map<BuildingService, Float> usageMap =
				new HashMap<BuildingService, Float>();

		for (BuildingService serv : counters.keySet()) {
			float usage = counters.get(serv).getUsage(start, end);
			usageMap.put(serv, usage);
		}

		return usageMap;
	}

	/**
	 * Gets the measurement mockups.
	 * 
	 * @return the measurement mockups
	 */
	List<BuildingMeasurementMockup> getMeasurementMockups() {
		Map<Calendar, Map<BuildingService, Float>> dates =
				new HashMap<Calendar, Map<BuildingService, Float>>();
		for (BuildingService serv : counters.keySet()) {
			Counter c = counters.get(serv);
			for (Calendar date : c.getDates()) {
				if (!dates.containsKey(date)) {
					dates.put(date, new HashMap<BuildingService, Float>());
				}
				try {
					dates.get(date).put(serv, c.getMeasure(date));
				} catch (NoSuchDate e) {
					logger.warn(e.getMessage());
				}
			}

		}

		List<BuildingMeasurementMockup> meas =
				new LinkedList<BuildingMeasurementMockup>();
		TreeSet<Calendar> keys = new TreeSet<Calendar>(dates.keySet());
		for (Calendar date : keys) {
			BuildingMeasurementMockup moc =
					new BuildingMeasurementMockup(date, dates.get(date));
			meas.add(moc);
		}
		return meas;
	}

	/**
	 * Sets the measures.
	 * 
	 * @param date
	 *            the date
	 * @param values
	 *            the values
	 */
	void setMeasures(final Calendar date,
			final Map<BuildingService, Float> values) {
		for (BuildingService service : counters.keySet()) {
			Counter count = counters.get(service);
			count.removeMeasure(date);
			count.addMeasure(date, values.get(service));
		}
	}

	/**
	 * Removes the measures.
	 * 
	 * @param date
	 *            the date
	 */
	public void removeMeasures(final Calendar date) {
		for (BuildingService service : counters.keySet()) {
			Counter count = counters.get(service);
			count.removeMeasure(date);
		}

	}
}
