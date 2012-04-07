package pl.jjkrol.proz;

import java.util.*;

public class Locum implements Measurable {

	private final String name;
	private final float area;
	private float participationFactor;
	private Occupant billingPerson;
	private final Ownership ownership;

	private List<MeasurableService> enabledServices = new ArrayList<MeasurableService>();
	private Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();
	private Map<BillableService, Map<String, Quotation>> quotations = new HashMap<BillableService, Map<String, Quotation>>();
	private List<Occupant> occupants = new ArrayList<Occupant>();

	Locum(float givenArea, String givenName) {
		this(givenArea, givenName, Ownership.FOR_RENT);
	}

	Locum(float givenArea, String givenName, Ownership givenOwnership) {
		name = givenName;
		area = givenArea;
		ownership = givenOwnership;

		// /@TODO get rid of it!
		counters.put(MeasurableService.CO, new Counter("m3"));
		counters.put(MeasurableService.ZW, new Counter("m3"));
		counters.put(MeasurableService.CW, new Counter("m3"));
		counters.put(MeasurableService.CCW, new Counter("m3"));
		counters.put(MeasurableService.GAZ, new Counter("m3"));
		counters.put(MeasurableService.EE, new Counter("kWh"));

		quotations.put(BillableService.CO, new HashMap<String, Quotation>());
		quotations
				.put(BillableService.WODA, new HashMap<String, Quotation>());
		quotations.put(BillableService.EE, new HashMap<String, Quotation>());
		quotations.put(BillableService.GAZ, new HashMap<String, Quotation>());
		quotations.put(BillableService.INTERNET,
				new HashMap<String, Quotation>());
		quotations.put(BillableService.PODGRZANIE,
				new HashMap<String, Quotation>());
		quotations.put(BillableService.SMIECI,
				new HashMap<String, Quotation>());
		quotations.put(BillableService.SCIEKI,
				new HashMap<String, Quotation>());

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

	public void setBillingPerson(Occupant billingPerson) {
		this.billingPerson = billingPerson;
	}
	
	public void setParticipationFactor(float participationFactor) {
		this.participationFactor = participationFactor;
	}
}
