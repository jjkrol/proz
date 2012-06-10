package pl.jjkrol.proz.mockups;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.Building;
import pl.jjkrol.proz.model.Locum;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultMockup.
 *
 * @author   jjkrol
 */
public class ResultMockup {
	
	/** The administrative results. */
	private final Map<BillableService, BigDecimal> administrativeResults;
	//TODO change to building mockup
	/** The building. */
	private final Building building;
	
	/** The from. */
	private final Calendar from;
	
	/** The locum. */
	private final LocumMockup locum;
	
	/** The quotation. */
	private final List<QuotationMockup> quotation;
	
	/** The results. */
	private final Map<BillableService, BigDecimal> results;
	
	/** The to. */
	private final Calendar to;
	
	/** The billable measurement start. */
	private Map<BillableService, Float> billableMeasurementStart;
	
	/** The billable measurement end. */
	private Map<BillableService, Float> billableMeasurementEnd;

	/**
	 * Instantiates a new result mockup.
	 *
	 * @param building the building
	 * @param locum the locum
	 * @param from the from
	 * @param to the to
	 * @param quotationName the quotation name
	 * @param results the results
	 * @param administrativeResults the administrative results
	 * @param billableMeasurementStart the billable measurement start
	 * @param billableMeasurementEnd the billable measurement end
	 */
	public ResultMockup(final Building building, final Locum locum, final Calendar from,
			final Calendar to, final String quotationName,
			final Map<BillableService, BigDecimal> results,
			final Map<BillableService, BigDecimal> administrativeResults,
			final Map<BillableService, Float> billableMeasurementStart,
			final Map<BillableService, Float> billableMeasurementEnd ) {
		this.building = building;
		// TODO it makes locum methods public
		this.locum = locum.getMockup();
		this.from = from;
		this.to = to;
		// FIXME no such quotation?
		// TODO it makes locum methods public
		this.quotation = locum.getQuotationsMockups().get(quotationName);
		this.results = new HashMap<BillableService, BigDecimal>(results);
		this.administrativeResults = new HashMap<BillableService, BigDecimal>(administrativeResults);
		this.billableMeasurementStart = new HashMap<BillableService, Float>(billableMeasurementStart);
		this.billableMeasurementEnd = new HashMap<BillableService, Float>(billableMeasurementEnd);

	}

	/**
	 * Gets the administrative results.
	 *
	 * @return  the administrativeResults
	 */
	public Map<BillableService, BigDecimal> getAdministrativeResults() {
		return administrativeResults;
	}

	/**
	 * Gets the from.
	 *
	 * @return  the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @return the billableMeasurementStart
	 */
	public Map<BillableService, Float> getBillableMeasurementStart() {
		return billableMeasurementStart;
	}

	/**
	 * @return the billableMeasurementEnd
	 */
	public Map<BillableService, Float> getBillableMeasurementEnd() {
		return billableMeasurementEnd;
	}

	/**
	 * Gets the locum.
	 *
	 * @return  the locum
	 */
	public LocumMockup getLocum() {
		return locum;
	}

	/**
	 * Gets the quotation.
	 *
	 * @return  the quotation
	 */
	public List<QuotationMockup> getQuotation() {
		return quotation;
	}

	/**
	 * Gets the results.
	 *
	 * @return  the results
	 */
	public Map<BillableService, BigDecimal> getResults() {
		return results;
	}

	/**
	 * Gets the to.
	 *
	 * @return  the to
	 */
	public Calendar getTo() {
		return to;
	}
}
