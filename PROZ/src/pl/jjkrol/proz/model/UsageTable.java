package pl.jjkrol.proz.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.mockups.UsageTableMockup;

/**
 * The Class UsageTable.
 */
public class UsageTable {
	/** set of results. */
	private Map<BillableService, BigDecimal> results;

	/** set of administrative results. */
	private Map<BillableService, BigDecimal> administrativeResults;

	/** The locum name. */
	private String locumName;

	/** The occupant name. */
	private String occupantName;

	/** The advancement. */
	private BigDecimal advancement;

	/** date of first measurement. */
	private Calendar from;

	/** date of second measurement. */
	private Calendar to;

	/** starting billable usage. */
	private Map<BillableService, Float> billableMeasurementStart;

	/** ending billable usage. */
	private Map<BillableService, Float> billableMeasurementEnd;

	/** The quotations. */
	private List<QuotationMockup> quotations;

	/**
	 * Gets the results.
	 * 
	 * @return the results
	 */
	Map<BillableService, BigDecimal> getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 * 
	 * @param results
	 *            the results to set
	 */
	void setResults(final Map<BillableService, BigDecimal> results) {
		this.results = results;
	}

	/**
	 * Gets the administrative results.
	 * 
	 * @return the administrativeResults
	 */
	Map<BillableService, BigDecimal> getAdministrativeResults() {
		return administrativeResults;
	}

	/**
	 * Sets the administrative results.
	 * 
	 * @param administrativeResults
	 *            the administrativeResults to set
	 */
	void setAdministrativeResults(
			final Map<BillableService, BigDecimal> administrativeResults) {
		this.administrativeResults = administrativeResults;
	}

	/**
	 * Gets the locum name.
	 * 
	 * @return the locumName
	 */
	String getLocumName() {
		return locumName;
	}

	/**
	 * Sets the locum name.
	 * 
	 * @param locumName
	 *            the locumName to set
	 */
	void setLocumName(final String locumName) {
		this.locumName = locumName;
	}

	/**
	 * Gets the occupant name.
	 * 
	 * @return the occupantName
	 */
	String getOccupantName() {
		return occupantName;
	}

	/**
	 * Sets the occupant name.
	 * 
	 * @param occupantName
	 *            the occupantName to set
	 */
	void setOccupantName(final String occupantName) {
		this.occupantName = occupantName;
	}

	/**
	 * Gets the advancement.
	 * 
	 * @return the advancement
	 */
	BigDecimal getAdvancement() {
		return advancement;
	}

	/**
	 * Sets the advancement.
	 * 
	 * @param advancement
	 *            the advancement to set
	 */
	void setAdvancement(final BigDecimal advancement) {
		this.advancement = advancement;
	}

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	Calendar getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 * 
	 * @param from
	 *            the from to set
	 */
	void setFrom(final Calendar from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 * 
	 * @return the to
	 */
	Calendar getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 * 
	 * @param to
	 *            the to to set
	 */
	void setTo(final Calendar to) {
		this.to = to;
	}

	/**
	 * Gets the billable measurement start.
	 * 
	 * @return the billableMeasurementStart
	 */
	Map<BillableService, Float> getBillableMeasurementStart() {
		return billableMeasurementStart;
	}

	/**
	 * Sets the billable measurement start.
	 * 
	 * @param billableMeasurementStart
	 *            the billableMeasurementStart to set
	 */
	void setBillableMeasurementStart(
			final Map<BillableService, Float> billableMeasurementStart) {
		this.billableMeasurementStart = billableMeasurementStart;
	}

	/**
	 * Gets the billable measurement end.
	 * 
	 * @return the billableMeasurementEnd
	 */
	Map<BillableService, Float> getBillableMeasurementEnd() {
		return billableMeasurementEnd;
	}

	/**
	 * Sets the billable measurement end.
	 * 
	 * @param billableMeasurementEnd
	 *            the billableMeasurementEnd to set
	 */
	void setBillableMeasurementEnd(
			final Map<BillableService, Float> billableMeasurementEnd) {
		this.billableMeasurementEnd = billableMeasurementEnd;
	}

	/**
	 * Instantiates a new usage table.
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
	 * @param quotations
	 *            the quotations
	 * @param results
	 *            the results
	 * @param administrativeResults
	 *            the administrative results
	 * @param billableMeasurementStart
	 *            the billable measurement start
	 * @param billableMeasurementEnd
	 *            the billable measurement end
	 */
	public UsageTable(final String locumName, final String occupantName,
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
	 * Gets the mockup.
	 * 
	 * @return the mockup
	 */
	public UsageTableMockup getMockup() {
		return new UsageTableMockup(locumName, occupantName, advancement, from,
				to, quotations, results, administrativeResults,
				billableMeasurementStart, billableMeasurementEnd);

	}
}
