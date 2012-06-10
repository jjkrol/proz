package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.ResultMockup;

/**
 * @author   jjkrol
 */
public class GenerateUsageTableEvent extends PaymentsEvent {
	public final ResultMockup result;

	public GenerateUsageTableEvent(ResultMockup result ) {
		this.result = result;
	}
}
