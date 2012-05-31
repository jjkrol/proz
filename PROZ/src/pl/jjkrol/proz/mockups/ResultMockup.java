package pl.jjkrol.proz.mockups;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.Building;
import pl.jjkrol.proz.model.Locum;

public class ResultMockup {
	public final Map<BillableService, Float> results;
	public final Map<BillableService, Float> administrativeResults;
	public final Building building;
	public final LocumMockup locum;
	public final Calendar from;
	public final Calendar to;
	public final List<QuotationMockup> quotation;

	public ResultMockup(Building building, Locum locum, Calendar from,
			Calendar to, String quotationName,
			Map<BillableService, Float> results,
			Map<BillableService, Float> administrativeResults) {
		this.building = building;
		this.locum = locum.getMockup();
		this.from = from;
		this.to = to;
		// FIXME no such quotation?
		this.quotation = locum.getQuotationsMockups().get(quotationName);
		this.results = results;
		this.administrativeResults = administrativeResults;

	}
}
