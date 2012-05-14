package pl.jjkrol.proz.controller;
import java.util.List;
/**
 * A mockup for passing Locum data between View and Controller
 * 
 * @author jjkrol
 */
public class LocumMockup {
	public final String name;
	public final float area;
	public final float participationFactor;
	public List<OccupantMockup> occupants;

	public LocumMockup(String givenName, float givenArea,
			float givenParticipationFactor, List<OccupantMockup> givenOccupants) {
		name = givenName;
		area = givenArea;
		participationFactor = givenParticipationFactor;
		occupants = givenOccupants;
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
