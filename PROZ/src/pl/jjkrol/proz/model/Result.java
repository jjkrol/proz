package pl.jjkrol.proz.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import pl.jjkrol.proz.mockups.ResultMockup;

// TODO: change to a mockup?
/**
 * Class responsible for storing all data connected with calculated result.
 *
 * @author   jjkrol
 */
public class Result {
	
	/** set of results. */
	private Map<BillableService, BigDecimal> results;
	
	/** set of administrative results. */
	private Map<BillableService, BigDecimal> administrativeResults;
	
	/** building for which the result is calculated. */
	private Building building;
	
	/** locum for which the result is calculated. */
	private Locum locum;
	
	/** date of first measurement. */
	private Calendar from;
	
	/** date of second measurement. */
	private Calendar to;
	
	/** name of quotation set used. */
	private String quotationName;
	
	/** starting billable usage */
	private Map<BillableService, Float> billableMeasurementStart;

	/** ending billable usage */
	private Map<BillableService, Float> billableMeasurementEnd;
	
	/**
	 * @return the billableMeasurementStart
	 */
	Map<BillableService, Float> getBillableMeasurementStart() {
		return billableMeasurementStart;
	}

	/**
	 * @param billableMeasurementStart the billableMeasurementStart to set
	 */
	void setBillableMeasurementStart(Map<BillableService, Float> billableUsageStart) {
		this.billableMeasurementStart = billableUsageStart;
	}

	/**
	 * @return the billableMeasurementEnd
	 */
	Map<BillableService, Float> getBillableMeasurementEnd() {
		return billableMeasurementEnd;
	}

	/**
	 * @param billableUsageEnd the billableMeasurementEnd to set
	 */
	void setBillableMeasurementEnd(Map<BillableService, Float> billableMeasurementEnd) {
		this.billableMeasurementEnd = billableMeasurementEnd;
	}

	
	
	/**
	 * Gets the results.
	 *
	 * @return   the results
	 */
	Map<BillableService, BigDecimal> getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results   the results to set
	 */
	void setResults(Map<BillableService, BigDecimal> results) {
		this.results = results;
	}

	/**
	 * Gets the administrative results.
	 *
	 * @return   the administrativeResults
	 */
	Map<BillableService, BigDecimal> getAdministrativeResults() {
		return administrativeResults;
	}

	/**
	 * Sets the administrative results.
	 *
	 * @param administrativeResults   the administrativeResults to set
	 */
	void setAdministrativeResults(
			Map<BillableService, BigDecimal> administrativeResults) {
		this.administrativeResults = administrativeResults;
	}

	/**
	 * Gets the building.
	 *
	 * @return   the building
	 */
	Building getBuilding() {
		return building;
	}

	/**
	 * Sets the building.
	 *
	 * @param building   the building to set
	 */
	void setBuilding(Building building) {
		this.building = building;
	}

	/**
	 * Gets the locum.
	 *
	 * @return   the locum
	 */
	Locum getLocum() {
		return locum;
	}

	/**
	 * Sets the locum.
	 *
	 * @param locum   the locum to set
	 */
	void setLocum(Locum locum) {
		this.locum = locum;
	}

	/**
	 * Gets the from.
	 *
	 * @return   the from
	 */
	Calendar getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from   the from to set
	 */
	void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return   the to
	 */
	Calendar getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to   the to to set
	 */
	void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * Gets the quotation name.
	 *
	 * @return   the quotationName
	 */
	String getQuotationName() {
		return quotationName;
	}

	/**
	 * Sets the quotation name.
	 *
	 * @param quotationName   the quotationName to set
	 */
	void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}

	/**
	 * Gets the mockup.
	 *
	 * @return the mockup
	 */
	ResultMockup getMockup() {
		return new ResultMockup(building, locum, from, to, quotationName, results,
				administrativeResults, billableMeasurementStart, billableMeasurementEnd);
	}
}
