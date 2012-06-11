package pl.jjkrol.proz.mockups;

import java.util.Map;
import pl.jjkrol.proz.model.BillableService;

/**
 * The Class QuotationMockup.
 * 
 * @TODO start using this
 * @author jjkrol
 */
public class QuotationMockup {

	/** The service. */
	private final BillableService service;

	/** The value. */
	private final Float value;

	/**
	 * Instantiates a new quotation mockup.
	 * 
	 * @param service
	 *            the service
	 * @param value
	 *            the value
	 */
	public QuotationMockup(final BillableService service, final Float value) {
		this.service = service;
		this.value = value;
	}
}
