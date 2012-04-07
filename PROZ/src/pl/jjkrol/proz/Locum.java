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
	private Map<BillableService, Map<Calendar, Quotation>> quotations = new HashMap<BillableService, Map<Calendar, Quotation>>();
	private List<Occupant> occupants = new ArrayList<Occupant>();

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

		quotations.put(BillableService.CO, new HashMap<Calendar, Quotation>());
		quotations
				.put(BillableService.WODA, new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.EE, new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.GAZ, new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.INTERNET,
				new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.PODGRZANIE,
				new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.SMIECI,
				new HashMap<Calendar, Quotation>());
		quotations.put(BillableService.SCIEKI,
				new HashMap<Calendar, Quotation>());

	}

	Locum(float givenArea, String givenName) {
		this(givenArea, givenName, Ownership.FOR_RENT);
	}

	public void addMeasures(Calendar date,
			Map<MeasurableService, Float> measures) {
		for (MeasurableService serv : measures.keySet()) {
			Counter count = counters.get(serv);
			count.addMeasure(date, measures.get(serv));
		}
	}

	public String getName() {
		return name;
	}

	public float getArea() {
		return area;
	}

	public float getParticipationFactor() {
		return participationFactor;
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

	public void setParticipationFactor(float participationFactor) {
		this.participationFactor = participationFactor;
	}

	public Occupant getBillingPerson() {
		return billingPerson;
	}

	public void setBillingPerson(Occupant billingPerson) {
		this.billingPerson = billingPerson;
	}

	public List<MeasurableService> getEnabledServices() {
		return enabledServices;
	}

	public List<Occupant> getOccupants() {
		return occupants;
	}

	public void addOccupant(Occupant occupant) {
		occupants.add(occupant);
	}

	public void removeOccupant(Occupant occupant) {
		occupants.remove(occupant);
	}

	public void addQuotationSet(Calendar date,
			Map<BillableService, Quotation> givenQuotations) {
		for (BillableService serv : givenQuotations.keySet()) {
			Map<Calendar, Quotation> serviceMap = quotations.get(serv);
			Quotation oneQuotation = givenQuotations.get(serv);
			serviceMap.put(date, oneQuotation);
		}
	}
	
	public Map<BillableService, Quotation> getQuotationSet(Calendar date){
		Map<BillableService, Quotation> returnQuotations = new HashMap<BillableService, Quotation>();
		for (BillableService serv : quotations.keySet()){
			Map<Calendar, Quotation> quotationsForService = quotations.get(serv);
			Quotation value = quotationsForService.get(date);			
			returnQuotations.put(serv, value);
		}
		return returnQuotations;
	}
}
