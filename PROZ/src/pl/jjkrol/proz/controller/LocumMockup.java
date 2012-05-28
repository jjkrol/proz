package pl.jjkrol.proz.controller;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.model.MeasurableService;
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
	public Map<MeasurableService, CounterMockup> counters;

	public LocumMockup(String name, float area,
			float participationFactor, List<OccupantMockup> occupants,
			Map<MeasurableService, CounterMockup> counters) {
		this.name = name;
		this.area = area;
		this.participationFactor = participationFactor;
		this.occupants = occupants;
		this.counters = counters;
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
		return name;
	}
}
