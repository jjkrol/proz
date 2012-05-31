package pl.jjkrol.proz.mockups;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.MeasurableService;
/**
 * A mockup for passing Locum data between View and Controller
 * 
 * @author jjkrol
 */
public class LocumMockup {
	
	private final float area;

	private final Map<MeasurableService, CounterMockup> counters;

	private final List<BillableService> enabledServices;

	private final String name;

	private final List<OccupantMockup> occupants;

	private final float participationFactor;

	public LocumMockup(String name, float area,
			float participationFactor, List<OccupantMockup> occupants,
			Map<MeasurableService, CounterMockup> counters,
			List<BillableService> enabledServices) {
		this.name = name;
		this.area = area;
		this.participationFactor = participationFactor;
		this.occupants = occupants;
		this.counters = counters;
		this.enabledServices = enabledServices;
	}
	/**
	 * @return the area
	 */
	public float getArea() {
		return area;
	}
	/**
	 * @return the counters
	 */
	public Map<MeasurableService, CounterMockup> getCounters() {
		return counters;
	}
	/**
	 * @return the enabledServices
	 */
	public List<BillableService> getEnabledServices() {
		return enabledServices;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the occupants
	 */
	public List<OccupantMockup> getOccupants() {
		return occupants;
	}
	
	/**
	 * @return the participationFactor
	 */
	public float getParticipationFactor() {
		return participationFactor;
	}

	// private OccupantMockup billingPerson;
	// private final Ownership ownership;
	//
	// private Map<MeasurableService, Counter> counters = new
	// HashMap<MeasurableService, Counter>();
	// private Map<BillableService, Map<String, Quotation>> quotations = new
	// HashMap<BillableService, Map<String, Quotation>>();
	// private List<Occupant> occupants = new ArrayList<Occupant>();
	
	@Override
	public String toString() {
		return name;
	}
}
