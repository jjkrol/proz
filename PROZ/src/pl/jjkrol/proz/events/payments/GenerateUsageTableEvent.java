package pl.jjkrol.proz.events.payments;

import java.util.Map;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.mockups.ResultMockup;

public class GenerateUsageTableEvent extends PROZEvent {
	public final ResultMockup result;

	public GenerateUsageTableEvent(ResultMockup result ) {
		this.result = result;
	}
}
