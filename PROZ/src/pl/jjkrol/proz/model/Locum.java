package pl.jjkrol.proz.model;

import java.util.*;

import pl.jjkrol.proz.controller.CounterMockup;
import pl.jjkrol.proz.controller.LocumMockup;
import pl.jjkrol.proz.controller.MeasurementMockup;
import pl.jjkrol.proz.controller.OccupantMockup;
import pl.jjkrol.proz.controller.QuotationMockup;
import pl.jjkrol.proz.view.MeasurementsTab;

/**
 * A class representing a single locum
 * 
 * @author jjkrol
 * 
 */
public class Locum implements Measurable {

	/**
	 * Name of the locum (or number), main identifier
	 */
	private final String name;
	/**
	 * Area of the locum in square meters
	 */
	private final float area;

	/**
	 * a factor, which is used for calculating the participation in
	 * administrative costs
	 */
	private float participationFactor;

	/**
	 * a person or company responsible for payments
	 */
	private Occupant billingPerson;

	/**
	 * tells if this locum is rented or used by administration
	 */
	private final Ownership ownership;

	/**
	 * a list of services which are enabled for the locum (and which are billed
	 * in consequence)
	 */
	private List<BillableService> enabledServices =
			new ArrayList<BillableService>();

	/**
	 * list of counters installed in the locum
	 */
	private Map<MeasurableService, Counter> counters =
			new HashMap<MeasurableService, Counter>();

	/**
	 * list of quotations for this locum
	 */
	private Map<String, List<Quotation>> quotations =
			new HashMap<String, List<Quotation>>();

	/**
	 * list of occupants living in this locum
	 */
	private List<Occupant> occupants = new ArrayList<Occupant>();

	public Locum() {
		this.name = "";
		this.area = 0;
		this.ownership = null;
	}
	public Locum(float givenArea, String givenName) {
		this(givenArea, givenName, Ownership.FOR_RENT);
	}

	public Locum(float givenArea, String givenName, Ownership givenOwnership) {
		name = givenName;
		area = givenArea;
		ownership = givenOwnership;
	}

	public void addMeasures(Calendar date,
			Map<MeasurableService, Float> measures) {
		for (MeasurableService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	public List<MeasurementMockup> getMeasurementsMockups() {
		Map<Calendar, Map<MeasurableService, Float>> dates =
				new HashMap<Calendar, Map<MeasurableService, Float>>();
		for (MeasurableService serv : counters.keySet()) {
			Counter c = counters.get(serv);
			for (Calendar date : c.getDates()) {
				if (!dates.containsKey(date)) {
					dates.put(date, new HashMap<MeasurableService, Float>());
				}
				dates.get(date).put(serv, c.getMeasure(date));
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

	public void addOccupant(Occupant occupant) {
		occupants.add(occupant);
	}

	public void addQuotationSet(String name, final List<Quotation> quotations) {
		if (!this.quotations.containsKey(name)) {
			this.quotations.put(name, quotations);
		}
	}

	public float getArea() {
		return area;
	}

	public Occupant getBillingPerson() {
		return billingPerson;
	}

	public List<BillableService> getEnabledServices() {
		return enabledServices;
	}

	public String getName() {
		return name;
	}

	public List<Occupant> getOccupants() {
		return occupants;
	}

	public float getParticipationFactor() {
		return participationFactor;
	}

	public List<Quotation> getQuotationSet(String name)
			throws NoSuchQuotationSet {
		if (quotations.containsKey(name)) {
			return quotations.get(name);
		} else
			throw new NoSuchQuotationSet();
	}

	/**
	 * calculates usage for each counter and returns all
	 */
	public Map<MeasurableService, Float> getUsage(Calendar start, Calendar end) {
		Map<MeasurableService, Float> returnMap =
				new HashMap<MeasurableService, Float>();

		for (MeasurableService service : counters.keySet()) {
			Counter singleCounter = counters.get(service);
			float counterUsage = singleCounter.getUsage(start, end);
			returnMap.put(service, counterUsage);
		}

		return returnMap;
	}

	public void removeOccupant(Occupant occupant) {
		occupants.remove(occupant);
	}

	/**
	 * @param enabledServices
	 *            the enabledServices to set
	 */
	public void setEnabledServices(List<BillableService> enabledServices) {
		this.enabledServices = enabledServices;
	}

	/**
	 * @param counters
	 *            the counters to set
	 */
	public void setCounters(Map<MeasurableService, Counter> counters) {
		this.counters = counters;
	}

	/**
	 * @param quotations
	 *            the quotations to set
	 */
/*	public void setQuotations(
			Map<BillableService, Map<String, Quotation>> quotations) {
		this.quotations = quotations;
	}*/

	public void setBillingPerson(Occupant billingPerson) {
		this.billingPerson = billingPerson;
	}

	public void setParticipationFactor(float participationFactor) {
		this.participationFactor = participationFactor;
	}

	/**
	 * creates a mockup of the Locum including its Occupants, Counters,
	 * EnabledServices and Quotations
	 * 
	 * @return
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
		List<BillableService> enabledServs = new ArrayList<BillableService>(enabledServices); 
		// List<MeasurableService> servs = new LinkedList<MeasureableSerivce>();
		// for(Occupant occ : occupants){
		// occs.add(occ.getMockup());
		// }
		// List<QuotationMockup> quots = new LinkedList<QuotationMockup>();
		// for(Occupant : occupants){
		// occs.add(occ.getMockup());
		// }
		return new LocumMockup(name, area, participationFactor, occs, counts, enabledServs);
	}

	public Map<String, List<QuotationMockup>> getQuotationsMockups() {
		Map<String, List<QuotationMockup>> mockups = new HashMap<String, List<QuotationMockup>>();
		for(String name : quotations.keySet()) {
			List<QuotationMockup> mocs = new ArrayList<QuotationMockup>();
			for(Quotation quot : quotations.get(name)) {
				mocs.add(quot.getMockup());
			}
			mockups.put(name, mocs);
		}
		return mockups;
	}
}
