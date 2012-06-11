package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.ResultMockup;

/**
 * The Class GenerateInvoiceEvent.
 */
public class GenerateInvoiceEvent extends PROZEvent {

	/** The result. */
	public final ResultMockup result;

	/**
	 * Instantiates a new generate invoice event.
	 * 
	 * @param result
	 *            the result
	 */
	public GenerateInvoiceEvent(final ResultMockup result) {
		this.result = result;
	}

}
