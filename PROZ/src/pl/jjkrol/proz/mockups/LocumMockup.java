package pl.jjkrol.proz.mockups;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.MeasurableService;

/**
 * A mockup for passing Locum data between View and Controller.
 *
 * @author   jjkrol
 */
public class LocumMockup {
	
	/** locum's area. */
	private final float area;
	
	/** counter mockups. */
	private final Map<MeasurableService, CounterMockup> counters;
	
	/** enabled services. */
	private final List<BillableService> enabledServices;
	
	/** locum's name. */
	private final String name;
	
	/** list of locum's occupants' mockups. */
	private final List<OccupantMockup> occupants;
	
	/** locum's participation factor. */
	private final float participationFactor;
	
	/** The advancement. */
	private BigDecimal advancement;
	
	/** The rent. */
	private BigDecimal rent;

	/**
	 * Instantiates a new locum mockup.
	 *
	 * @param name the name
	 * @param area the area
	 * @param participationFactor the participation factor
	 * @param occupants the occupants
	 * @param counters the counters
	 * @param enabledServices the enabled services
	 */
	public LocumMockup(String name, float area,
			float participationFactor, List<OccupantMockup> occupants,
			Map<MeasurableService, CounterMockup> counters,
			List<BillableService> enabledServices,
			BigDecimal advancement,
			BigDecimal rent) {
		this.name = name;
		this.area = area;
		this.participationFactor = participationFactor;
		this.occupants = occupants;
		this.counters = counters;
		this.enabledServices = enabledServices;
		this.advancement = advancement;
		this.rent = rent;
	}
	
	/**
	 * @return the advancement
	 */
	public BigDecimal getAdvancement() {
		return advancement;
	}

	/**
	 * @return the rent
	 */
	public BigDecimal getRent() {
		return rent;
	}

	/**
	 * Gets the area.
	 *
	 * @return   the area
	 * @uml.property  name="area"
	 */
	public float getArea() {
		return area;
	}
	
	/**
	 * Gets the counters.
	 *
	 * @return   the counters
	 */
	public Map<MeasurableService, CounterMockup> getCounters() {
		return counters;
	}
	
	/**
	 * Gets the enabled services.
	 *
	 * @return   the enabledServices
	 */
	public List<BillableService> getEnabledServices() {
		return enabledServices;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return   the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the occupants.
	 *
	 * @return   the occupants
	 */
	public List<OccupantMockup> getOccupants() {
		return occupants;
	}
	
	/**
	 * Gets the participation factor.
	 *
	 * @return   the participationFactor
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
}
