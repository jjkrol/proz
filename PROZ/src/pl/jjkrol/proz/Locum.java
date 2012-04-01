package pl.jjkrol.proz;

import java.util.*;

public class Locum implements Measurable {

	private final String name;
	private final float area;
	private float participationFactor;
	private Occupant billingPerson;

	private List<Service> enabledServices = new ArrayList<Service>();
	private Map<Service, Counter> counters = new HashMap<Service, Counter>();
	private List<Occupant> occupants = new ArrayList<Occupant>();

	Locum(float givenArea, String givenName) {
		name = givenName;
		area = givenArea;
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
	public Map<Service, Float> getUsage(Calendar start, Calendar end) {
		Map<Service, Float> returnMap = new HashMap<Service, Float>();

		for (Service service : counters.keySet()) {
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

	public List<Service> getEnabledServices() {
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

	public void addMeasure() {

	}

}
