package pl.jjkrol.proz.model; 

import java.util.*;

public interface PaymentCalculator {

	public Map<BillableService, Float> calculatePayment(House house, Locum loc,
			Calendar start, Calendar end, String quotationName);


	public Map<BillableService, Float> calculateAdministrativePayment(
			House house, Locum loc, Calendar start, Calendar end,
			String quotationName); 

}
