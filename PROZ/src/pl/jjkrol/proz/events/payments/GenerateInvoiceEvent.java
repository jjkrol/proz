package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.ResultMockup;

public class GenerateInvoiceEvent extends PROZEvent {

	public final ResultMockup result;
	public GenerateInvoiceEvent(ResultMockup result) {
		this.result = result;
	}

}
