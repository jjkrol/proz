package pl.jjkrol.proz.mockups;

import java.util.Map;
import pl.jjkrol.proz.model.BillableService;

public class QuotationMockup {
	private final BillableService service;
	private final Float value;
	public QuotationMockup(final BillableService service, final Float value){
		this.service = service;
		this.value = value;
	}
}
