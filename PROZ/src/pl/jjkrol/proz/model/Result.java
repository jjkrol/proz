package pl.jjkrol.proz.model;

import java.util.Calendar;
import java.util.Map;
import pl.jjkrol.proz.mockups.ResultMockup;

/**
 * Class responsible for storing all data connected with calculated result
 * 
 * @author jjkrol
 * 
 */
public class Result {
	private Map<BillableService, Float> results;
	private Map<BillableService, Float> administrativeResults;
	private Building building;
	private Locum locum;
	private Calendar from;
	private Calendar to;
	private String quotationName;

	/**
	 * @return the results
	 */
	public Map<BillableService, Float> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(Map<BillableService, Float> results) {
		this.results = results;
	}

	/**
	 * @return the administrativeResults
	 */
	public Map<BillableService, Float> getAdministrativeResults() {
		return administrativeResults;
	}

	/**
	 * @param administrativeResults
	 *            the administrativeResults to set
	 */
	public void setAdministrativeResults(
			Map<BillableService, Float> administrativeResults) {
		this.administrativeResults = administrativeResults;
	}

	/**
	 * @return the building
	 */
	public Building getBuilding() {
		return building;
	}

	/**
	 * @param building
	 *            the building to set
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}

	/**
	 * @return the locum
	 */
	public Locum getLocum() {
		return locum;
	}

	/**
	 * @param locum
	 *            the locum to set
	 */
	public void setLocum(Locum locum) {
		this.locum = locum;
	}

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * @return the quotationName
	 */
	public String getQuotationName() {
		return quotationName;
	}

	/**
	 * @param quotationName
	 *            the quotationName to set
	 */
	public void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}

	public ResultMockup getMockup() {
		return new ResultMockup(building, locum, from, to, quotationName, results,
				administrativeResults);
	}
}
