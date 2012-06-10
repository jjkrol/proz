package pl.jjkrol.proz.model;
import pl.jjkrol.proz.mockups.QuotationMockup;
/**
 * @author   jjkrol
 */
public class Quotation {
	/**
	 * @uml.property  name="service"
	 * @uml.associationEnd  
	 */
	private BillableService service;
	/**
	 * @uml.property  name="price"
	 */
	private float price;

	public Quotation(float price, BillableService service) {
		this.price = price;
		this.service = service;
	}

	/**
	 * @return
	 * @uml.property  name="price"
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @return
	 * @uml.property  name="service"
	 */
	public BillableService getService() {
		return service;
	}
	
	public QuotationMockup getMockup() {
		return new QuotationMockup(service, price);
	}
}
