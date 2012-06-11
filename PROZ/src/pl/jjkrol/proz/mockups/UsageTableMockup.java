package pl.jjkrol.proz.mockups;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.model.BillableService;

/**
 * The Class UsageTableMockup.
 */
public class UsageTableMockup {
	/** The administrative results. */
	private final Map<BillableService, BigDecimal> administrativeResults;
	// TODO change to building mockup

	/** The advancement. */
	private final BigDecimal advancement;

	/** The billable measurement end. */
	private final Map<BillableService, Float> billableMeasurementEnd;

	/** The billable measurement start. */
	private final Map<BillableService, Float> billableMeasurementStart;

	/** The from. */
	private final Calendar from;

	/** The locum. */
	private final String locumName;

	/** The occupant name. */
	private final String occupantName;

	/** The quotation. */
	private final List<QuotationMockup> quotations;

	/** The results. */
	private final Map<BillableService, BigDecimal> results;

	/** The to. */
	private final Calendar to;

	/**
	 * Instantiates a new result mockup.
	 * 
	 * @param locumName
	 *            the locum name
	 * @param occupantName
	 *            the occupant name
	 * @param advancement
	 *            the advancement
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param quotationName
	 *            the quotation name
	 * @param quotations
	 *            the quotation
	 * @param results
	 *            the results
	 * @param administrativeResults
	 *            the administrative results
	 * @param billableMeasurementStart
	 *            the billable measurement start
	 * @param billableMeasurementEnd
	 *            the billable measurement end
	 */
	public UsageTableMockup(final String locumName, final String occupantName,
			final BigDecimal advancement, final Calendar from,
			final Calendar to, final List<QuotationMockup> quotations,
			final Map<BillableService, BigDecimal> results,
			final Map<BillableService, BigDecimal> administrativeResults,
			final Map<BillableService, Float> billableMeasurementStart,
			final Map<BillableService, Float> billableMeasurementEnd) {

		this.locumName = locumName;
		this.from = from;
		this.to = to;
		this.occupantName = occupantName;
		this.advancement = advancement;
		this.quotations = quotations;
		this.results = new HashMap<BillableService, BigDecimal>(results);
		this.administrativeResults =
				new HashMap<BillableService, BigDecimal>(administrativeResults);
		this.billableMeasurementStart =
				new HashMap<BillableService, Float>(billableMeasurementStart);
		this.billableMeasurementEnd =
				new HashMap<BillableService, Float>(billableMeasurementEnd);
	}

	/**
	 * Gets the administrative results.
	 * 
	 * @return the administrativeResults
	 */
	public Map<BillableService, BigDecimal> getAdministrativeResults() {
		return administrativeResults;
	}

	/**
	 * Gets the advancement.
	 * 
	 * @return the advancement
	 */
	public BigDecimal getAdvancement() {
		return advancement;
	}

	/**
	 * Gets the billable measurement end.
	 * 
	 * @return the billableMeasurementEnd
	 */
	public Map<BillableService, Float> getBillableMeasurementEnd() {
		return billableMeasurementEnd;
	}

	/**
	 * Gets the billable measurement start.
	 * 
	 * @return the billableMeasurementStart
	 */
	public Map<BillableService, Float> getBillableMeasurementStart() {
		return billableMeasurementStart;
	}

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * Gets the locum name.
	 * 
	 * @return the locum name
	 */
	public String getLocumName() {
		return locumName;
	}

	/**
	 * Gets the occupant name.
	 * 
	 * @return the occupantName
	 */
	public String getOccupantName() {
		return occupantName;
	}

	/**
	 * Gets the quotations.
	 * 
	 * @return the quotations
	 */
	public List<QuotationMockup> getQuotations() {
		return quotations;
	}

	/**
	 * Gets the results.
	 * 
	 * @return the results
	 */
	public Map<BillableService, BigDecimal> getResults() {
		return results;
	}

	/**
	 * Gets the to.
	 * 
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}
}
