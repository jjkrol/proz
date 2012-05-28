package pl.jjkrol.proz.model;
import pl.jjkrol.proz.controller.QuotationMockup;
public class Quotation {
	private BillableService service;
	private float price;

	public Quotation(float price, BillableService service) {
		this.price = price;
		this.service = service;
	}

	public float getPrice() {
		return price;
	}

	public BillableService getService() {
		return service;
	}
	
	public QuotationMockup getMockup() {
		return new QuotationMockup();
	}
}
