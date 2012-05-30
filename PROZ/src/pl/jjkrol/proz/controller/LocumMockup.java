package pl.jjkrol.proz.controller;
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
	public final String name;
	public final float area;
	public final float participationFactor;
	public final List<OccupantMockup> occupants;
	public final Map<MeasurableService, CounterMockup> counters;
	public final List<BillableService> enabledServices;
	
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
