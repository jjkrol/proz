package pl.jjkrol.proz.model;

import pl.jjkrol.proz.mockups.QuotationMockup;

/**
 * The Class Quotation.
 * 
 * @author jjkrol
 */
public class Quotation {

	/** The service. */
	private BillableService service;

	/** The price. */
	private float price;

	/**
	 * Instantiates a new quotation.
	 * 
	 * @param price
	 *            the price
	 * @param service
	 *            the service
	 */
	public Quotation(final float price, final BillableService service) {
		this.price = price;
		this.service = service;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	public BillableService getService() {
		return service;
	}

	/**
	 * Gets the mockup.
	 * 
	 * @return the mockup
	 */
	public QuotationMockup getMockup() {
		return new QuotationMockup(service, price);
	}
}
