package pl.jjkrol.proz.model;

import java.math.BigDecimal;
import java.util.*;

import pl.jjkrol.proz.mockups.CounterMockup;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import org.apache.log4j.Logger;

/**
 * A class representing a single locum.
 * 
 * @author jjkrol
 */
public class Locum implements Measurable{

	/**
	 * Responsible for representing ownership of a locum.
	 */
	public enum Ownership {

		/** */
		FOR_RENT,
		/** */
		OWN
	}

	/** The logger. */
	static Logger logger = Logger.getLogger(Locum.class);
	/** The area. */
	private float area;
	/** The billing person. */
	private Occupant billingPerson;
	/** The building. */
	private Building building;
	/** list of counters installed in the locum. */
	private Map<MeasurableService, Counter> counters =
			new HashMap<MeasurableService, Counter>();
	/**
	 * a list of services which are enabled for the locum (and which are billed
	 * in consequence). 
	 */
	private List<BillableService> enabledServices =
			new ArrayList<BillableService>();
	/** The name. */
	private String name;
	/** The advancement paid for the media */
	private BigDecimal advancement;
	/** The rent paid for the locum */
	private BigDecimal rent;
	/** list of occupants living in this locum.*/
	private Set<Occupant> occupants = new HashSet<Occupant>();
	/** tells if this locum is rented or used by administration. */
	private Ownership ownership;
	/** The participation factor. */
	private float participationFactor;
	/** list of quotations for this locum. */
	private Map<String, List<Quotation>> quotations =
			new HashMap<String, List<Quotation>>();

	/**
	 * Instantiates a new locum.
	 */
	Locum() {
		this.name = "";
		this.area = 0;
		this.ownership = null;
	}

	/**
	 * Instantiates a new locum.
	 * 
	 * @param givenArea
	 *            the given area
	 * @param givenName
	 *            the given name
	 */
	Locum(float givenArea, String givenName) {
		this(givenArea, givenName, Ownership.FOR_RENT);
	}

	/**
	 * Instantiates a new locum.
	 * 
	 * @param givenArea
	 *            the given area
	 * @param givenName
	 *            the given name
	 * @param givenOwnership
	 *            the given ownership
	 */
	Locum(float givenArea, String givenName, Ownership givenOwnership) {
		name = givenName;
		area = givenArea;
		ownership = givenOwnership;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMeasures(Calendar date,
			Map<MeasurableService, Float> measures) {
		for (MeasurableService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	/**
	 * Adds the occupant.
	 * 
	 * @param occupant
	 *            the occupant
	 */
	void addOccupant(Occupant occupant) {
		occupants.add(occupant);
	}

	/**
	 * Adds the quotation set.
	 * 
	 * @param name
	 *            the name
	 * @param quotations
	 *            the quotations
	 */
	void addQuotationSet(String name, final List<Quotation> quotations) {
		if (!this.quotations.containsKey(name)) {
			this.quotations.put(name, quotations);
		}
	}

	/**
	 * Gets the area.
	 * 
	 * @return the area
	 */
	float getArea() {
		return area;
	}

	/**
	 * Gets the billing person.
	 * 
	 * @return the billing person
	 */
	Occupant getBillingPerson() {
		return billingPerson;
	}

	/**
	 * Gets the counters.
	 * 
	 * @return the counters
	 */
	Map<MeasurableService, Counter> getCounters() {
		return counters;
	}

	/**
	 * Gets the enabled services.
	 * 
	 * @return the enabled services
	 */
	List<BillableService> getEnabledServices() {
		return enabledServices;
	}

	/**
	 * Gets the measurements mockups.
	 * 
	 * @return the measurements mockups
	 */
	List<MeasurementMockup> getMeasurementsMockups() {
		Map<Calendar, Map<MeasurableService, Float>> dates =
				new HashMap<Calendar, Map<MeasurableService, Float>>();
		for (MeasurableService serv : counters.keySet()) {
			Counter c = counters.get(serv);
			for (Calendar date : c.getDates()) {
				if (!dates.containsKey(date)) {
					dates.put(date, new HashMap<MeasurableService, Float>());
				}
				try {
					dates.get(date).put(serv, c.getMeasure(date));
				} catch (NoSuchDate e) {
					logger.warn(e.getMessage());
				}
			}
		}

		List<MeasurementMockup> meas = new LinkedList<MeasurementMockup>();
		TreeSet<Calendar> keys = new TreeSet<Calendar>(dates.keySet());
		for (Calendar date : keys) {
			MeasurementMockup moc =
					new MeasurementMockup(date, dates.get(date));
			meas.add(moc);
		}
		return meas;
	}

	/**
	 * creates a mockup of the Locum including its Occupants, Counters,
	 * EnabledServices and Quotations.
	 * 
	 * @return the mockup
	 */
	public LocumMockup getMockup() {
		List<OccupantMockup> occs = new LinkedList<OccupantMockup>();
		for (Occupant occ : occupants) {
			occs.add(occ.getMockup());
		}
		Map<MeasurableService, CounterMockup> counts =
				new HashMap<MeasurableService, CounterMockup>();
		for (MeasurableService serv : counters.keySet()) {
			Counter c = counters.get(serv);
			counts.put(serv, c.getMockup());
		}
		List<BillableService> enabledServs =
				new ArrayList<BillableService>(enabledServices);
		// List<MeasurableService> servs = new LinkedList<MeasureableSerivce>();
		// for(Occupant occ : occupants){
		// occs.add(occ.getMockup());
		// }
		// List<QuotationMockup> quots = new LinkedList<QuotationMockup>();
		// for(Occupant : occupants){
		// occs.add(occ.getMockup());
		// }
		return new LocumMockup(name, area, participationFactor, occs, counts,
				enabledServs, advancement, rent);
	}

	/**
	 * @return the advancement
	 */
	BigDecimal getAdvancement() {
		return advancement;
	}

	/**
	 * @param advancement the advancement to set
	 */
	void setAdvancement(BigDecimal advancement) {
		this.advancement = advancement;
	}

	/**
	 * @return the rent
	 */
	BigDecimal getRent() {
		return rent;
	}

	/**
	 * @param rent the rent to set
	 */
	void setRent(BigDecimal rent) {
		this.rent = rent;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	String getName() {
		return name;
	}

	/**
	 * Gets the occupants.
	 * 
	 * @return the occupants
	 */
	Set<Occupant> getOccupants() {
		return occupants;
	}

	/**
	 * Gets the ownership.
	 * 
	 * @return the ownership
	 */
	Ownership getOwnership() {
		return ownership;
	}

	/**
	 * Gets the participation factor.
	 * 
	 * @return the participation factor
	 */
	float getParticipationFactor() {
		return participationFactor;
	}

	/**
	 * Gets the quotations.
	 * 
	 * @return the quotations
	 */
	Map<String, List<Quotation>> getQuotations() {
		return quotations;
	}

	/**
	 * Gets the quotation set.
	 * 
	 * @param name
	 *            the name
	 * @return the quotation set
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 */
	List<Quotation> getQuotationSet(String name)
			throws NoSuchQuotationSet {
		if (quotations.containsKey(name)) {
			return quotations.get(name);
		} else
			throw new NoSuchQuotationSet();
	}

	/**
	 * Gets the quotations mockups.
	 * 
	 * @return the quotations mockups
	 */
	public Map<String, List<QuotationMockup>> getQuotationsMockups() {
		Map<String, List<QuotationMockup>> mockups =
				new HashMap<String, List<QuotationMockup>>();
		for (String name : quotations.keySet()) {
			List<QuotationMockup> mocs = new ArrayList<QuotationMockup>();
			for (Quotation quot : quotations.get(name)) {
				mocs.add(quot.getMockup());
			}
			mockups.put(name, mocs);
		}
		return mockups;
	}

	/**
	 * calculates usage for each counter and returns all.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the usage
	 * @throws NoSuchDate 
	 */
	public Map<MeasurableService, Float> getMeasurement(Calendar date) throws NoSuchDate {
		Map<MeasurableService, Float> returnMap =
				new HashMap<MeasurableService, Float>();

		for (MeasurableService service : counters.keySet()) {
			Counter singleCounter = counters.get(service);
			float counterUsage = singleCounter.getMeasure(date);
			returnMap.put(service, counterUsage);
		}

		return returnMap;
	}	
	
	/**
	 * calculates usage for each counter and returns all.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the usage
	 * @throws NoSuchDate 
	 */
	@Override
	public Map<MeasurableService, Float> getUsage(Calendar start, Calendar end) throws NoSuchDate {
		Map<MeasurableService, Float> returnMap =
				new HashMap<MeasurableService, Float>();

		for (MeasurableService service : counters.keySet()) {
			Counter singleCounter = counters.get(service);
			float counterUsage = singleCounter.getUsage(start, end);
			returnMap.put(service, counterUsage);
		}

		return returnMap;
	}

	/**
	 * Removes the occupant.
	 * 
	 * @param occupant
	 *            the occupant
	 */
	void removeOccupant(Occupant occupant) {
		occupants.remove(occupant);
	}

	/**
	 * Sets the area.
	 * 
	 * @param area
	 *            the area to set
	 */
	void setArea(float area) {
		this.area = area;
	}

	/**
	 * Sets the billing person.
	 * 
	 * @param billingPerson
	 *            the new billing person
	 */
	/*
	 * public void setQuotations( Map<BillableService, Map<String, Quotation>>
	 * quotations) { this.quotations = quotations; }
	 */

	void setBillingPerson(Occupant billingPerson) {
		this.billingPerson = billingPerson;
	}

	/**
	 * Sets the counters.
	 * 
	 * @param counters
	 *            the counters to set
	 */
	void setCounters(Map<MeasurableService, Counter> counters) {
		this.counters = counters;
	}

	/**
	 * Sets the enabled services.
	 * 
	 * @param enabledServices
	 *            the enabledServices to set
	 */
	void setEnabledServices(List<BillableService> enabledServices) {
		this.enabledServices = enabledServices;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the occupants.
	 * 
	 * @param occupants
	 *            the occupants to set
	 */
	void setOccupants(Set<Occupant> occupants) {
		this.occupants = occupants;
	}

	/**
	 * Sets the ownership.
	 * 
	 * @param ownership
	 *            the ownership to set
	 */
	void setOwnership(Ownership ownership) {
		this.ownership = ownership;
	}

	/**
	 * Sets the participation factor.
	 * 
	 * @param participationFactor
	 *            the new participation factor
	 */
	void setParticipationFactor(float participationFactor) {
		this.participationFactor = participationFactor;
	}

	/**
	 * Sets the quotations.
	 * 
	 * @param quotations
	 *            the quotations to set
	 */
	void setQuotations(Map<String, List<Quotation>> quotations) {
		this.quotations = quotations;
	}

	/**
	 * Gets the building.
	 * 
	 * @return the building
	 */
	private Building getBuilding() {
		return building;
	}

	/**
	 * Sets the building.
	 * 
	 * @param building
	 *            the new building
	 */
	private void setBuilding(Building building) {
		this.building = building;
	}

	void removeMeasures(Calendar date) {
		for(MeasurableService service : counters.keySet()) {
			Counter count = counters.get(service);
			count.removeMeasure(date);
		}
	}

	void setMeasures(Calendar date, Map<MeasurableService, Float> values) {
		for(MeasurableService service : counters.keySet()) {
			Counter count = counters.get(service);
			count.removeMeasure(date);
			count.addMeasure(date, values.get(service));
		}
	}
}
