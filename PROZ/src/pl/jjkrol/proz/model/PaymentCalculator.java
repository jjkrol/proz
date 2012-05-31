package pl.jjkrol.proz.model; 

import java.util.*;

public interface PaymentCalculator {
	
	public Result calculate(final Building building, final Locum loc,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchQuotationSet;

	public Map<BillableService, Float> calculatePayment(Building house, Locum loc,
			Calendar start, Calendar end, String quotationName) throws NoSuchQuotationSet;


	public Map<BillableService, Float> calculateAdministrativePayment(
			Building house, Locum loc, Calendar start, Calendar end,
			String quotationName) throws NoSuchQuotationSet; 

}
