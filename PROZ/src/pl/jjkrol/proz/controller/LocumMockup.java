package pl.jjkrol.proz.controller;

/**
 * A mockup for passing Locum data between View and Controller
 * 
 * @author jjkrol
 */
public class LocumMockup {
	public final String name;
	public final float area;
	public float participationFactor;

	public LocumMockup(String givenName, float givenArea,
			float givenParticipationFactor) {
		name = givenName;
		area = givenArea;
		participationFactor = givenParticipationFactor;
	}

	// private OccupantMockup billingPerson;
	// private final Ownership ownership;
	//
	// private List<MeasurableService> enabledServices = new
	// ArrayList<MeasurableService>();
	// private Map<MeasurableService, Counter> counters = new
	// HashMap<MeasurableService, Counter>();
	// private Map<BillableService, Map<String, Quotation>> quotations = new
	// HashMap<BillableService, Map<String, Quotation>>();
	// private List<Occupant> occupants = new ArrayList<Occupant>();
	@Override
	public String toString() {
		return "Name: " + name + ", area: " + area + ", partFact: "
				+ participationFactor;
	}
}
