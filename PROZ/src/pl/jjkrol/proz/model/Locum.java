package pl.jjkrol.proz.model;

import java.util.*;

import pl.jjkrol.proz.controller.LocumMockup;
import pl.jjkrol.proz.controller.OccupantMockup;

/**
 * A class representing a single locum
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
	 * a factor, which is used for calculating the participation
	 * in administrative costs
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
	 * a list of services which are enabled for the locum (and which are billed in consequence)
	 */
	private List<MeasurableService> enabledServices = new ArrayList<MeasurableService>();
	
	/**
	 * list of counters installed in the locum
	 */
	private Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();
	
	/**
	 * list of quotations for this locum
	 */
	private Map<BillableService, Map<String, Quotation>> quotations = new HashMap<BillableService, Map<String, Quotation>>();
	
	/**
	 * list of occupants living in this locum
	 */
	private List<Occupant> occupants = new ArrayList<Occupant>();

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

	public void addOccupant(Occupant occupant) {
		occupants.add(occupant);
	}

	public void addQuotationSet(String date,
			Map<BillableService, Quotation> givenQuotations) {
		for (BillableService serv : givenQuotations.keySet()) {
			Map<String, Quotation> serviceMap = quotations.get(serv);
			Quotation oneQuotation = givenQuotations.get(serv);
			serviceMap.put(date, oneQuotation);
		}
	}

	public float getArea() {
		return area;
	}

	public Occupant getBillingPerson() {
		return billingPerson;
	}

	public List<MeasurableService> getEnabledServices() {
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

	public Map<BillableService, Quotation> getQuotationSet(String date){
		Map<BillableService, Quotation> returnQuotations = new HashMap<BillableService, Quotation>();
		for (BillableService serv : quotations.keySet()){
			Map<String, Quotation> quotationsForService = quotations.get(serv);
			Quotation value = quotationsForService.get(date);			
			returnQuotations.put(serv, value);
		}
		return returnQuotations;
	}

	/**
	 * calculates usage for each counter and returns all
	 */
	public Map<MeasurableService, Float> getUsage(Calendar start, Calendar end) {
		Map<MeasurableService, Float> returnMap = new HashMap<MeasurableService, Float>();

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
	 * @param enabledServices the enabledServices to set
	 */
	public void setEnabledServices(List<MeasurableService> enabledServices) {
		this.enabledServices = enabledServices;
	}

	/**
	 * @param counters the counters to set
	 */
	public void setCounters(Map<MeasurableService, Counter> counters) {
		this.counters = counters;
	}

	/**
	 * @param quotations the quotations to set
	 */
	public void setQuotations(
			Map<BillableService, Map<String, Quotation>> quotations) {
		this.quotations = quotations;
	}

	public void setBillingPerson(Occupant billingPerson) {
		this.billingPerson = billingPerson;
	}
	
	public void setParticipationFactor(float participationFactor) {
		this.participationFactor = participationFactor;
	}
	
	public LocumMockup getMockup(){
		List<OccupantMockup> occs = new LinkedList<OccupantMockup>();
		for(Occupant occ : occupants){
			occs.add(occ.getMockup());
		}
		return new LocumMockup(name, area, participationFactor, occs);
	}
}
